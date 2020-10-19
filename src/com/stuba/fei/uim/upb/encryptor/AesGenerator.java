package com.stuba.fei.uim.upb.encryptor;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.util.Base64;

public class AesGenerator {

    String key = "";

    public AesGenerator() {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // for example
            secretKey = keyGen.generateKey();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Key generating error!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
        key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public String getKey() {
        return key;
    }
}
