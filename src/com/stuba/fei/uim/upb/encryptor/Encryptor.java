package com.stuba.fei.uim.upb.encryptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Encryptor extends JFrame {

    JPanel panel = new JPanel();
    JButton open = new JButton("Open file");
    JButton loadKey = new JButton("Add RSA public");
    JButton crypto = new JButton("Run");
    JButton back = new JButton("Back");
    JTextField filename = new JTextField();
    String fileType = "";
    JTextField rsaKey = new JTextField();
    String aesKeyStr = "";
    String rsaKeyPath = "";
    RsaGenerator rsaGenerator = new RsaGenerator();
    AesGenerator aesGenerator = new AesGenerator();

    public Encryptor() {

        Container container = getContentPane();

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int rVal = chooser.showOpenDialog(Encryptor.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename.setText(chooser.getSelectedFile().getAbsolutePath());
                    fileType = getExtension(chooser.getSelectedFile().getName());
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                }
            }
        });

        loadKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int rVal = chooser.showOpenDialog(Encryptor.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    rsaKey.setText(chooser.getSelectedFile().getAbsolutePath());
                    rsaKeyPath = rsaKey.getText();
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                }
            }
        });

        open.setSize(new Dimension(70, 30));
        loadKey.setSize(new Dimension(70, 30));
        filename.setPreferredSize(new Dimension(150, 30));
        filename.setEditable(false);
        rsaKey.setPreferredSize(new Dimension(150, 30));
        rsaKey.setEditable(false);

        panel.add(filename);
        panel.add(open);
        container.add(panel, BorderLayout.NORTH);

        panel = new JPanel();
        panel.add(rsaKey);
        panel.add(loadKey);
        container.add(panel, BorderLayout.CENTER);

        crypto.setSize(new Dimension(70, 30));
        panel = new JPanel();
        panel.add(crypto);
        panel.add(back);
        container.add(panel, BorderLayout.SOUTH);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new Menu();
                frame.setSize(400, 200);
                Menu.centreWindow(frame);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Encryptor");
                frame.setResizable(false);
                frame.setVisible(true);
                setVisible(false);
                dispose();
            }
        });

        crypto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filename.getText().equals("")) {
                    JOptionPane.showMessageDialog(container,
                            "Choose a file!",
                            "No file choosen",
                            JOptionPane.WARNING_MESSAGE);
                } else if (rsaKey.getText().equals("")) {
                    JOptionPane.showMessageDialog(container,
                            "Add reciever's public key!",
                            "No key added",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    aesKeyStr = aesGenerator.getKey();
                    crypter(filename.getText(), fileType, aesKeyStr, rsaKeyPath);
                    JFrame frame = new Menu();
                    frame.setSize(400, 200);
                    Menu.centreWindow(frame);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setTitle("Encryptor");
                    frame.setResizable(false);
                    frame.setVisible(true);
                    setVisible(false);
                    dispose();
                }
            }
        });
    }

    private String getExtension(String file) {
        String extension = "";
        int i = file.lastIndexOf('.');
        if (i > 0) {
            extension = file.substring(i + 1);
        }
        return extension;
    }

    private void crypter(String filename, String type, String aes, String rsa) {

        File inputFile = new File(filename);
        String msg = "Error";
        String title = "Done";
        boolean done = true;

        try {
            CryptoUtils.encrypt(aes, inputFile, new File("encrypted." + type), rsa);
            msg = "Yr file was encrypted successfully";
        } catch (CryptoException ex) {
            JOptionPane.showMessageDialog(Encryptor.this,
                    "Error encrypting/decrypting file", "Error", JOptionPane.INFORMATION_MESSAGE);
            done = false;
        }
        if (done) {
            JOptionPane.showMessageDialog(Encryptor.this,
                    msg, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
