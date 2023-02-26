package com.ex;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAService {

    public String generateKeys(String path) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        KeyDTO keyDTO=new KeyDTO(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()), Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(path+"/key.json"), keyDTO);
        return keyDTO.getPublicKey();
    }

    public void decode(File file, String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream fileInputStream=new FileInputStream(file);
        byte[] bytes=fileInputStream.readAllBytes();
        fileInputStream.close();
        ObjectMapper objectMapper = new ObjectMapper();
        KeyDTO key=objectMapper.readValue(new File(path+"/key.json"), KeyDTO.class);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key.getPrivateKey()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decryptedFileBytes = decryptCipher.doFinal(bytes);
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        fileOutputStream.write(decryptedFileBytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void encode(File file, String pubKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        FileInputStream fileInputStream=new FileInputStream(file);
        byte[] bytes=fileInputStream.readAllBytes();
        fileInputStream.close();
        byte[] encodedPb = Base64.getDecoder().decode(pubKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
        PublicKey publicKey = kf.generatePublic(keySpecPb);
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] b=encryptCipher.doFinal(bytes);
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        fileOutputStream.write(b);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
