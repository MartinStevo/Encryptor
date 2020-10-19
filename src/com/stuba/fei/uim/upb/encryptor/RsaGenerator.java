package com.stuba.fei.uim.upb.encryptor;

import javax.crypto.Cipher;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaGenerator {
    public RsaGenerator() {
    }

    protected void generateRsa() {
        String key = "";
        try {
            KeyPairGenerator rsaKeyGen = KeyPairGenerator.getInstance("RSA");
            rsaKeyGen.initialize(4096);
            KeyPair rsaKeyPair = rsaKeyGen.generateKeyPair();
            output(Base64.getEncoder().encodeToString(rsaKeyPair.getPrivate().getEncoded()), "privateRSA");
            output(Base64.getEncoder().encodeToString(rsaKeyPair.getPublic().getEncoded()), "publicRSA");

        } catch (Exception e) {
            System.out.println("Exception while encryption/decryption");
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(new JFrame(),
                "Yr RSA key pair was generated successfully",
                "RSA generating", JOptionPane.INFORMATION_MESSAGE);
    }

    private void output(String output, String file) {
        File f = new File(file);

        try {
            FileWriter writer = new FileWriter(f);
            writer.write(output);
            writer.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Key generating error!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Yr key was not saved",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public String decrypt(byte[] key, String filepath) {

        byte[] outputBytes = new byte[0];

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] keyBytes = Files.readAllBytes(Paths.get(filepath));
            PKCS8EncodedKeySpec pKCS8EncodedKeySpec  = new PKCS8EncodedKeySpec (Base64.getDecoder().decode(keyBytes));
            PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            outputBytes = cipher.doFinal(key);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return Base64.getEncoder().encodeToString(outputBytes);
    }

    public byte[] encrypt(String key, String filepath) {

        byte[] outputBytes = new byte[0];

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] keyBytes = Files.readAllBytes(Paths.get(filepath));
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyBytes));
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            outputBytes = cipher.doFinal(Base64.getDecoder().decode(key));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return outputBytes;
    }
}
