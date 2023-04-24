package com.example.aramoolah.ui.fragment.add_item;

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
import android.widget.Button;
import android.widget.EditText;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.databinding.FragmentAddItemBinding;
import com.example.aramoolah.viewmodel.AddItemViewModel;

public class AddItemFragment extends Fragment {
    FragmentAddItemBinding binding;
    AddItemViewModel mAddItemViewModel;

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
        mAddItemViewModel = new ViewModelProvider(this).get(AddItemViewModel.class);
        addItem_btn = binding.addItemSubmitBtn;
        addItem_et = binding.addItemEt;

        SharedPreferences itemCategoryPref = getActivity().getSharedPreferences("itemCategory", Context.MODE_PRIVATE);
        int itemCategoryId = itemCategoryPref.getInt("itemCategory", -1);

        Session session = new Session(requireContext());
        int userId = session.getUserId();

        addItem_btn.setOnClickListener(view1 -> {
            try {
                addItem(userId, itemCategoryId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addItem(int userId, int itemCategoryId) throws InterruptedException {
        mAddItemViewModel.addItem(userId, itemCategoryId, addItem_et.getText().toString());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_add_item_fragment_to_nav_list_item_fragment);
    }
}