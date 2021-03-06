package simple.marauroa.application.core.executor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.openide.util.Exceptions;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.api.STATUS;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class DefaultMarauroaProcess implements IMarauroaProcess {

    private final IMarauroaApplication app;
    private String processName = "Marauroa";
    private Process process;
    private MarauroaProcessThread thread;
    private Timer timer;
    private static final Logger LOG =
            Logger.getLogger(DefaultMarauroaProcess.class.getSimpleName());

    public DefaultMarauroaProcess(IMarauroaApplication app,
            String processName) {
        this.app = app;
        this.processName = processName;
    }

    public DefaultMarauroaProcess(IMarauroaApplication app) {
        this.app = app;
        this.processName = app.getName();
    }

    @Override
    public Integer execute() throws ExecutionException, InterruptedException {
        final Callable<Process> marauroaProcess;
        marauroaProcess = new Callable<Process>() {
            @Override
            public Process call() throws IOException {
                ArrayList<String> commands = new ArrayList<>();
                commands.add("java");
                commands.add("-cp");
                commands.add(app.getLibraries());
                commands.add("marauroa.server.marauroad");
                commands.add("-c");
                commands.add(app.getAppINIFileName());
                commands.add("-l");
                if (app.getCommandLine() != null
                        && !app.getCommandLine().isEmpty()) {
                    StringTokenizer st =
                            new StringTokenizer(app.getCommandLine(), " ");
                    while (st.hasMoreTokens()) {
                        commands.add(st.nextToken());
                    }
                }
                for (String cmd : commands) {
                    LOG.info(cmd);
                }
                ProcessBuilder pb = new ProcessBuilder(commands);
                pb.directory(new File(app.getApplicationPath()));
                pb.redirectErrorStream(true);
                process = pb.start();
                return process;
            }
        };
        thread = new MarauroaProcessThread(marauroaProcess);
        thread.start();
        return thread.getTaskResult();
    }

    @Override
    public String getProcessName() {
        return processName;
    }

    @Override
    public void stop() {
        if (process != null) {
            process.destroy();
        }
    }

    private class MarauroaProcessThread extends Thread {

        private final Callable<Process> marauroaProcess;

        public MarauroaProcessThread(Callable<Process> marauroaProcess) {
            this.marauroaProcess = marauroaProcess;
        }
        int taskResult = 0;

        @Override
        public void run() {
            try {
                ExecutionDescriptor descriptor =
                        new ExecutionDescriptor().frontWindow(true)
                        .controllable(true);

                ExecutionService service = ExecutionService.newService(
                        marauroaProcess,
                        descriptor, getProcessName());

                Future<Integer> task = service.run();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (getTaskResult() != 0) {
                            LOG.log(Level.FINE,
                                    "Something went wrong with the task. "
                                    + "Changing status!");
                            app.setStatus(STATUS.STOPPED);
                            //The user needs to reconnect.
                            timer.cancel();
                        }
                    }
                }, 5_000, 5_000);
                taskResult = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            } catch (CancellationException ex) {
                //Do nothing. The user just cancelled it.
            }
        }

        public int getTaskResult() {
            return taskResult;
        }
    }
}
