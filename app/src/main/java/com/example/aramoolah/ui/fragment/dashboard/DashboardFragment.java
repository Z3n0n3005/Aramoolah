package com.example.aramoolah.ui.fragment.dashboard;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.FragmentDashboardBinding;
import com.example.aramoolah.viewmodel.DashboardViewModel;

import java.util.List;

public class DashboardFragment extends Fragment {
    FragmentDashboardBinding binding;
    DashboardViewModel mDashboardViewModel;
    RecyclerView dashboardRecycler;
    DashboardAdapter dashboardAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        //Get current userid from intent -> Set current user
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            int userId = extras.getInt("userId");
            try {
                mDashboardViewModel.setCurrentUser(userId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        dashboardRecycler = binding.dashboardRecycler;
        dashboardAdapter = new DashboardAdapter();
        dashboardRecycler.setAdapter(dashboardAdapter);
        dashboardRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            mDashboardViewModel.getCurrentUserWalletList().observe(getViewLifecycleOwner(), walletListObserver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    final Observer<List<Wallet>> walletListObserver = new Observer<List<Wallet>>() {
        @Override
        public void onChanged(List<Wallet> wallets) {
            dashboardAdapter.updateWalletList(wallets);
        }
    };
}