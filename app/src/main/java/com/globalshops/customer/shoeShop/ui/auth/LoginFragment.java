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
import com.globalshops.customer.shoeShop.utils.InputValidation;
import com.globalshops.customer.shoeShop.viewmodels.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private MaterialButton loginButton;
    private MaterialButton createAccountButton;
    private ProgressBar loginProgressBar;

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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailTextInputLayout = view.findViewById(R.id.login_email_textField);
        passwordTextInputLayout = view.findViewById(R.id.login_password_textField);
        loginButton = view.findViewById(R.id.login_button);
        createAccountButton = view.findViewById(R.id.login_create_account_button);
        loginProgressBar = view.findViewById(R.id.login_progressBar);
        addClickListeners();
    }

    private void login(){
        loginProgressBar.setVisibility(View.VISIBLE);
        String email = emailTextInputLayout.getEditText().getText().toString().trim();
        String password = passwordTextInputLayout.getEditText().getText().toString().trim();

        if (InputValidation.isValidEmail(email, emailTextInputLayout)
        |InputValidation.isValidPassword(password, passwordTextInputLayout)){
            loginProgressBar.setVisibility(View.GONE);
            return;
        }else {
            viewModel.logIn(email, password).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean){
                        //navigate to home fragment
                        NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.login_fragment_to_home_fragment);
                    }
                }
            });
        }
    }
    private void addClickListeners(){
        loginButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:{
                login();
                break;
            }
            case R.id.login_create_account_button:{
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.login_fragment_to_create_account_fragment);
                break;
            }
        }
    }
}