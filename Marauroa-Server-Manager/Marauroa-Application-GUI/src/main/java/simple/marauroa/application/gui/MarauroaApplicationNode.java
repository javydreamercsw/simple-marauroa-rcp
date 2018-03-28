package simple.marauroa.application.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.swing.*;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.DeleteAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

import simple.marauroa.application.api.ApplicationStatusChangeListener;
import simple.marauroa.application.api.IDiagramManager;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.api.STATUS;
import simple.marauroa.application.core.MarauroaApplication;
import simple.marauroa.application.core.MarauroaApplicationRepository;
import simple.marauroa.application.core.MonitorService;
import simple.marauroa.application.core.tool.Tool;
import simple.marauroa.application.gui.dialog.AddRPZoneDialog;
import simple.marauroa.application.gui.dialog.PluginConfigurationDialog;

/**
 * Represents a MarauroaApplication element within the system
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class MarauroaApplicationNode extends BeanNode
        implements ApplicationStatusChangeListener
{

  private AddRPZoneDialog dialog = null;
  private PluginConfigurationDialog configDialog = null;
  private final PropertyChangeSupport supp = new PropertyChangeSupport(this);
  private IMarauroaApplication application;
  private Action[] actions;
  private static final Logger LOG
          = Logger.getLogger(MarauroaApplicationNode.class.getSimpleName());

  public MarauroaApplicationNode(IMarauroaApplication application)
          throws IntrospectionException
  {
    super(application,
            Children.create(new RPZoneChildFactory(application), true),
            Lookups.singleton(application));
    setDisplayName(application.toStringForDisplay());
    this.application = application;
    application.setStatus(STATUS.STOPPED);
    application.addStatusListener(MarauroaApplicationNode.this);
  }

  @Override
  public Image getIcon(int type)
  {
    Image icon = getMarauroaApplication().getIcon(type);
    if (icon == null)
    {
      try
      {
        icon = Tool.createImage("simple.marauroa.application.gui",
                "resources/images/app.png", "App icon");
      }
      catch (MalformedURLException ex)
      {
        Exceptions.printStackTrace(ex);
      }
      catch (Exception ex)
      {
        Exceptions.printStackTrace(ex);
      }
    }
    return icon;
  }

  @Override
  public Image getOpenedIcon(int i)
  {
    return getIcon(i);
  }

  @Override
  public boolean canDestroy()
  {
    return false;
  }

  @Override
  public Cookie getCookie(Class clazz)
  {
    Children ch = getChildren();

    if (clazz.isInstance(ch))
    {
      return (Cookie) ch;
    }

    return super.getCookie(clazz);
  }

  @Override
  public Action[] getActions(boolean popup)
  {
    actions = new Action[]
    {
      SystemAction.get(DeleteAction.class),
      null,
      new ConfigureAction(),
      new AddRPZoneAction(),
      new StartServerAction(),
      new StopServerAction(),
      new ConnectAction(),
      new DisconnectAction(),
      null,
      new DeleteServerAction()
    };
    updateActionsState(actions);
    return actions;
  }

  /**
   * @return the app
   */
  public IMarauroaApplication getMarauroaApplication()
  {
    return getLookup().lookup(IMarauroaApplication.class);
  }

  private void updateActionsState(Action[] actions)
  {
    for (Action action : actions)
    {
      if (action != null && action instanceof AddRPZoneAction)
      {
        action.setEnabled(application.getStatus().contains(STATUS.CONNECTED));
      }
      if (action != null && action instanceof StartServerAction)
      {
        action.setEnabled(application.getStatus().contains(STATUS.STOPPED));
      }
      if (action != null && action instanceof StopServerAction)
      {
        action.setEnabled(application.getStatus().contains(STATUS.STARTED));
      }
      if (action != null && action instanceof ConnectAction)
      {
        action.setEnabled(application.getStatus().contains(STATUS.DISCONNECTED)
                && application.getStatus().contains(STATUS.STARTED));
      }
      if (action != null && action instanceof DisconnectAction)
      {
        action.setEnabled(application.getStatus().contains(STATUS.CONNECTED)
                && application.getStatus().contains(STATUS.STARTED));
      }
      if (action != null && action instanceof DeleteServerAction)
      {
        action.setEnabled(!application.getStatus().contains(STATUS.STARTED));
      }
      if (action != null && action instanceof ConfigureAction)
      {
        action.setEnabled(true);
      }
    }
  }

  @Override
  public void statusChanged()
  {
    //Update the actions in case something external changed the app status
    LOG.fine(
            "Updating node actions due to status change...");
    actions = getActions(true);
    MonitorService service
            = Lookup.getDefault().lookup(MonitorService.class);
    if (service != null
            && application.getStatus().contains(STATUS.DISCONNECTED))
    {
      LOG.fine(
              "Removing monitor...");
      //Stop any monitors we have on this application
      service.removeMonitor(application.getName());
    }
    if (application.getStatus().contains(STATUS.UPDATED))
    {
      //The contents of the application changed, time to update the
      //visual component
      LOG.info("Need to update!");
      Lookup.getDefault().lookup(IDiagramManager.class)
              .update(application);
      application.getStatus().remove(STATUS.UPDATED);
    }
  }

  private class AddRPZoneAction extends AbstractAction
  {

    private static final long serialVersionUID = 134_982_449_824_934_884L;

    public AddRPZoneAction()
    {
      putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class,
              "add.zone"));
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
      if (dialog == null)
      {
        dialog = new AddRPZoneDialog(new JFrame(),
                getLookup().lookup(IMarauroaApplication.class));
      }
      dialog.setLocationRelativeTo(null);
      dialog.setVisible(true);
    }
  }

  private class ConfigureAction extends AbstractAction
  {

    private static final long serialVersionUID = 7_855_170_160_882_347_996L;

    public ConfigureAction()
    {
      putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class,
              "configure"));
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
      if (configDialog == null)
      {
        configDialog = new PluginConfigurationDialog(new JFrame(),
                getLookup().lookup(IMarauroaApplication.class));
      }
      configDialog.setLocationRelativeTo(null);
      configDialog.setVisible(true);
    }
  }

  private class ConnectAction extends AbstractAction
  {

    private static final long serialVersionUID = 2_597_983_100_407_135_705L;

    public ConnectAction()
    {
      putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class,
              "connect"));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      MonitorService service
              = Lookup.getDefault().lookup(MonitorService.class);
      if (service != null
              && service.getMonitor(application.getName()) == null)
      {
        try
        {
          service.addMonitor(application.getName());
          if (service.getMonitor(application.getName()).isRunning())
          {
            application.setStatus(STATUS.CONNECTED);
          }
        }
        catch (Exception ex)
        {
          Exceptions.printStackTrace(ex);
        }
      }
    }
  }

  private class DisconnectAction extends AbstractAction
  {

    private static final long serialVersionUID = 5_022_199_390_057_555_127L;

    public DisconnectAction()
    {
      putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class,
              "disconnect"));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      MonitorService service
              = Lookup.getDefault().lookup(MonitorService.class);
      if (service != null
              && service.getMonitor(application.getName()) != null)
      {
        try
        {
          service.removeMonitor(application.getName());
          application.setStatus(STATUS.DISCONNECTED);
        }
        catch (Exception ex)
        {
          Exceptions.printStackTrace(ex);
        }
      }
    }
  }

  private class StartServerAction extends AbstractAction
  {

    private static final long serialVersionUID = 829_685_652_449_267_332L;

    public StartServerAction()
    {
      putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class,
              "start.server"));
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
      IMarauroaApplication app
              = getLookup().lookup(IMarauroaApplication.class);
      try
      {
        app.start();
        application.setStatus(STATUS.STARTED);
      }
      catch (Exception ex)
      {
        Exceptions.printStackTrace(ex);
      }
    }
  }

  private class StopServerAction extends AbstractAction
  {

    private static final long serialVersionUID = -1_393_726_358_934_983_469L;

    public StopServerAction()
    {
      putValue(NAME, NbBundle.getMessage(MarauroaApplicationNode.class,
              "stop.server"));
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
      IMarauroaApplication app
              = getLookup().lookup(IMarauroaApplication.class);
      try
      {
        app.shutdown();
        application.setStatus(STATUS.STOPPED);
      }
      catch (Exception ex)
      {
        Exceptions.printStackTrace(ex);
      }
    }
  }

  private class DeleteServerAction extends AbstractAction
  {

    private static final long serialVersionUID = -14_168_899_025_915_506L;

    public DeleteServerAction()
    {
      super(NbBundle.getMessage(MarauroaApplicationNode.class,
              "delete.server"));
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
      NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
              "<html><font size=+1 color=red>"
              + NbBundle.getMessage(
                      MarauroaApplication.class,
                      "acknowledge")
              + "</font><br>"
              + "<br>"
              + NbBundle.getMessage(
                      MarauroaApplicationNode.class,
                      "delete.server.confirmation").replaceAll("%a",
                              getMarauroaApplication().getName())
              + "<b><b> " + NbBundle.getMessage(
                      MarauroaApplication.class,
                      "yes") + "</b> - to Delete<br>"
              + "&nbsp;&nbsp;<b>" + NbBundle.getMessage(
                      MarauroaApplication.class,
                      "no") + "</b> - to Cancel<br>",
              NotifyDescriptor.YES_NO_OPTION);
      Object result = DialogDisplayer.getDefault().notify(nd);
      if (result == NotifyDescriptor.YES_OPTION)
      {
        try
        {
          MarauroaApplicationRepository.deleteApplication(
                  getLookup().lookup(IMarauroaApplication.class));
        }
        catch (Exception ex)
        {
          Exceptions.printStackTrace(ex);
        }
      }
      else if (result == NotifyDescriptor.NO_OPTION)
      {
        //Do nothing
      }
    }
  }
}
