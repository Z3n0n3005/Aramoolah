package com.example.aramoolah.ui.fragment.list_item;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int LIST_ITEM = 0;
    private static final int ADD_ITEM = 1;
    private List<Item> itemList;

    public ListItemAdapter(){
        this.itemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == LIST_ITEM){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_info, parent, false);
            return new ListItemViewHolder(view);
        }
        if(viewType == ADD_ITEM){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_add, parent, false);
            return new AddItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListItemViewHolder){
            Item item = itemList.get(position);
            ((ListItemViewHolder) holder).bindListItemViewHolder(item);
        }
        if(holder instanceof AddItemViewHolder){
            ((AddItemViewHolder) holder).bindAddItemViewHolder();
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < itemList.size()){
            return LIST_ITEM;
        } else if (position == itemList.size()){
            return ADD_ITEM;
        }
        return super.getItemViewType(position);
    }

    public void updateItemList(List<Item> itemList){
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder{
        Button item_btn;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_btn = itemView.findViewById(R.id.dashboard_info_btn);
        }

        public void bindListItemViewHolder(Item item){
            item_btn.setText(item.itemName);
            item_btn.setBackgroundColor(Color.WHITE);
        }
    }

    public static class AddItemViewHolder extends RecyclerView.ViewHolder{
        Button addItem_btn;

        public AddItemViewHolder(@NonNull View itemView) {
            super(itemView);
            addItem_btn = itemView.findViewById(R.id.dashboard_add_btn);
        }

        public void bindAddItemViewHolder(){
            addItem_btn.setText("Add item");
            addItem_btn.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_list_item_fragment_to_nav_add_item_fragment));
        }
    }
}
