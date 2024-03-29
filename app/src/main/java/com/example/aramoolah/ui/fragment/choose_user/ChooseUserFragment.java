package com.example.aramoolah.ui.fragment.choose_user;

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

import com.example.aramoolah.data.model.User;
import com.example.aramoolah.databinding.FragmentChooseUserBinding;
import com.example.aramoolah.viewmodel.LoginViewModel;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class ChooseUserFragment extends Fragment {
    private FragmentChooseUserBinding binding;

    private LoginViewModel mLoginViewModel;
//    private AddTransactionViewModel mAddTransactionViewModel;
    private RecyclerView chooseUser_recycler;
    private ChooseUserAdapter chooseUserAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChooseUserBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        mAddTransactionViewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);
        chooseUser_recycler = binding.chooseUserRecycler;
        chooseUserAdapter = new ChooseUserAdapter(mLoginViewModel);
        chooseUser_recycler.setAdapter(chooseUserAdapter);
        chooseUser_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            mLoginViewModel.getUserList().observe(getViewLifecycleOwner(), userListObserver);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    final Observer<List<User>> userListObserver = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> userList) {
            chooseUserAdapter.updateUserList(userList);
        }
    };

    public void initializeDatabase() throws InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
//        new Initialize(mAddTransactionViewModel, mLoginViewModel);
    }
}