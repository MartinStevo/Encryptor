package com.stuba.fei.uim.upb.encryptor;

import java.io.*;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final byte[] IV = new byte[128/8];

    static RsaGenerator rsaGenerator = new RsaGenerator();
    static String key = "";

    public static void encrypt(String keyAes, File inputFile, File outputFile, String rsaPath)
            throws CryptoException {
        key = keyAes;

        try {
            Mac theMac = Mac.getInstance("HmacMD5");
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(IV);

            Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));

            theMac.init(secretKey);
            int size = 544;
            byte[] empty = new byte[size];
            byte[] mac = new byte[0];
            long inputSize = Math.min(16384, inputFile.length());
            byte[] inputBytes = new byte[(int) inputSize];

            try (InputStream inputStream = new FileInputStream(inputFile)) {
                try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                    outputStream.write(empty);
                    while (inputStream.read(inputBytes) != -1) {
                        if (inputFile.length() < 16384) {
                            byte[] buffer = cipher.doFinal(inputBytes);
                            outputStream.write(buffer);
                            theMac.update(buffer);
                        } else {
                            byte[] buffer = cipher.update(inputBytes);
                            outputStream.write(buffer);
                            theMac.update(buffer);
                        }
                    }
                    outputStream.close();
                    mac = theMac.doFinal();
                    byte[] keyToEncrypt = rsaGenerator.encrypt(key, rsaPath);

                    RandomAccessFile accessFile = new RandomAccessFile(outputFile, "rws");
                    accessFile.seek(0);
                    accessFile.write(keyToEncrypt);
                    accessFile.write(IV);
                    accessFile.write(mac);
                    accessFile.close();
                }
                inputStream.close();
            }

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException | IllegalArgumentException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException
                ex) {
            throw new CryptoException("Error encrypting file", ex);
        }


    }

    public static void decrypt(File inputFile, File outputFile, String rsaPath)
            throws CryptoException {

        try {
            Mac theMac = Mac.getInstance("HmacMD5");

            int size = 544;
            byte[] mac = new byte[0];
            long inputSize = Math.min(16384, inputFile.length() - 544);
            byte[] inputBytes = new byte[(int) inputSize];

            try (InputStream inputStream = new FileInputStream(inputFile)) {
                try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] macEnc = new byte[16];
                    byte[] ivEnc = new byte[16];
                    byte[] keyEnc = new byte[512];
                    inputStream.read(keyEnc);
                    inputStream.read(ivEnc);
                    inputStream.read(macEnc);
                    key = rsaGenerator.decrypt(keyEnc, rsaPath);
                    outputKey(macEnc);

                    Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
                    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivEnc));

                    theMac.init(secretKey);

                    while (inputStream.read(inputBytes) != -1) {
                        if (inputFile.length() < 16912) {
                            theMac.update(inputBytes);
                            outputStream.write(cipher.doFinal(inputBytes));
                        } else {
                            theMac.update(inputBytes);
                            outputStream.write(cipher.update(inputBytes));
                        }
                    }
                    mac = theMac.doFinal();
                    outputStream.close();
                    if (!Arrays.equals(macEnc, mac)) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Integrity of file was corrupted",
                                "Mac comparison",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                inputStream.close();
            }

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException | IllegalArgumentException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException
                ex) {
            throw new CryptoException("Error decrypting file", ex);
        }
    }

    protected static void outputKey(byte[] mac) {

        File output = new File("mac");

        try (FileWriter writer = new FileWriter(output)) {
            writer.write(Base64.getEncoder().encodeToString(mac));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "mac!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "mac",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}

