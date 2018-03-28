/*
 * AddRPZoneDialog.java
 *
 * Created on Feb 17, 2011, 3:13:09 PM
 */
package simple.marauroa.application.gui.dialog;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import simple.marauroa.application.api.IMarauroaApplication;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class AddRPZoneDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 8_638_234_523_299_619_988L;

    private final IMarauroaApplication app;

    /**
     * Creates new form AddRPZoneDialog
     */
    public AddRPZoneDialog(java.awt.Frame parent, IMarauroaApplication app) {
        super(parent, true);
        initComponents();
        this.app = app;
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
        zoneName = new javax.swing.JTextField();
        create = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AddRPZoneDialog.class, "AddRPZone.zone.label.name.text")); // NOI18N

        zoneName.setText(org.openide.util.NbBundle.getMessage(AddRPZoneDialog.class, "AddRPZoneDialog.zoneName.text")); // NOI18N

        create.setText(org.openide.util.NbBundle.getMessage(AddRPZoneDialog.class, "AddRPZoneDialog.create.text")); // NOI18N
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createActionPerformed(evt);
            }
        });

        cancel.setText(org.openide.util.NbBundle.getMessage(AddRPZoneDialog.class, "AddRPZoneDialog.cancel.text")); // NOI18N
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zoneName, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(create)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(cancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(zoneName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(create)
                    .addComponent(cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    private void createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createActionPerformed
        //TODO
        DialogDisplayer.getDefault().notifyLater(
                new NotifyDescriptor.Message("This is meant to add a "
                        + "new Zone to the server but is not implemented yet",
                        NotifyDescriptor.WARNING_MESSAGE));
        setVisible(false);
    }//GEN-LAST:event_createActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JButton create;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField zoneName;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            zoneName.setText("");
        }
        super.setVisible(visible);
    }
}
