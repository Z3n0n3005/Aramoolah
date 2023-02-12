package com.example.aramoolah.fragment;

import android.os.Bundle;
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
import com.example.aramoolah.databinding.FragmentBookkeepingAddTransactionBinding;
import com.example.aramoolah.model.Item;
import com.example.aramoolah.model.ItemCategory;
import com.example.aramoolah.model.Transaction;
import com.example.aramoolah.model.TransactionType;
import com.example.aramoolah.model.User;
import com.example.aramoolah.model.Wallet;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import org.javamoney.moneta.Money;

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

        // Wallet Spinner
        Spinner walletSp = binding.walletSp;
        ArrayAdapter<CharSequence> walletNamesAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.wallet,
                android.R.layout.simple_spinner_item
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
        Integer amountOfMoney = Integer.parseInt(binding.amountOfMoneyEt.getText().toString());
        Integer numberOfItem = Integer.parseInt(binding.numberOfItemsEt.getText().toString());
        Integer walletId = mPersonalFinanceViewModel.getWalletId(binding.walletSp.getSelectedItem().toString());
        Integer itemId = mPersonalFinanceViewModel.getItemId(binding.itemNameEt.getText().toString());
        TransactionType transactionType;

        try {
            transactionType = TransactionType.valueOf(binding.transactionTypeSp.getSelectedItem().toString());
        } catch (IllegalArgumentException e){
            throw new RuntimeException("Invalid value for enum");
        }


        if(inputCheck(amountOfMoney,numberOfItem,walletId,itemId,transactionType)){
            mPersonalFinanceViewModel.addTransaction(amountOfMoney, numberOfItem, transactionType, walletId, itemId);
            Toast.makeText(requireContext(), "Successfully added transaction", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_BookkeepingAddTransactionFragment_to_BookkeepingHistoryFragment);
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean inputCheck(Integer amountOfMoney, Integer numberOfItem, Integer walletId, Integer itemId, TransactionType transactionType){
        return (amountOfMoney != null && amountOfMoney >= 0)
                && (numberOfItem != null && numberOfItem > 0)
                && (walletId != null)
                && (itemId != null)
                && (transactionType != null);
    }

    public void runOnce() throws InterruptedException {
        User user = new User();
        user.firstName = "John";
        user.middleName = "John";
        user.lastName = "John";
        user.email = "John@gmail.com";
        user.password = "12345";
        mPersonalFinanceViewModel.addUser(user);

        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        Money money = Money.of(200000000, currencyUnit);

        Wallet wallet = new Wallet();
        wallet.walletName = "Cash";
        wallet.totalAmount = money;
        wallet.userId = mPersonalFinanceViewModel.getUserId(user.email);
        mPersonalFinanceViewModel.addWallet(wallet);

        Money redbull = Money.of(15000, currencyUnit);

        Item item = new Item();
        item.itemName = "redbull";
        item.itemCategory = ItemCategory.FOOD;
        item.cost = redbull;
        mPersonalFinanceViewModel.addItem(item);

        Money cake = Money.of(20000, currencyUnit);

        Item item2 = new Item();
        item2.itemCategory = ItemCategory.FOOD;
        item2.itemName = "Cake";
        item2.cost = cake;
        mPersonalFinanceViewModel.addItem(item2);
    }
}