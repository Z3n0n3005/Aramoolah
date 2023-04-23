package com.example.aramoolah.ui.fragment.list_item;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Session;
import com.example.aramoolah.databinding.FragmentListItemBinding;
import com.example.aramoolah.viewmodel.ListItemViewModel;

import java.util.List;

public class ListItemFragment extends Fragment {
    FragmentListItemBinding binding;
    ListItemViewModel mListItemViewModel;

    ListItemAdapter listItemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListItemViewModel = new ViewModelProvider(this).get(ListItemViewModel.class);

        Session session = new Session(requireContext());
        int userId = session.getUserId();

        try {
            mListItemViewModel.setCurrentUser(userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        RecyclerView listItemRecycler = binding.recyclerListItem;
        listItemAdapter = new ListItemAdapter();
        listItemRecycler.setAdapter(listItemAdapter);
        listItemRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get Item category from shared preference
        SharedPreferences itemCategoryPref = getActivity().getSharedPreferences("itemCategory", Context.MODE_PRIVATE);
        ItemCategory itemCategory = ItemCategory.valueOf(itemCategoryPref.getString("itemCategory", "Other"));

        try {
            mListItemViewModel.getItemList(itemCategory).observe(getViewLifecycleOwner(), itemListObserver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    final Observer<List<Item>> itemListObserver = new Observer<List<Item>>() {
        @Override
        public void onChanged(List<Item> itemList) {
            listItemAdapter.updateItemList(itemList);
        }
    };
}