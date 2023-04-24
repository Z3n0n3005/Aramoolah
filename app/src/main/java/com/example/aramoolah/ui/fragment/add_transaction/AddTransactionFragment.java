package com.example.aramoolah.ui.fragment.add_transaction;

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
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.FragmentAddTransactionBinding;
import com.example.aramoolah.viewmodel.AddTransactionViewModel;

import org.javamoney.moneta.Money;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AddTransactionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //TODO: Transfer money from 1 account to another.
    //TODO: Move submit button to navBar
    private FragmentAddTransactionBinding binding;
    private AddTransactionViewModel mAddTransactionViewModel;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // PersonalFinanceViewModel
        mAddTransactionViewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);

        // Set current user
        Session session = new Session(requireContext());
        int userId = session.getUserId();

        try {
            mAddTransactionViewModel.setCurrentUser(userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            //Initialize
            List<Item> itemList = mAddTransactionViewModel.getCurrentUserItemList().getValue();
            List<ItemCategory> categoryList = mAddTransactionViewModel.getCurrentUserItemCategoryList().getValue();
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
        binding.addTransactionSubmitBtn.setOnClickListener(view1 -> {
            try {
                addTransaction();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
        Spinner walletSp = binding.addTransactionWalletSp;
        List<Wallet> walletList;
        try {
            walletList = mAddTransactionViewModel.getCurrentUserWalletList().getValue();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        walletSp.setAdapter(getAdapter(walletList));

    }

    private void setUpTransactionTypeSpinner(){
        Spinner transactionTypeSp = binding.addTransactionTypeSp;
        ArrayAdapter<CharSequence> transactionTypeAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.transaction_type,
                android.R.layout.simple_spinner_item
        );
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeSp.setAdapter(transactionTypeAdapter);
    }

    private void setUpItemCategorySpinner(List<ItemCategory> categoryList) throws InterruptedException {
        Spinner itemNameSp = binding.addTransactionItemNameSp;
        Spinner categorySp = binding.addTransactionCategorySp;
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
                        if (!Objects.equals(itemCategory.itemCategoryId, item.itemCategoryId)) {
                            try {
                                filteredItemList = mAddTransactionViewModel.getItemList(itemCategory.itemCategoryId);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            itemNameSp.setAdapter(getAdapter(filteredItemList));
                        }
                    }
                    if(item == null){
                        try {
                            filteredItemList = mAddTransactionViewModel.getItemList(itemCategory.itemCategoryId);
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
        Spinner itemNameSp = binding.addTransactionItemNameSp;
        Spinner categorySp = binding.addTransactionCategorySp;
        itemNameSp.setAdapter(getAdapter(itemList));

        itemNameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);
                ItemCategory itemCategory = (ItemCategory) categorySp.getSelectedItem();
                List<ItemCategory> filteredCategory = new ArrayList<>();
                // Auto select
                if(itemCategory == null && item != null){
                    ItemCategory selectedItemCategory = null;
                    try {
                        selectedItemCategory = mAddTransactionViewModel.getItemCategory(item.itemCategoryId);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
            R.layout.layout_spinner_row_nothing_selected,
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

        if(inputCheck(costInt,numberOfItem,walletId,itemId,transactionType)){
            Transaction transaction = new Transaction(walletId,itemId, transactionType, cost, numberOfItem, LocalDateTime.now());

            mAddTransactionViewModel.addTransaction(transaction, costInt);
            Toast.makeText(requireContext(), "Successfully added transaction", Toast.LENGTH_SHORT).show();
            //Navigate to bookeeping history fragment afterward
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_add_transaction_fragment_to_nav_history_fragment);
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }

    }
    private Money createCost(){
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        BigInteger amountOfMoneyInt = createCostInt();
        return Money.of(amountOfMoneyInt, currencyUnit);
    }

    private BigInteger createCostInt(){
        return BigInteger.valueOf(Integer.parseInt(binding.addTransactionCostEt.getText().toString()));
    }

    private Integer createNumberOfItem(){
        return Integer.parseInt(binding.addTransactionNumItemEt.getText().toString());
    }

    private Integer createWalletId() throws InterruptedException {
        return mAddTransactionViewModel.getWalletId(binding.addTransactionWalletSp.getSelectedItem().toString());
    }

    private Integer createItemId() throws InterruptedException {
        Integer itemId = mAddTransactionViewModel.getItemId(binding.addTransactionItemNameSp.getSelectedItem().toString());
        return itemId;
    }

    private TransactionType createTransactionType(){
        String transactionTypeStr = binding.addTransactionTypeSp.getSelectedItem().toString();

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

}