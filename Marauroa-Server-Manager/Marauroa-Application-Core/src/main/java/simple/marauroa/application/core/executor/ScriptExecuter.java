package simple.marauroa.application.core.executor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ScriptExecuter implements IMarauroaProcess {

    private List<String> commands;
    private String path;
    private String name;
    private Process process;

    public ScriptExecuter(String name, List<String> commands, String path) {
        this.commands = commands;
        this.path = path;
        this.name = name;
    }

    @Override
    public Integer execute() throws ExecutionException, InterruptedException {
        //Register basic singletons
        Callable<Process> marauroaProcess = new Callable<Process>() {

            @Override
            public Process call() throws IOException {
                ProcessBuilder pb = new ProcessBuilder(commands);
                pb.directory(new File(getPath()));
                pb.redirectErrorStream(true);
                process = pb.start();
                return process;
            }
        };

        ExecutionDescriptor descriptor = 
                new ExecutionDescriptor().frontWindow(true)
                        .controllable(true).inputOutput(null);

        ExecutionService service = ExecutionService.newService(marauroaProcess,
                descriptor, getProcessName());

        Future<Integer> task = service.run();
        return task.get();
    }

    @Override
    public String getProcessName() {
        return name;
    }

    @Override
    public void stop() {
        //Do nothing
    }

    /**
     * @return the command
     */
    public List<String> getCommands() {
        return commands;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(List<String> command) {
        this.commands = command;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
