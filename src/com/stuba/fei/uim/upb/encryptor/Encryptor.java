package com.stuba.fei.uim.upb.encryptor;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class Encryptor extends JFrame {

    JPanel panel = new JPanel();
    JButton open = new JButton("Open file");
    JButton loadKey = new JButton("Add Key");
    JButton crypto = new JButton("Run");
    JTextField filename = new JTextField();
    String fileType = "";
    JTextField key = new JTextField();
    JComboBox<String> comboBox = new JComboBox<>();
    String keyStr = "";

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
                    key.setText(chooser.getSelectedFile().getAbsolutePath());
                    keyStr = openKeyFile(key.getText());
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                }
            }
        });

        open.setSize(new Dimension(70, 30));
        loadKey.setSize(new Dimension(70, 30));
        loadKey.setEnabled(false);
        filename.setPreferredSize(new Dimension(150, 30));
        filename.setEditable(false);
        key.setPreferredSize(new Dimension(150, 30));
        key.setEditable(false);
        comboBox.setPreferredSize(new Dimension(120, 30));
        comboBox.addItem("Encrypt");
        comboBox.addItem("Decrypt");

        panel.add(filename);
        panel.add(open);
        container.add(panel, BorderLayout.NORTH);

        panel = new JPanel();
        panel.add(comboBox);
        panel.add(key);
        panel.add(loadKey);
        container.add(panel, BorderLayout.CENTER);

        crypto.setSize(new Dimension(70, 30));
        panel = new JPanel();
        panel.add(crypto);
        container.add(panel, BorderLayout.SOUTH);

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem().toString().equals("Encrypt"))
                    loadKey.setEnabled(false);
                if (comboBox.getSelectedItem().toString().equals("Decrypt"))
                    loadKey.setEnabled(true);
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
                } else if (Objects.requireNonNull(comboBox.getSelectedItem()).toString().equals("Decrypt") && key.getText().equals("")) {
                    JOptionPane.showMessageDialog(container,
                            "Add a key!",
                            "No key added",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    if (comboBox.getSelectedItem().toString().equals("Encrypt")) {
                        keyStr = generateKey();
                    }
                    crypter(filename.getText(), fileType, Objects.requireNonNull(comboBox.getSelectedItem()).toString(), keyStr);
                    comboBox.setSelectedIndex(0);
                    filename.setText("");
                    key.setText("");
                    keyStr = "";
                }
            }
        });
    }

    private String openKeyFile(String file) {
        String key = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file));
            key = new String(encoded);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(Encryptor.this,
                    "Could not load key",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
        return key;
    }

    private String getExtension(String file) {
        String extension = "";
        int i = file.lastIndexOf('.');
        if (i > 0) {
            extension = file.substring(i + 1);
        }
        return extension;
    }

    private void crypter(String filename, String type, String crypt, String key) {

        File inputFile = new File(filename);
        String msg = "Error";
        String title = "Done";
        boolean done = true;

        try {
            switch (crypt) {
                case "Encrypt":
                    msg = "Yr file was encrypted successfully";
                    CryptoUtils.encrypt(key, inputFile, new File("encrypted." + type));
                    outputKey(key);
                    break;
                case "Decrypt":
                    msg = "Yr file was decrypted successfully";
                    CryptoUtils.decrypt(key, inputFile, new File("decrypted." + type));
                    break;
                default:
                    msg = "Error";
                    title = "Error";
            }
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

    private String generateKey() {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // for example
            secretKey = keyGen.generateKey();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(Encryptor.this,
                    "Key generating error!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private void outputKey(String keyText) {

        File output = new File("key");

        try (FileWriter writer = new FileWriter(output)) {
            writer.write(keyText);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(Encryptor.this,
                    "Key generating error!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Encryptor.this,
                    "Yr key was not saved",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
