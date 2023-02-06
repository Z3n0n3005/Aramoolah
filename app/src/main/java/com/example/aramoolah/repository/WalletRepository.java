package com.example.aramoolah.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.WalletDao;
import com.example.aramoolah.data.WalletDatabase;
import com.example.aramoolah.model.Wallet;

import java.util.List;

public class WalletRepository {
    private WalletDao walletDao;

    public WalletRepository(WalletDao walletDao){
        this.walletDao = walletDao;
    }

    public void addWallet(Wallet wallet){walletDao.addWallet(wallet);}
    public void updateWallet(Wallet wallet){walletDao.updateWallet(wallet);}
    public void deleteWallet(Wallet wallet){walletDao.deleteWallet(wallet);}
    public LiveData<List<Wallet>> getAllWallet(){return walletDao.getAllWallet();}
}
