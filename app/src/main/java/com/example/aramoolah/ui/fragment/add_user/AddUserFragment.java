package com.example.aramoolah.ui.fragment.add_user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aramoolah.R;
import com.example.aramoolah.databinding.FragmentAddUserBinding;
import com.example.aramoolah.viewmodel.AddUserViewModel;
import com.example.aramoolah.viewmodel.LoginViewModel;

public class AddUserFragment extends Fragment {
    FragmentAddUserBinding binding;
    AddUserViewModel mAddUserViewModel;
    LoginViewModel mLoginViewModel;
    EditText firstName_et;
    EditText middleName_et;
    EditText lastName_et;
    EditText email_et;
    EditText password_et;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddUserViewModel = new ViewModelProvider(this).get(AddUserViewModel.class);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        firstName_et = binding.addUserFirstNameEt;
        middleName_et = binding.addUserMiddleNameEt;
        lastName_et = binding.addUserLastNameEt;
        email_et = binding.addUserEmailEt;
        password_et = binding.addUserPasswordEt;

        binding.addUserSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addUser();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void addUser() throws InterruptedException {

        if(!inputCheck()){
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        } else if(!passwordCheck()){
            password_et.setError("Pin can only contain 6 number");
            Toast.makeText(getContext(), "Pin can only contain 6 number.", Toast.LENGTH_SHORT).show();
        } else if(!emailCheck()){
            email_et.setError("Email is inappropriate.");
            Toast.makeText(getContext(), "Email is inappropriate.", Toast.LENGTH_SHORT).show();
        } else{
            mAddUserViewModel.addUser(
                    firstName_et.getText().toString(),
                    middleName_et.getText().toString(),
                    lastName_et.getText().toString(),
                    email_et.getText().toString(),
                    password_et.getText().toString());
            mLoginViewModel.updateUserList();
            Toast.makeText(getContext(), "Add new user successfully.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_add_user_fragment_to_nav_choose_user_fragment);
        }

    }

    // TODO: test if this is always true
    private boolean inputCheck(){
        return (!firstName_et.getText().toString().equals("")) &&
                (!lastName_et.getText().toString().equals("")) &&
                (!email_et.getText().toString().equals("")) &&
                (!password_et.getText().toString().equals(""));
    }

    private boolean passwordCheck(){
        return (password_et.getText().toString().length() == 6);
    }

    private boolean emailCheck() throws InterruptedException {
        return (email_et.getText().toString().contains("@")) &&
                (email_et.getText().toString().contains(".")) &&
                // Check if email is unique
                (mAddUserViewModel.getUser(email_et.getText().toString()) == null);
    }
}