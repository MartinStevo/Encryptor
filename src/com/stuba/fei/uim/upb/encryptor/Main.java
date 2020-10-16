package com.stuba.fei.uim.upb.encryptor;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new Encryptor();
        frame.setSize(400, 300);
        centreWindow(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Encryptor");
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void centreWindow(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
