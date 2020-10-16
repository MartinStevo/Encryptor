package com.stuba.fei.uim.upb.encryptor;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, String key, File inputFile,
                                 File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            byte[] inputBytes = new byte[16384];
            try (InputStream inputStream = new FileInputStream(inputFile)) {
                try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                    while (inputStream.read(inputBytes) != -1) {
                        if(inputFile.length() < 16384){
                            outputStream.write(cipher.doFinal(inputBytes));
                        } else {
                            outputStream.write(cipher.update(inputBytes));
                        }
                    }
                    outputStream.close();
                }
                inputStream.close();
            }

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException
                                        | IllegalArgumentException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}

