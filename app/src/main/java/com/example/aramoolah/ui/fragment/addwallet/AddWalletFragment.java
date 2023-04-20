package com.example.aramoolah.ui.fragment.addwallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.databinding.FragmentAddWalletBinding;
import com.example.aramoolah.viewmodel.AddWalletViewModel;

import java.util.Objects;

public class AddWalletFragment extends Fragment {
    FragmentAddWalletBinding binding;

    AddWalletViewModel mAddWalletViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddWalletBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddWalletViewModel = new ViewModelProvider(this).get(AddWalletViewModel.class);

        // Set current user
        Session session = new Session(requireContext());
        int userId = session.getUserId();

        try {
            mAddWalletViewModel.setCurrentUser(userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        binding.addWalletSubmitBtn.setOnClickListener(view1 -> {
            addWallet();
        });
    }

    private void addWallet(){
        EditText walletName_et = binding.addWalletNameEt;
        EditText initialValue_et = binding.addWalletInitialValueEt;

        String walletName = walletName_et.getText().toString();
        String initialValue_str = initialValue_et.getText().toString();
        if(inputCheck(walletName, initialValue_str)){
            Long initialValue = Long.parseLong(initialValue_str);
            mAddWalletViewModel.addWallet(walletName, initialValue);
            Toast.makeText(requireContext(), "Successfully added wallet", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_add_wallet_fragment_to_nav_dashboard_fragment);
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean inputCheck(String walletName, String initialValue_str){
        return !Objects.equals(walletName, "") && !Objects.equals(initialValue_str, "");
    }
}