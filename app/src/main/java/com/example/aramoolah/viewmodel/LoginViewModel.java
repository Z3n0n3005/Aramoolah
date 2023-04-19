package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginViewModel extends AndroidViewModel {
    UserRepository userRepository;
    volatile MutableLiveData<List<User>> userList;
    MutableLiveData<User> currentUser;
    public LoginViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
        UserDao userDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).userDao();
        userRepository = new UserRepository(userDao);
        userList = getUserList();
        currentUser = new MutableLiveData<>();
    }

    public MutableLiveData<List<User>> getUserList() throws InterruptedException {
        class Foo implements Runnable {
            private volatile MutableLiveData<List<User>> mUserList = new MutableLiveData<>();

            @Override
            public void run() {
                if(userList == null) {
                    mUserList = new MutableLiveData<>(userRepository.getAllUser());
                } else {
                    mUserList = userList;
                }
            }

            public MutableLiveData<List<User>> getResult(){
                return mUserList;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public void updateUserList() throws InterruptedException {
        Thread thread = new Thread(() -> userList.postValue(userRepository.getAllUser()));
        thread.start();
        thread.join();
    }

    public void setCurrentUser(int userId) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                User user = userRepository.getUser(userId);
                currentUser.postValue(user);
            }
        });
        thread.start();
        thread.join();
    }
    public LiveData<User> getCurrentUser(){
        return currentUser;
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

    private byte[] getSaltFromPassword(String saltStrRetrieved) throws InterruptedException {
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

    public boolean isCorrectPassword(Integer userId, String passwordProvided) throws InterruptedException {
        class Foo implements Runnable{
            Boolean result = false;
            @Override
            public void run() {
                String password = currentUser.getValue().password;
                int toInd = password.length();
                int fromInd = toInd - 32;
//                String hashStrRetrieved = password.substring(0, fromInd - 1);
                String saltStrRetrieved = password.substring(fromInd, toInd);
                try {
                    byte[] retrievedSalt = getSaltFromPassword(saltStrRetrieved);
                    String passwordProvidedHash = PBKDFHash(passwordProvided, retrievedSalt);
                    if(password.equals(passwordProvidedHash)){
                        result = true;
                    }
                } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }
            }

            public boolean getResult(){return result;}
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

}
