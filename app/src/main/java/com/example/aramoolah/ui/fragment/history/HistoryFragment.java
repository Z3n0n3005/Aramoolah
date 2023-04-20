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
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.FragmentHistoryBinding;
import com.example.aramoolah.viewmodel.HistoryViewModel;

import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryViewModel mHistoryViewModel;
    private RecyclerView history_recycler;
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

        mHistoryViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        Session session = new Session(requireContext());
        int userId = session.getUserId();

        try {
            mHistoryViewModel.setCurrentUser(userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        historyAdapter = new HistoryAdapter();
        history_recycler = binding.historyRecycler;
        history_recycler.setAdapter(historyAdapter);
        history_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            mHistoryViewModel.getCurrentUserTransactionList().observe(getViewLifecycleOwner(), transactionListObserver);
            mHistoryViewModel.getCurrentUserItemList().observe(getViewLifecycleOwner(), itemListObserver);
            mHistoryViewModel.getCurrentUserWalletList().observe(getViewLifecycleOwner(), walletListObserver);
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

}