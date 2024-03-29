package com.example.aramoolah.data.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.model.Wallet;

import org.javamoney.moneta.Money;

import java.util.List;
import java.util.Map;

public class WalletRepository {
    private WalletDao walletDao;

    public WalletRepository(WalletDao walletDao){
        this.walletDao = walletDao;
    }

    public void addWallet(Wallet wallet){walletDao.addWallet(wallet);}
    public void updateWallet(Wallet wallet){walletDao.updateWallet(wallet);}
    public void updateTotalAmount(Integer walletId, Money totalAmount){walletDao.updateTotalAmount(walletId, totalAmount);}
    public void deleteWallet(Wallet wallet){walletDao.deleteWallet(wallet);}
    public LiveData<List<Wallet>> getAllWallet(){return walletDao.getAllWallet();}
    public int getWalletId(String walletName){return walletDao.getWalletId(walletName);}
    public Wallet getWallet(Integer walletId){return walletDao.getWallet(walletId);}
    public Map<Integer, List<Integer>> getWalletTransactionList(Integer walletId){return walletDao.getWalletTransactionList();}
}
