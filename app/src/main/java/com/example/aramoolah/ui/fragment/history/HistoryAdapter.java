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
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TRANSACTION_VIEW = 0;
    private static final int MONTH_VIEW = 1;

    private List<Transaction> transactionList;
    private List<Item> itemList;
    private List<Wallet> walletList;
    private Map<YearMonth, BigInteger> mapMonthToMoney;
    private int currentTransactionInd;
    private YearMonth oldYearMonth;
    private YearMonth currentYearMonth;
    private final NumberFormat moneyFormat;

    public HistoryAdapter(){
        moneyFormat = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
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
            Transaction transaction = transactionList.get(currentTransactionInd);
            currentTransactionInd--;
            ((TransactionViewHolder) holder) .bindTransactionViewHolder(transaction, itemList, walletList, moneyFormat);
        }
        if(holder instanceof MonthViewHolder){
            ((MonthViewHolder) holder).bindMonthViewHolder(mapMonthToMoney, currentYearMonth, moneyFormat);

        }

    }

    @Override
    public int getItemCount() {
        return transactionList.size() + mapMonthToMoney.size();
    }

    @Override
    public int getItemViewType(int position){
        Log.d("History adapter", "postion:" + currentTransactionInd);
        int result = TRANSACTION_VIEW;

        YearMonth nextYearMonth = null;
        nextYearMonth = YearMonth.from(transactionList.get(currentTransactionInd).localDateTime);


        if(currentYearMonth == null){
            result = MONTH_VIEW;
        } else if(nextYearMonth.isBefore(currentYearMonth)){
            result = MONTH_VIEW;
        }
        oldYearMonth = currentYearMonth;
        currentYearMonth = nextYearMonth;
        return result;
//        return super.getItemViewType(position);
    }

    public void updateTransactionList(List<Transaction> transactionList){
        this.transactionList.clear();
        this.transactionList = transactionList;
//        currentYearMonth = YearMonth.from(this.transactionList.get(0).localDateTime);
        currentTransactionInd = transactionList.size() - 1;
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

    public void updateMapMonthToMoney(Map<YearMonth, BigInteger> mapMonthToMoney){
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

        public void bindTransactionViewHolder(Transaction transaction, List<Item> itemList, List<Wallet> walletList, NumberFormat moneyFormat){
            // TODO: Add transfer between wallet
            if(transaction.transactionType.equals(TransactionType.EXPENSE)){
                transactionType_txt.setText("-");
                transactionType_txt.setTextColor(Color.RED);
                money_txt.setTextColor(Color.RED);
            } else if(transaction.transactionType.equals(TransactionType.INCOME)){
                transactionType_txt.setText("+");
                transactionType_txt.setTextColor(Color.GREEN);
                money_txt.setTextColor(Color.GREEN);
            } else {
                transactionType_txt.setText("⇌");
                transactionType_txt.setTextColor(Color.YELLOW);
                money_txt.setTextColor(Color.YELLOW);
            }
            //TODO: (Low) String.format

            money_txt.setText(moneyFormat.format(transaction.amountOfMoney.getNumberStripped().toBigInteger()));
            for(Item item: itemList){
                if(transaction.itemId.equals(item.itemId)){
                    String itemCategory = item.itemCategory.toString();
                    String firstChar = itemCategory.substring(0, 1);
                    String remainingChar = itemCategory.substring(1).toLowerCase();

                    itemCategory_txt.setText(String.format("%s%s", firstChar, remainingChar));
                    break;
                }
            }

            for(Wallet wallet: walletList){
                if(transaction.walletId.equals(wallet.walletId)){
                    wallet_txt.setText(wallet.walletName);
                    break;
                }
            }

            DateTimeFormatter transactionDayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

        public void bindMonthViewHolder(Map<YearMonth, BigInteger> mapMonthToMoney, YearMonth currentYearMonth, NumberFormat moneyFormat){
            DateTimeFormatter monthYearFormat = DateTimeFormatter.ofPattern("MM/yyyy");
            date_txt.setText(monthYearFormat.format(currentYearMonth));
            total_txt.setText(moneyFormat.format(mapMonthToMoney.get(currentYearMonth)));
        }
    }
}
