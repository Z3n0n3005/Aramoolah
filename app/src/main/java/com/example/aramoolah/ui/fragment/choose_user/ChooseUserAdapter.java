package com.example.aramoolah.ui.fragment.choose_user;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.viewmodel.LoginViewModel;

import java.util.ArrayList;
import java.util.List;


public class ChooseUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CHOOSE_USER = 0;
    private static final int ADD_USER = 1;

    private static LoginViewModel mLoginViewModel;
    private List<User> userList;
    Integer userListSize = 0;
    private static User currentUser;

    public ChooseUserAdapter(LoginViewModel mLoginViewModel){
        this.userList = new ArrayList<>();
        ChooseUserAdapter.mLoginViewModel = mLoginViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == CHOOSE_USER){
            View view = inflater.inflate(R.layout.layout_recycler_choose_user, parent, false);
            return new ChooseUserViewHolder(view);
        }
        if(viewType == ADD_USER){
            View view = inflater.inflate(R.layout.layout_recycler_add_user, parent, false);
            return new AddUserViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ChooseUserViewHolder){
            User current = userList.get(position);
            ((ChooseUserViewHolder) holder).bindChooseUserViewHolder(current);
        }
        if(holder instanceof AddUserViewHolder){
            AddUserViewHolder tempHolder = ((AddUserViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return userListSize + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < userListSize){
            return CHOOSE_USER;
        } else {
            return ADD_USER;
        }
//        return super.getItemViewType(position);
    }

    public void updateUserList(List<User> userList){
        this.userList.clear();
        this.userList = userList;
        this.userListSize = userList.size();
        notifyDataSetChanged();
    }

    public static class ChooseUserViewHolder extends RecyclerView.ViewHolder{
        Button chooseUser_btn;
        Context context;
        public ChooseUserViewHolder(@NonNull View itemView) {
            super(itemView);
            chooseUser_btn = itemView.findViewById(R.id.choose_user_choose_user_btn);
            context = itemView.getContext();
        }

        public void bindChooseUserViewHolder(User user){
            chooseUser_btn.setText(user.firstName);
            chooseUser_btn.setOnClickListener(view -> {
                SharedPreferences login = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = login.edit();
                loginEditor.putString("userName", user.firstName);
                loginEditor.putInt("userId", user.userId);
                loginEditor.apply();
                Navigation.findNavController(view).navigate(R.id.action_nav_choose_user_fragment_to_nav_pin_fragment);
            });
        }
    }

    public static class AddUserViewHolder extends RecyclerView.ViewHolder{
        Button addUser_btn;


        public AddUserViewHolder(@NonNull View itemView) {
            super(itemView);
            addUser_btn = itemView.findViewById(R.id.choose_user_add_user_btn);
            addUser_btn.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_choose_user_fragment_to_nav_add_user_fragment));
        }
    }
}
