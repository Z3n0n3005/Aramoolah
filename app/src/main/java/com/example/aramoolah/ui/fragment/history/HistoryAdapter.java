package com.example.aramoolah.ui.fragment.history;

import android.graphics.Color;
import android.util.Log;
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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TRANSACTION_VIEW = 0;
    private static final int MONTH_VIEW = 1;

    private List<Transaction> transactionList;
    private List<Item> itemList;
    private List<Wallet> walletList;
    private Map<String, BigInteger> mapMonthToMoney;

    public HistoryAdapter(){
        transactionList = new ArrayList<>();
        itemList = new ArrayList<>();
        walletList = new ArrayList<>();
        mapMonthToMoney = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO: Create HistoryAdapter
        //TODO: Color coding TransactionType -> (+): Green, (-): Red
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == TRANSACTION_VIEW){
            View view = inflater.inflate(R.layout.layout_recycler_history_row, parent, false);
            return new TransactionViewHolder(view);
        }
        if(viewType == MONTH_VIEW){
            View view = inflater.inflate(R.layout.layout_recycler_history_time, parent, false);
            return new MonthViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TransactionViewHolder){
            Transaction transaction = transactionList.get(position);
            ((TransactionViewHolder) holder) .bindTransactionViewHolder(transaction, itemList, walletList);
        }
        if(holder instanceof MonthViewHolder){

        }

    }

    @Override
    public int getItemCount() {

        return transactionList.size();
    }

    @Override
    public int getItemViewType(int position){
        return transactionList.size() + mapMonthToMoney.size();
    }

    public void updateTransactionList(List<Transaction> transactionList){
        this.transactionList.clear();
        this.transactionList = transactionList;
        Collections.reverse(this.transactionList);
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

    public void updateMapMonthToMoney(Map<String, BigInteger> mapMonthToMoney){
        this.mapMonthToMoney.clear();
        this.mapMonthToMoney = mapMonthToMoney;
        notifyDataSetChanged();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView transactionType_txt, money_txt, itemCategory_txt, wallet_txt, time_txt;

        public TransactionViewHolder(@NonNull View view){
            super(view);
            this.transactionType_txt = view.findViewById(R.id.history_transaction_type_txt);
            this.money_txt = view.findViewById(R.id.history_money_txt);
            this.itemCategory_txt = view.findViewById(R.id.history_category_txt);
            this.wallet_txt = view.findViewById(R.id.history_wallet_txt);
            this.time_txt = view.findViewById(R.id.history_time_txt);
        }

        public void bindTransactionViewHolder(Transaction transaction, List<Item> itemList, List<Wallet> walletList){
            // TODO: Add transfer between wallet
            if(transaction.transactionType.equals(TransactionType.EXPENSE)){
                transactionType_txt.setText("-");
                transactionType_txt.setTextColor(Color.RED);
            } else if(transaction.transactionType.equals(TransactionType.INCOME)){
                transactionType_txt.setText("+");
                transactionType_txt.setTextColor(Color.GREEN);
            }
            //TODO: (Low) String.format
            money_txt.setText(transaction.amountOfMoney.getNumberStripped().toBigInteger().toString());
            for(Item item: itemList){
                if(transaction.itemId.equals(item.itemId)){
                    itemCategory_txt.setText(item.itemCategory.toString());
                    break;
                }
            }

            for(Wallet wallet: walletList){
                if(transaction.walletId.equals(wallet.walletId)){
                    wallet_txt.setText(wallet.walletName);
                    break;
                }
            }

            DateTimeFormatter transactionDayFormat = DateTimeFormatter.ofPattern("dd MM yyyy");
            LocalDateTime time = transaction.localDateTime;
            time_txt.setText(transactionDayFormat.format(time));
        }
    }

    public static class MonthViewHolder extends RecyclerView.ViewHolder{
        TextView total_txt, date_txt;
        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            total_txt = itemView.findViewById(R.id.history_total_txt);
            date_txt = itemView.findViewById(R.id.history_date_txt);
        }

        public void bindMonthViewHolder(Map<String, BigInteger> mapMonthToMoney){
        }
    }
}
