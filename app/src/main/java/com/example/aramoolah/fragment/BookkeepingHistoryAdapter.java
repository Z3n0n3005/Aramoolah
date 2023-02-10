package com.example.aramoolah.fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookkeepingHistoryAdapter extends RecyclerView.Adapter<BookkeepingHistoryAdapter.MyViewHolder> {
    @NonNull
    @Override
    public BookkeepingHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BookkeepingHistoryAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View view){super(view);}
    }
}
