package com.example.aramoolah.util.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hash {
    private static Hash mHash = null;

    private Hash(){}

    public static Hash getInstance(){
        if(Hash.mHash == null){
            mHash = new Hash();
        }
        return mHash;
    }

    public String PBKDFHash(String password, byte[] saltProvided) throws NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {
        class Foo implements Runnable {
            byte[] hash;
            List<Byte> result;
            String resultStr;
            @Override
            public void run() {
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16];
                if(saltProvided.length != 0) {
                    salt = saltProvided;
                } else{
                    random.nextBytes(salt);
                }

                KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
                SecretKeyFactory factory = null;
                try {
                    factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                try {
                    hash = factory.generateSecret(spec).getEncoded();
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }

                String saltStr = "";
                for (byte s : salt) {
                    String st = String.format("%02X", s);
                    saltStr += st;
                }
                String hashStr = "";
                for(byte h: hash){
                    String st = String.format("%02X", h);
                    hashStr += st;
                }
                resultStr = hashStr + saltStr;
            }

            public String getResult(){
                return resultStr;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public String PBKDFHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {
        byte[] empty = new byte[16];
        return PBKDFHash(password, empty);
    }

    public byte[] getSaltFromPassword(String saltStrRetrieved) throws InterruptedException {
        class Foo implements Runnable{
            byte[] result;
            @Override
            public void run() {
                result = new byte[saltStrRetrieved.length() / 2];
                for (int i = 0; i < result.length; i++) {
                    int index = i * 2;
                    int j = Integer.parseInt(saltStrRetrieved.substring(index, index + 2), 16);
                    result[i] = (byte) j;
                }
            }
            public byte[] getResult(){return result;}
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }
}
