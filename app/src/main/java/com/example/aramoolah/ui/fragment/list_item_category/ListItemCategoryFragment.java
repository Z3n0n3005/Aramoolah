package com.example.aramoolah.ui.fragment.list_item_category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.databinding.FragmentListItemCategoryBinding;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import java.util.List;

public class ListItemCategoryFragment extends Fragment {
    FragmentListItemCategoryBinding binding;
    ListItemCategoryAdapter listItemCategoryAdapter;
    PersonalFinanceViewModel mPersonalFinanceViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListItemCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPersonalFinanceViewModel = new ViewModelProvider(this).get(PersonalFinanceViewModel.class);

        RecyclerView listItemCategoryRecycler = binding.listItemRecycler;
        listItemCategoryAdapter = new ListItemCategoryAdapter();
        listItemCategoryRecycler.setAdapter(listItemCategoryAdapter);
        listItemCategoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Session session = new Session(requireContext());
        int userId = session.getUserId();

        try {
            mPersonalFinanceViewModel.setCurrentUser(userId);
            mPersonalFinanceViewModel.getCurrentUserItemCategoryList().observe(getViewLifecycleOwner(), itemCategoryListObserver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    final Observer<List<ItemCategory>> itemCategoryListObserver = new Observer<List<ItemCategory>>() {
        @Override
        public void onChanged(List<ItemCategory> itemCategoryList) {
            listItemCategoryAdapter.updateItemCategoryList(itemCategoryList);
        }
    };
}