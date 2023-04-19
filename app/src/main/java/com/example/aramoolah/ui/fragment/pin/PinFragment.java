package com.example.aramoolah.ui.fragment.pin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aramoolah.R;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.databinding.FragmentPinBinding;
import com.example.aramoolah.ui.activity.PersonalFinanceActivity;
import com.example.aramoolah.viewmodel.LoginViewModel;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.transform.Result;

public class PinFragment extends Fragment {
    FragmentPinBinding binding;
    LoginViewModel mLoginViewModel;
    PersonalFinanceViewModel mPersonalFinanceViewModel;
    EditText pinPassword_et;
    Button pinLogin_btn;
    TextView userName_txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPinBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mPersonalFinanceViewModel = new ViewModelProvider(this).get(PersonalFinanceViewModel.class);
        pinLogin_btn = binding.pinLoginBtn;
        pinPassword_et = binding.pinPasswordEt;
        userName_txt = binding.pinUserNameTxt;

        // Get current user name and id from shared preference
        SharedPreferences login = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userName = login.getString("userName", "Placeholder-name");
        int userId = login.getInt("userId", -1);


        if(userId == -1){
            Toast.makeText(getContext(), "User information failed to be retrieved", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_pin_fragment_to_nav_choose_user_fragment);
        }

        // Set current user using userId
        try {
            mLoginViewModel.setCurrentUser(userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        userName_txt.setText(userName);

        // TODO: Reinitialize the data with new user
        // TODO: Check password hash result in database
        // TODO: Test Check password functions

        // On login check if password is correct
        pinLogin_btn.setOnClickListener(view1 -> {
            if(pinPassword_et.getText() != null){

                try {
                    if(mLoginViewModel.isCorrectPassword(userId, pinPassword_et.getText().toString())){
                        // If correct -> set the current user in PersonalFinanceViewModel -> start the main activity
                        mPersonalFinanceViewModel.setCurrentUser(userId);
                        getActivity().setResult(Activity.RESULT_OK);
                        Intent intent = new Intent(getContext(), PersonalFinanceActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        // Else -> get bonked
                        Toast.makeText(getContext(), "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), "Pin cannot be empty.", Toast.LENGTH_SHORT).show();
            }

        });

    }


}