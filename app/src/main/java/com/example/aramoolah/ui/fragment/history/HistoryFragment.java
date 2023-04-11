package com.example.aramoolah.ui.fragment.history;

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
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.FragmentHistoryBinding;
import com.example.aramoolah.viewmodel.HistoryViewModel;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryViewModel mHistoryViewModel;
    private RecyclerView history_recycler;
    private HistoryAdapter rowAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHistoryViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        rowAdapter = new HistoryAdapter();
        history_recycler = binding.historyRecycler;
        history_recycler.setAdapter(rowAdapter);
        history_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            mHistoryViewModel.getCurrentUserTransactionList().observe(getViewLifecycleOwner(), transactionListObserver);
            mHistoryViewModel.getCurrentUserItemList().observe(getViewLifecycleOwner(), itemListObserver);
            mHistoryViewModel.getCurrentUserWalletList().observe(getViewLifecycleOwner(), walletListObserver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Add Transaction button navigation
        binding.addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_HistoryFragment_to_AddTransactionFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    final Observer<List<Transaction>> transactionListObserver = new Observer<List<Transaction>>() {
        @Override
        public void onChanged(List<Transaction> transactions) {
            rowAdapter.updateTransactionList(transactions);
        }
    };

    final Observer<List<Item>> itemListObserver = new Observer<List<Item>>() {
        @Override
        public void onChanged(List<Item> items) {
            rowAdapter.updateItemList(items);
        }
    };

    final Observer<List<Wallet>> walletListObserver = new Observer<List<Wallet>>() {
        @Override
        public void onChanged(List<Wallet> wallets) {
            rowAdapter.updateWalletList(wallets);
        }
    };

}