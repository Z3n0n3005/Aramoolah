package com.example.aramoolah.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.LayoutBookkeepingHistoryRowBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BookkeepingHistoryRowAdapter extends RecyclerView.Adapter<BookkeepingHistoryRowAdapter.MyViewHolder> {
//    private LayoutBookkeepingHistoryRowBinding rowBinding;
    private List<Transaction> transactionList;
    private List<Item> itemList;
    private List<Wallet> walletList;

    public BookkeepingHistoryRowAdapter(){
        transactionList = new ArrayList<>();
        itemList = new ArrayList<>();
        walletList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BookkeepingHistoryRowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO: Create BookkeepingHistoryRowAdapter
        //TODO: Color coding TransactionType -> (+): Green, (-): Red
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        rowBinding = LayoutBookkeepingHistoryRowBinding.inflate(inflater, parent, false);


        View view = inflater.inflate(R.layout.layout_bookkeeping_history_row, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BookkeepingHistoryRowAdapter.MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        if(transaction.transactionType.equals(TransactionType.EXPENSE)){
            holder.transactionType_txt.setText("-");
        } else {
            holder.transactionType_txt.setText("+");
        }
        //TODO: (Low) String.format
        holder.money_txt.setText(transaction.amountOfMoney.getNumberStripped().toBigInteger().toString());
        for(Item item: itemList){
            if(transaction.itemId.equals(item.itemId)){
                holder.itemCategory_txt.setText(item.itemCategory.toString());
                break;
            }
        }

        for(Wallet wallet: walletList){
            if(transaction.walletId.equals(wallet.walletId)){
                holder.wallet_txt.setText(wallet.walletName);
                break;
            }
        }

        DateTimeFormatter transactionDayFormat = DateTimeFormatter.ofPattern("dd MM yyyy");
        LocalDateTime time = transaction.localDateTime;
        holder.time_txt.setText(transactionDayFormat.format(time));

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void updateTransactionList(List<Transaction> transactionList){
        this.transactionList.clear();
        this.transactionList = transactionList;
        //TODO: (Normal) notifyDataSetChanged()
        notifyDataSetChanged();
    }

    public void updateItemList(List<Item> itemList){
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void updateWalletList(List<Wallet> walletList){
        this.walletList.clear();
        this.walletList = walletList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView transactionType_txt, money_txt, itemCategory_txt, wallet_txt, time_txt;

        public MyViewHolder(@NonNull View view){
            super(view);
            this.transactionType_txt = view.findViewById(R.id.bookkeeping_transaction_type_txt);
            this.money_txt = view.findViewById(R.id.bookkeeping_money_txt);
            this.itemCategory_txt = view.findViewById(R.id.bookkeeping_category_txt);
            this.wallet_txt = view.findViewById(R.id.bookkeeping_wallet_txt);
            this.time_txt = view.findViewById(R.id.bookkeeping_time_txt);
        }
    }
}
