package com.example.aramoolah.ui.fragment.listitemcategory;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.ItemCategory;

public class ListItemCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_CATEGORY = 0;
    //TODO: Turn ItemCategory from enum to db
    private final ItemCategory[] itemCategoryList;

    public ListItemCategoryAdapter(){
        itemCategoryList = ItemCategory.values();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == ITEM_CATEGORY){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_info, parent, false);
            return new ItemCategoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemCategoryViewHolder){
            ItemCategory itemCategory = itemCategoryList[position];
            ((ItemCategoryViewHolder) holder).bindItemCategoryViewHolder(itemCategory);
        }
    }

    @Override
    public int getItemCount() {
        return itemCategoryList.length;
    }

    public static class ItemCategoryViewHolder extends RecyclerView.ViewHolder{
        Button itemCategory_btn;
        public ItemCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCategory_btn = itemView.findViewById(R.id.dashboard_info_btn);
        }

        public void bindItemCategoryViewHolder(ItemCategory currentItemCategory){
            itemCategory_btn.setText(currentItemCategory.toString());
            itemCategory_btn.setBackgroundColor(Color.GRAY);
        }
    }
}
