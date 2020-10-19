package com.stuba.fei.uim.upb.encryptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame  {
    private final JButton encrypt = new JButton("Encrypt");
    private final JButton decrypt = new JButton("Decrypt");
    private final JButton createRSA = new JButton("Generate RSA");

    public Menu() {
        Container container = getContentPane();
        JPanel panel = new JPanel();

        encrypt.setPreferredSize(new Dimension(120,30));
        encrypt.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));
        decrypt.setPreferredSize(new Dimension(120,30));
        decrypt.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));
        createRSA.setPreferredSize(new Dimension(180,30));
        createRSA.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));

        panel.add(encrypt);
        panel.add(decrypt);
        container.add(panel, BorderLayout.SOUTH);

        panel = new JPanel();
        panel.add(createRSA);
        container.add(panel, BorderLayout.NORTH);

        encrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jframe = new Encryptor();
                jframe.setSize(400, 300);
                centreWindow(jframe);
                jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jframe.setTitle("Encryptor");
                jframe.setResizable(false);
                jframe.setVisible(true);
                setVisible(false);
                dispose();
            }
        });
        decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jframe = new Decryptor();
                jframe.setSize(400, 300);
                centreWindow(jframe);
                jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jframe.setTitle("Encryptor");
                jframe.setResizable(false);
                jframe.setVisible(true);
                setVisible(false);
            }
        });
        createRSA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RsaGenerator rsaGenerator = new RsaGenerator();
                rsaGenerator.generateRsa();
            }
        });
    }

    public static void centreWindow(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
