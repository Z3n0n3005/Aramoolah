package com.example.aramoolah.ui.fragment.add_item;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aramoolah.databinding.FragmentAddItemBinding;

public class AddItemFragment extends Fragment {
    FragmentAddItemBinding binding;

    EditText addItem_et;
    Button addItem_btn;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addItem_btn = binding.addItemSubmitBtn;
        addItem_et = binding.addItemEt;

        SharedPreferences itemCategoryPref = getActivity().getSharedPreferences("itemCategory", Context.MODE_PRIVATE);
        int itemCategoryId = itemCategoryPref.getInt("itemCategory", -1);

        addItem_btn.setOnClickListener(view1 -> addItem());
    }

    public void addItem(){

    }
}