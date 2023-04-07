package com.example.aramoolah.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
import com.example.aramoolah.util.Initialize;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import org.javamoney.moneta.Money;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class BookkeepingAddTransactionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //TODO: Transfer money from 1 account to another.
    //TODO: Move submit button to navBar
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
        try {
            // Run Once
//            initializeDatabase();
            //Initialize
            List<Item> itemList = mPersonalFinanceViewModel.getCurrentUserItemList().getValue();
            List<ItemCategory> categoryList = Arrays.asList(ItemCategory.values());
            // Transaction type transactionType_sp
            setUpItemCategorySpinner(categoryList);
            // Wallet Spinner walletSp
            setUpTransactionTypeSpinner();
            // Item category spinner ItemCategory_sp
            setUpWalletSpinner();
            // Item name spinner ItemName_sp
            setUpItemNameSpinner(itemList, categoryList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //TODO: Find a view that can type and quick search (low priority)

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void setUpWalletSpinner(){
        Spinner walletSp = binding.walletSp;
        List<Wallet> walletList;
        try {
            walletList = mPersonalFinanceViewModel.getCurrentUserWalletList().getValue();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        walletSp.setAdapter(getAdapter(walletList));

        // If want preselected first item
//        walletSp.setAdapter(walletNamesAdapter);
//        walletSp.setOnItemSelectedListener(this);
//        walletSp.setPrompt("Select a wallet");
    }

    private void setUpTransactionTypeSpinner(){
        Spinner transactionTypeSp = binding.transactionTypeSp;
        ArrayAdapter<CharSequence> transactionTypeAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.transaction_type,
                android.R.layout.simple_spinner_item
        );
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeSp.setAdapter(transactionTypeAdapter);
    }

    private void setUpItemCategorySpinner(List<ItemCategory> categoryList) throws InterruptedException {
        Spinner itemNameSp = binding.itemNameSp;
        Spinner categorySp = binding.categorySp;
        categorySp.setAdapter(getAdapter(categoryList));

        // TODO test if auto fill category works

        // This is for setting what happens to the item list when chosen a category
        categorySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ItemCategory itemCategory = (ItemCategory) adapterView.getItemAtPosition(i);
                Item item = (Item) itemNameSp.getSelectedItem();
                List<Item> filteredItemList;
                if(itemCategory != null){
                    if(item != null) {
                        if (!itemCategory.equals(item.itemCategory)) {
                            try {
                                filteredItemList = mPersonalFinanceViewModel.getItemFromItemCategory(itemCategory);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            itemNameSp.setAdapter(getAdapter(filteredItemList));
                        }
                    }
                    if(item == null){
                        try {
                            filteredItemList = mPersonalFinanceViewModel.getItemFromItemCategory(itemCategory);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        itemNameSp.setAdapter(getAdapter(filteredItemList));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpItemNameSpinner(List<Item> itemList, List<ItemCategory> categoryList) throws InterruptedException {
        Spinner itemNameSp = binding.itemNameSp;
        Spinner categorySp = binding.categorySp;
        itemNameSp.setAdapter(getAdapter(itemList));

        itemNameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);
                ItemCategory itemCategory = (ItemCategory) categorySp.getSelectedItem();
                List<ItemCategory> filteredCategory = new ArrayList<>();
                // Auto select
                if(itemCategory == null && item != null){
                    ItemCategory selectedItemCategory = item.itemCategory;
                    categorySp.setSelection(categoryList.indexOf(selectedItemCategory) + 1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private <T> NothingSelectedSpinnerAdapter getAdapter(List<T> list ){
        ArrayAdapter<T> arrayAdapter = new ArrayAdapter<>(
            getActivity(),
            android.R.layout.simple_spinner_item,
            list
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return new NothingSelectedSpinnerAdapter(
            arrayAdapter,
            R.layout.spinner_row_nothing_selected_layout,
            getActivity()
        );
    }

    public void addTransaction() throws InterruptedException {
        //amountOfMoney
        BigInteger costInt = createCostInt();
        Money cost = createCost();

        //numberOfItem
        Integer numberOfItem = createNumberOfItem();

        //walletId
        Integer walletId = createWalletId();

        //itemId
        Integer itemId = createItemId();

        //transactionType
        TransactionType transactionType = createTransactionType();
//        Log.d("AddTransaction", "amountOfMoney: " + amountOfMoneyInt);
//        Log.d("AddTransaction", "numberOfItem: " + numberOfItem);
//        Log.d("AddTransaction", "walletId: " + walletId);
//        Log.d("AddTransaction", "itemId: " + itemId);
//        Log.d("AddTransaction", "transactionType: " + transactionType);

        if(inputCheck(costInt,numberOfItem,walletId,itemId,transactionType)){
            Transaction transaction = new Transaction(walletId,itemId, transactionType, cost, numberOfItem, LocalDateTime.now());

            mPersonalFinanceViewModel.addTransaction(transaction, costInt);
            Toast.makeText(requireContext(), "Successfully added transaction", Toast.LENGTH_SHORT).show();
            //Navigate to bookeeping history fragment afterward
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_BookkeepingAddTransactionFragment_to_BookkeepingHistoryFragment);
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }

    }
    private Money createCost(){
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        BigInteger amountOfMoneyInt = createCostInt();
        Money amountOfMoney = Money.of(amountOfMoneyInt, currencyUnit);
        return amountOfMoney;
    }

    private BigInteger createCostInt(){
        return BigInteger.valueOf(Integer.parseInt(binding.costEt.getText().toString()));
    }

    private Integer createNumberOfItem(){
        return Integer.parseInt(binding.numberOfItemsEt.getText().toString());
    }

    private Integer createWalletId() throws InterruptedException {
        return mPersonalFinanceViewModel.getWalletId(binding.walletSp.getSelectedItem().toString());
    }

    private Integer createItemId() throws InterruptedException {
        Integer itemId = mPersonalFinanceViewModel.getItemId(binding.itemNameSp.getSelectedItem().toString());
        return itemId;
    }

    private TransactionType createTransactionType(){
        String transactionTypeStr = binding.transactionTypeSp.getSelectedItem().toString();
        TransactionType transactionType;

        if(transactionTypeStr.equals("+")){
            return TransactionType.INCOME;
        }
        return TransactionType.EXPENSE;
    }

    private boolean inputCheck(BigInteger amountOfMoneyInt, Integer numberOfItem, Integer walletId, Integer itemId, TransactionType transactionType){
        return (amountOfMoneyInt != null && amountOfMoneyInt.compareTo(BigInteger.valueOf(0)) > 0)
                && (numberOfItem != null && numberOfItem > 0)
                && (walletId != null)
                && (itemId != null)
                && (transactionType != null);
    }
    public void initializeDatabase() throws InterruptedException {
        new Initialize(mPersonalFinanceViewModel, binding);
    }
}