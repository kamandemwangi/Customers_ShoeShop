package com.globalshops.customer.shoeShop.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.User;
import com.globalshops.customer.shoeShop.utils.InputValidation;
import com.globalshops.customer.shoeShop.viewmodels.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAccountFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout fullNamesTextInputLayout;
    private TextInputLayout phoneNumberTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private MaterialButton createAccountButton;
    private MaterialButton haveAccountButton;
    private ProgressBar createAccountProgressBar;

    private AuthViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullNamesTextInputLayout = view.findViewById(R.id.create_account_full_name_textField);
        phoneNumberTextInputLayout = view.findViewById(R.id.create_account_phone_number_textField);
        emailTextInputLayout = view.findViewById(R.id.create_account_email_textField);
        passwordTextInputLayout = view.findViewById(R.id.create_account_password_textField);
        createAccountButton = view.findViewById(R.id.create_account_button);
        haveAccountButton = view.findViewById(R.id.create_account_have_an_account_button);
        createAccountProgressBar = view.findViewById(R.id.create_account_progressBar);

        addClickListeners();
    }

    private void createAccount(){
        createAccountProgressBar.setVisibility(View.VISIBLE);
        String fullNames = fullNamesTextInputLayout.getEditText().getText().toString().trim();
        String phoneNumber = phoneNumberTextInputLayout.getEditText().getText().toString().trim();
        String email = emailTextInputLayout.getEditText().getText().toString().trim();
        String password = passwordTextInputLayout.getEditText().getText().toString().trim();

        if (InputValidation.isValidName(fullNames, fullNamesTextInputLayout)
        |InputValidation.isValidPhoneNumber(phoneNumber, phoneNumberTextInputLayout)
        |InputValidation.isValidEmail(email, emailTextInputLayout)
        |InputValidation.isValidPassword(password, passwordTextInputLayout)){
            createAccountProgressBar.setVisibility(View.GONE);
            return;
        }else {
            User user = new User(fullNames, phoneNumber, email);
            viewModel.createAccount(email, password, user).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean){
                        createAccountProgressBar.setVisibility(View.GONE);
                        //navigate home fragment
                        NavHostFragment.findNavController(CreateAccountFragment.this).navigate(R.id.create_account_fragment_to_home_fragment);
                    }
                }
            });
        }
    }

    private void addClickListeners(){
        createAccountButton.setOnClickListener(this);
        haveAccountButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_account_button:{
                createAccount();
                break;
            }
            case R.id.create_account_have_an_account_button:{
                //navigate to log in page
                NavHostFragment.findNavController(CreateAccountFragment.this).navigate(R.id.create_account_fragment_to_login_fragment);
                break;
            }
        }
    }
}