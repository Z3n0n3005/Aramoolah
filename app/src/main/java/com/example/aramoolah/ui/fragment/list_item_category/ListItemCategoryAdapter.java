package com.example.aramoolah.ui.fragment.list_item_category;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.ItemCategory;

import java.util.ArrayList;
import java.util.List;

public class ListItemCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_CATEGORY = 0;
    private static final int ADD_CATEGORY = 1;
    //TODO: Turn ItemCategory from enum to db
    private List<ItemCategory> itemCategoryList;

    public ListItemCategoryAdapter(){
        itemCategoryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == ITEM_CATEGORY){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_info, parent, false);
            return new ItemCategoryViewHolder(view);
        }
        if(viewType == ADD_CATEGORY){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_add, parent, false);
            return new AddCategoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemCategoryViewHolder){
            ItemCategory itemCategory = itemCategoryList.get(position);
            ((ItemCategoryViewHolder) holder).bindItemCategoryViewHolder(itemCategory);
        }
        if(holder instanceof AddCategoryViewHolder){
            ((AddCategoryViewHolder) holder).bindAddCategoryViewHolder();
        }
    }

    @Override
    public int getItemCount() {
        return itemCategoryList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < itemCategoryList.size()){
            return ITEM_CATEGORY;
        } else if (position == itemCategoryList.size()){
            return ADD_CATEGORY;
        }
        return super.getItemViewType(position);
    }

    public void updateItemCategoryList(List<ItemCategory> itemCategoryList){
        this.itemCategoryList.clear();
        this.itemCategoryList = itemCategoryList;
        notifyDataSetChanged();
    }

    public static class ItemCategoryViewHolder extends RecyclerView.ViewHolder{
        Button itemCategory_btn;
        Context context;
        public ItemCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemCategory_btn = itemView.findViewById(R.id.dashboard_info_btn);
        }

        public void bindItemCategoryViewHolder(ItemCategory currentItemCategory){
            itemCategory_btn.setText(currentItemCategory.toString());
            itemCategory_btn.setBackgroundColor(Color.WHITE);
            itemCategory_btn.setOnClickListener(view -> {
                SharedPreferences itemCategory = context.getSharedPreferences("itemCategory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = itemCategory.edit();
                editor.putInt("itemCategory", currentItemCategory.itemCategoryId);
                editor.apply();
                Navigation.findNavController(view).navigate(R.id.action_nav_list_item_category_fragment_to_nav_list_item_fragment);
            });
        }
    }

    public static class AddCategoryViewHolder extends RecyclerView.ViewHolder{
        Button addCategory_btn;

        public AddCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            addCategory_btn = itemView.findViewById(R.id.dashboard_add_btn);
        }

        public void bindAddCategoryViewHolder(){
            addCategory_btn.setText("Add item category");
            addCategory_btn.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_list_item_category_fragment_to_nav_add_item_category_fragment));
        }
    }
}
