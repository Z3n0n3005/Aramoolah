package com.example.aramoolah.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aramoolah.R;
import com.example.aramoolah.databinding.FragmentBookkeepingAddTransactionBinding;

public class BookkeepingAddTransactionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentBookkeepingAddTransactionBinding binding;

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
        // Wallet Spinner
        Spinner walletEt = binding.walletEt;
        ArrayAdapter<CharSequence> walletNamesAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.wallet, android.R.layout.simple_spinner_item);
        walletNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletEt.setAdapter(walletNamesAdapter);
        walletEt.setOnItemSelectedListener(this);
        walletEt.setPrompt("Select a wallet");

        // Submit button
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
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
        String walletName = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void addTransaction(){

    }
}