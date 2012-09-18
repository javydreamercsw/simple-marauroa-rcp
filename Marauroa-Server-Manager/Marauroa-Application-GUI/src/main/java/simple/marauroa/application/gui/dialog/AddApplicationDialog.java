/*
 * AddApplicationDialog.java
 *
 * Created on Feb 11, 2011, 4:58:21 PM
 */
package simple.marauroa.application.gui.dialog;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.*;
import simple.marauroa.application.api.IAddApplicationDialogProvider;
import simple.marauroa.application.api.IDataBase;
import simple.marauroa.application.api.IMarauroaApplication;
import simple.marauroa.application.api.IMarauroaApplicationProvider;
import simple.marauroa.application.core.MarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class AddApplicationDialog extends javax.swing.JDialog {

    private final ArrayList<IMarauroaApplication> applications =
            new ArrayList<IMarauroaApplication>();
    private final static RequestProcessor RP = new RequestProcessor("Adding application", 1, true);

    /**
     * Creates new form AddApplicationDialog
     */
    protected AddApplicationDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        options = new javax.swing.JList();
        create = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        appName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(AddApplicationDialog.class, "add.application.title")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AddApplicationDialog.class, "add.application.message")); // NOI18N

        options.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(options);

        create.setText(org.openide.util.NbBundle.getMessage(AddApplicationDialog.class, "AddApplicationDialog.create.text")); // NOI18N
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });

        cancel.setText(org.openide.util.NbBundle.getMessage(AddApplicationDialog.class, "AddApplicationDialog.cancel.text")); // NOI18N
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        nameLabel.setText(org.openide.util.NbBundle.getMessage(AddApplicationDialog.class, "AddApplicationDialog.nameLabel.text")); // NOI18N

        appName.setText(org.openide.util.NbBundle.getMessage(AddApplicationDialog.class, "AddApplicationDialog.appName.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appName, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(create)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(cancel))
                    .addComponent(nameLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(appName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(create)
                    .addComponent(cancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
        setEnabled(false);
        if (options.getSelectedIndex() >= 0) {
            if (MarauroaApplication.validateName(appName.getText())) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Make sure there's no other application with the same name
                            if (!Lookup.getDefault().lookup(IAddApplicationDialogProvider.class).isFolderCreationIgnored()
                                    && Lookup.getDefault().lookup(IDataBase.class).applicationExists(appName.getText())) {
                                DialogDisplayer.getDefault().notifyLater(
                                        new NotifyDescriptor.Message(NbBundle.getMessage(
                                        AddApplicationDialog.class,
                                        "add.application.already.exists").replaceAll("%a", appName.getText()),
                                        NotifyDescriptor.ERROR_MESSAGE));
                                return;
                            }
                            IMarauroaApplication newInstance = applications.get(options.getSelectedIndex()).getClass().newInstance();
                            newInstance.setName(appName.getText());
                            if (!Lookup.getDefault().lookup(IAddApplicationDialogProvider.class).isFolderCreationIgnored()) {
                                newInstance.createAppDirectory();
                            } else {
                                newInstance.setApplicationPath(
                                        newInstance.getApplicationPath()
                                        + System.getProperty("file.separator")
                                        + newInstance.getName());
                            }
                            //Add to database
                            Lookup.getDefault().lookup(IDataBase.class).addApplication(newInstance);
                        } catch (InstantiationException ex) {
                            Exceptions.printStackTrace(ex);
                            setVisible(false);
                        } catch (IllegalAccessException ex) {
                            Exceptions.printStackTrace(ex);
                            setVisible(false);
                        }
                        setVisible(false);
                    }
                };
                final RequestProcessor.Task theTask = RP.create(runnable);

                final ProgressHandle ph = ProgressHandleFactory.createHandle("Adding application", theTask);
                theTask.addTaskListener(new TaskListener() {
                    @Override
                    public void taskFinished(org.openide.util.Task task) {
                        //make sure that we get rid of the ProgressHandle
                        //when the task is finished
                        ph.finish();
                    }
                });

                //start the progresshandle the progress UI will show 500s after
                ph.start();

                //this actually starts the task
                theTask.schedule(0);
            } else {
                DialogDisplayer.getDefault().notifyLater(
                        new NotifyDescriptor.Message(NbBundle.getMessage(
                        AddApplicationDialog.class,
                        "add.application.invalid.name"),
                        NotifyDescriptor.ERROR_MESSAGE));
            }
        } else {
            DialogDisplayer.getDefault().notifyLater(
                    new NotifyDescriptor.Message(NbBundle.getMessage(
                    AddApplicationDialog.class,
                    "add.application.invalid.app.type"),
                    NotifyDescriptor.ERROR_MESSAGE));
        }
    }//GEN-LAST:event_createActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTextField appName;
    private javax.swing.JButton cancel;
    private javax.swing.JButton create;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JList options;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setVisible(boolean visible) {
        //Refresh each time we are shown
        if (visible) {
            final ArrayList<IMarauroaApplicationProvider> applicationTypes =
                    new ArrayList<IMarauroaApplicationProvider>();
            for (IMarauroaApplicationProvider app : Lookup.getDefault().lookupAll(IMarauroaApplicationProvider.class)) {
                //Add them to the options
                if (!applicationTypes.contains(app)) {
                    applicationTypes.add(app);
                    applications.add(app.getTemplate());
                    Logger.getLogger(AddApplicationDialog.class.getSimpleName()).log(Level.FINE,
                            "Adding: {0} to the list of available "
                            + "applications", app.getTemplate().toStringForDisplay());
                }
            }
            options.setModel(new javax.swing.AbstractListModel() {
                final Object[] objects = Lookup.getDefault().lookup(IDataBase.class).getIMarauroaApplications().toArray();

                @Override
                public int getSize() {
                    return applicationTypes.size();
                }

                @Override
                public Object getElementAt(int i) {
                    return applicationTypes.get(i).getTemplate().toStringForDisplay();
                }
            });
        } else {
            //Clear any potential changes done via the IAddApplicationDialogProvider interface
            appName.setEditable(true);
            appName.setText("");
            Lookup.getDefault().lookup(IAddApplicationDialogProvider.class).setIgnoreFolderCreation(false);
        }
        super.setVisible(visible);
    }
}