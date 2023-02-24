package com.example.aramoolah.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.databinding.FragmentBookkeepingAddTransactionBinding;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class BookkeepingAddTransactionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentBookkeepingAddTransactionBinding binding;
    private PersonalFinanceViewModel mPersonalFinanceViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentBookkeepingAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // PersonalFinanceViewModel
        mPersonalFinanceViewModel = new ViewModelProvider(this).get(PersonalFinanceViewModel.class);

        // Run Once
        try {
            runOnce();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Wallet Spinner walletSp
        Spinner walletSp = binding.walletSp;
        List<Wallet> walletList;
        try {
            walletList = mPersonalFinanceViewModel.getCurrentUserWallet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ArrayAdapter<Wallet> walletNamesAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                walletList
        );
        walletNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletSp.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        walletNamesAdapter,
                        R.layout.spinner_row_nothing_selected_layout,
                        getActivity()
                )
        );
        walletSp.setOnItemSelectedListener(this);
        walletSp.setPrompt("Select a wallet");

        // Transaction type transactionType_sp
        Spinner transactionTypeSp = binding.transactionTypeSp;
        ArrayAdapter<CharSequence> transactionTypeAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.transactionType,
                android.R.layout.simple_spinner_item
        );
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeSp.setAdapter(transactionTypeAdapter);

        //TODO: Choosing ItemCategory cut down item option

        // Item category spinner ItemCategory_sp

        //TODO: Find a view that can type and quick search


        //TODO: Auto choose category depending on Item


        // Submit button
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addTransaction();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void addTransaction() throws InterruptedException {
        //amountOfMoney
        Integer amountOfMoneyInt = createAmountOfMoneyInt();
        Money amountOfMoney = createAmountOfMoney();

        //numberOfItem
        Integer numberOfItem = createNumberOfItem();

        //walletId
        Integer walletId = createWalletId();

        //TODO: change to query itemId auto;
        //itemId
        Integer itemId = createItemId();

        //transactionType
        TransactionType transactionType = createTransactionType();


        if(inputCheck(amountOfMoneyInt,numberOfItem,walletId,itemId,transactionType)){
            Transaction transaction = new Transaction(walletId,itemId, transactionType, amountOfMoney, numberOfItem, LocalDateTime.now());
            mPersonalFinanceViewModel.addTransaction(transaction);
            Toast.makeText(requireContext(), "Successfully added transaction", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_BookkeepingAddTransactionFragment_to_BookkeepingHistoryFragment);
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }

    }

    public Money createAmountOfMoney(){
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        Integer amountOfMoneyInt = createAmountOfMoneyInt();
        Money amountOfMoney = Money.of(amountOfMoneyInt, currencyUnit);
        return amountOfMoney;
    }

    public Integer createAmountOfMoneyInt(){
        return Integer.parseInt(binding.amountOfMoneyEt.getText().toString());
    }

    public Integer createNumberOfItem(){
        return Integer.parseInt(binding.numberOfItemsEt.getText().toString());
    }

    public Integer createWalletId() throws InterruptedException {
        return mPersonalFinanceViewModel.getWalletId(binding.walletSp.getSelectedItem().toString());
    }

    public Integer createItemId() throws InterruptedException {
        Integer itemId = mPersonalFinanceViewModel.getItemId(binding.itemNameEt.getText().toString());
        return itemId;
    }

    public TransactionType createTransactionType(){
        String transactionTypeStr = binding.transactionTypeSp.getSelectedItem().toString();
        TransactionType transactionType;

        if(transactionTypeStr.equals("+")){
            return TransactionType.INCOME;
        }
        return TransactionType.EXPENSE;
    }

    public boolean inputCheck(Integer amountOfMoneyInt, Integer numberOfItem, Integer walletId, Integer itemId, TransactionType transactionType){
        return (amountOfMoneyInt != null && amountOfMoneyInt >= 0)
                && (numberOfItem != null && numberOfItem > 0)
                && (walletId != null)
                && (itemId != null)
                && (transactionType != null);
    }

//    public void addUser(){
//        User user = new User("John","John","John","John@gmail.com","12345");
//        mPersonalFinanceViewModel.addUser(user);
//    }
//
//    public void addWallet() throws InterruptedException {
//        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
//        Money money = Money.of(200000000, currencyUnit);
//
//        Wallet wallet = new Wallet(mPersonalFinanceViewModel.getUserId("John@gmail.com"), "Cash", money);
//        mPersonalFinanceViewModel.addWallet(wallet);
//    }
//
//    public void addItem() throws InterruptedException {
//        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
//        Money redbull = Money.of(15000, currencyUnit);
//        Integer userId = mPersonalFinanceViewModel.getUserId("John@gmail.com");
//
//        Item item = new Item(userId, "redbull", redbull, ItemCategory.DRINK);
//        mPersonalFinanceViewModel.addItem(item);
//
//        Money cake = Money.of(20000, currencyUnit);
//
//        Item item2 = new Item(userId, "cake", cake, ItemCategory.FOOD);
//        mPersonalFinanceViewModel.addItem(item2);
//    }
//    public void addTempTransaction(){
//        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
//        Money expense = Money.of(15000, currencyUnit);
//
//        Transaction transaction = new Transaction(1, 1, TransactionType.EXPENSE, expense, 1, LocalDateTime.now());
//        mPersonalFinanceViewModel.addTransaction(transaction);
//    }
//    public void logTemp() throws InterruptedException {
//        mPersonalFinanceViewModel.setCurrentUser("John@gmail.com");
//        User current = mPersonalFinanceViewModel.currentUser;
//        Map<Integer, List<Integer>> userAndWalletList = mPersonalFinanceViewModel.getAllWalletOfCurrentUser();
//        boolean isCurrentInMap = userAndWalletList.containsKey(current.userId);
//        List<Integer> walletList = userAndWalletList.get(current.userId);
//        for(Integer walletId : walletList){
//            Log.d("ROOM RELATION", "logTemp: " + walletId);
//        }
//
//    }
    public void runOnce() throws InterruptedException {
        mPersonalFinanceViewModel.setCurrentUser("John@gmail.com");
    }
}