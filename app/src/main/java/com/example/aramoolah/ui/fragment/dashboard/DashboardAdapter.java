package com.example.aramoolah.ui.fragment.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.Wallet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int WALLET_TITLE = 0;
    private static final int WALLET_INFO = 1;
    private static final int WALLET_ADD = 2;
    private static final int ITEM_TITLE = 3;
    private static final int ITEM_MORE = 4;

    private List<Wallet> walletList;
    Integer walletListSize = 0;

    public DashboardAdapter(){
        this.walletList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == WALLET_TITLE){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_title, parent, false);
            return new WalletTitleViewHolder(view);
        }
        if(viewType == WALLET_INFO){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_info, parent, false);
            return new WalletInfoViewHolder(view);
        }
        if(viewType == WALLET_ADD){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_add, parent, false);
            return new WalletAddViewHolder(view);
        }
        if(viewType == ITEM_TITLE){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_title, parent, false);
            return new ItemTitleViewHolder(view);
        }
        if(viewType == ITEM_MORE){
            View view = inflater.inflate(R.layout.layout_recycler_dashboard_more, parent, false);
            return new ItemMoreViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  WalletTitleViewHolder){
            WalletTitleViewHolder wHolder = (WalletTitleViewHolder) holder;
        }
        if(holder instanceof WalletInfoViewHolder){
            Wallet wallet = walletList.get(position-1);
            ((WalletInfoViewHolder) holder).bindWalletInfoViewHolder(wallet);
        }
        if(holder instanceof WalletAddViewHolder){
            ((WalletAddViewHolder) holder).bindWalletAddViewHolder();
        }
        if(holder instanceof ItemTitleViewHolder){
            ((ItemTitleViewHolder) holder).bindItemTitleViewHolder();
        }
        if(holder instanceof ItemMoreViewHolder){
            ((ItemMoreViewHolder) holder).bindItemMoreViewHOlder();
        }


    }

    @Override
    public int getItemCount() {
        return walletListSize + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return WALLET_TITLE;
        } else if(position > 0 && position <= walletListSize) {
            return WALLET_INFO;
        } else if(position == walletListSize + 1) {
            return WALLET_ADD;
        } else if(position == walletListSize + 2){
            return ITEM_TITLE;
        } else if(position == walletListSize + 3){
            return ITEM_MORE;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void updateWalletList(List<Wallet> walletList){
        this.walletList.clear();
        this.walletList = walletList;
        this.walletListSize = walletList.size();
        notifyDataSetChanged();
    }

    public static class WalletTitleViewHolder extends RecyclerView.ViewHolder{
        TextView walletTitle_txt;
        public WalletTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.walletTitle_txt = itemView.findViewById(R.id.dashboard_wallet_title_txt);
        }
    }

    public static class WalletInfoViewHolder extends RecyclerView.ViewHolder{
        Button walletInfo_btn;
        public WalletInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            walletInfo_btn = itemView.findViewById(R.id.dashboard_info_btn);
        }

        //Set wallet info into button on bind
        public void bindWalletInfoViewHolder(Wallet wallet){
            NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));

            String btnText = wallet.walletName + " " + moneyFormat.format(wallet.totalAmount.getNumberStripped().toBigInteger());
            walletInfo_btn.setText(btnText);
        }
    }

    public static class WalletAddViewHolder extends RecyclerView.ViewHolder{
        Button walletAdd_btn;

        public WalletAddViewHolder(@NonNull View itemView) {
            super(itemView);
            walletAdd_btn = itemView.findViewById(R.id.dashboard_add_btn);
        }

        public void bindWalletAddViewHolder(){
            walletAdd_btn.setText("Add Wallet");
            walletAdd_btn.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_dashboard_fragment_to_nav_add_wallet_fragment));
        }
    }

    public static class ItemTitleViewHolder extends RecyclerView.ViewHolder{
        TextView itemTitle_txt;
        public ItemTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle_txt = itemView.findViewById(R.id.dashboard_wallet_title_txt);
        }

        public void bindItemTitleViewHolder(){
            itemTitle_txt.setText("List of items");
        }
    }

    public static class ItemMoreViewHolder extends RecyclerView.ViewHolder{
        Button itemMore_btn;
        public ItemMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            itemMore_btn = itemView.findViewById(R.id.dashboard_more_btn);
        }

        public void bindItemMoreViewHOlder(){
            itemMore_btn.setText("More items");
            itemMore_btn.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_dashboard_fragment_to_nav_list_item_category_fragment));
        }
    }
}
