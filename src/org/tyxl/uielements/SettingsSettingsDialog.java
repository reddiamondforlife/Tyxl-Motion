/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyxl.uielements;

import org.tyxl.controller.MainWindow;

/**
 *
 * @author Daniel
 */
public class SettingsSettingsDialog extends javax.swing.JDialog {
MainWindow mw;
    /**
     * Creates new form SettingsSettingsDialog
     */
    public SettingsSettingsDialog(java.awt.Frame parent, boolean modal, MainWindow mw) {
        super(parent, modal);
        this.mw=mw;
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

        scrollWindowCheckBox = new javax.swing.JCheckBox();
        showVerboseOutputCheckBox = new javax.swing.JCheckBox();
        save = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        scrollWindowCheckBox.setSelected(true);
        scrollWindowCheckBox.setText("Scroll output window");
        scrollWindowCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scrollWindowCheckBoxActionPerformed(evt);
            }
        });

        showVerboseOutputCheckBox.setText("Show verbose output");

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showVerboseOutputCheckBox)
                    .addComponent(scrollWindowCheckBox)
                    .addComponent(save))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(showVerboseOutputCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollWindowCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(save)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scrollWindowCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scrollWindowCheckBoxActionPerformed
        mw.checkScrollWindow();
    }//GEN-LAST:event_scrollWindowCheckBoxActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
       setVisible(false);
    }//GEN-LAST:event_saveActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton save;
    public static javax.swing.JCheckBox scrollWindowCheckBox;
    public static javax.swing.JCheckBox showVerboseOutputCheckBox;
    // End of variables declaration//GEN-END:variables
}
