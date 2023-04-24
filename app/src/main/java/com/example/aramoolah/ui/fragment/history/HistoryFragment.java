package com.example.aramoolah.ui.fragment.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.FragmentHistoryBinding;
import com.example.aramoolah.viewmodel.HistoryViewModel;

import org.javamoney.moneta.Money;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HistoryViewModel mHistoryViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        Session session = new Session(requireContext());
        int userId = session.getUserId();

        try {
            mHistoryViewModel.setCurrentUser(userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
//        Money money = Money.of(15000, currencyUnit);
//        LocalDateTime dateTime = LocalDateTime.of(2023,5,1,0,0);
//        Transaction temp = new Transaction(1,1, TransactionType.INCOME, money, 1, dateTime);
//        mHistoryViewModel.addTransaction(temp);

        historyAdapter = new HistoryAdapter();
        RecyclerView history_recycler = binding.historyRecycler;
        history_recycler.setAdapter(historyAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        history_recycler.setLayoutManager(linearLayoutManager);
        try {
            mHistoryViewModel.getCurrentUserTransactionList().observe(getViewLifecycleOwner(), transactionListObserver);
            mHistoryViewModel.getCurrentUserItemList().observe(getViewLifecycleOwner(), itemListObserver);
            mHistoryViewModel.getMapTransactionIdToItemCategoryName().observe(getViewLifecycleOwner(), mapTransactionIdToItemCategoryNameObserver);
            mHistoryViewModel.getCurrentUserWalletList().observe(getViewLifecycleOwner(), walletListObserver);
            mHistoryViewModel.getMapMonthToMoney().observe(getViewLifecycleOwner(), mapMonthToMoneyObserver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Add Transaction button navigation
        binding.addTransactionBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_nav_history_fragment_to_nav_add_transaction_fragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    final Observer<List<Transaction>> transactionListObserver = new Observer<List<Transaction>>() {
        @Override
        public void onChanged(List<Transaction> transactions) {
            historyAdapter.updateTransactionList(transactions);
        }
    };

    final Observer<List<Item>> itemListObserver = new Observer<List<Item>>() {
        @Override
        public void onChanged(List<Item> items) {
            historyAdapter.updateItemList(items);
        }
    };

    final Observer<List<Wallet>> walletListObserver = new Observer<List<Wallet>>() {
        @Override
        public void onChanged(List<Wallet> wallets) {
            historyAdapter.updateWalletList(wallets);
        }
    };

    final Observer<Map<YearMonth, BigInteger>> mapMonthToMoneyObserver = new Observer<Map<YearMonth, BigInteger>>() {
        @Override
        public void onChanged(Map<YearMonth, BigInteger> stringBigIntegerMap) {
            historyAdapter.updateMapMonthToMoney(stringBigIntegerMap);
        }
    };

    final Observer<Map<Long, String>> mapTransactionIdToItemCategoryNameObserver = new Observer<Map<Long, String>>() {
        @Override
        public void onChanged(Map<Long, String> mapTransactionIdToItemCategoryName) {
            historyAdapter.updateMapTransactionIdToItemCategoryName(mapTransactionIdToItemCategoryName);
        }
    };

}