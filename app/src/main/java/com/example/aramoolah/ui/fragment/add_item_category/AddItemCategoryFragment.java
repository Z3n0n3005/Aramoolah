package com.example.aramoolah.ui.fragment.add_item_category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.databinding.FragmentAddItemCategoryBinding;
import com.example.aramoolah.viewmodel.AddItemCategoryViewModel;

public class AddItemCategoryFragment extends Fragment {
    FragmentAddItemCategoryBinding binding;
    AddItemCategoryViewModel mAddItemCategoryViewModel;

    EditText addItemCategory_et;
    Button addItemCategory_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddItemCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddItemCategoryViewModel = new ViewModelProvider(this).get(AddItemCategoryViewModel.class);

        addItemCategory_et = binding.addItemCategoryEt;
        addItemCategory_btn = binding.addItemCategorySubmitBtn;

        Session session = new Session(requireContext());
        int userId = session.getUserId();

        addItemCategory_btn.setOnClickListener(view1 -> {
            try {
                addItemCategory(userId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //TODO: Implement addItemCategory
    private void addItemCategory(int userId) throws InterruptedException {
        mAddItemCategoryViewModel.addItemCategory(userId, addItemCategory_et.getText().toString());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_add_item_category_fragment_to_nav_list_item_category_fragment);
    }
}