package com.globalshops.customer.shoeShop.ui.bottom_nav;

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
import com.globalshops.customer.shoeShop.ui.OpenOrdersFragment;
import com.globalshops.customer.shoeShop.viewmodels.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AccountFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout emailTextInputLayout;
    private MaterialButton saveChangesButton;
    private MaterialButton logoutButton;
    private MaterialButton openOrdersButton;
    private ProgressBar accountDetailsProgressBar;

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
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextInputLayout = view.findViewById(R.id.account_details_full_names_textField);
        phoneTextInputLayout = view.findViewById(R.id.account_details_phone_textField);
        emailTextInputLayout = view.findViewById(R.id.account_details_email_textField);
        saveChangesButton = view.findViewById(R.id.account_details_save_changes_button);
        logoutButton = view.findViewById(R.id.account_details_log_out_button);
        openOrdersButton = view.findViewById(R.id.account_details_open_orders_button);
        accountDetailsProgressBar = view.findViewById(R.id.account_details_progressBar);
        addClickListeners();
        observeUser();
    }

    private void observeUser(){
        viewModel.getUserDetails().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    nameTextInputLayout.getEditText().setText(user.getFullNames());
                    phoneTextInputLayout.getEditText().setText(user.getPhoneNumber());
                    emailTextInputLayout.getEditText().setText(user.getEmail());
                }
            }
        });
    }

    private void logout(){
        viewModel.logOut();
        NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.account_details_to_login_flow);
    }

    private void addClickListeners(){
        saveChangesButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        openOrdersButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_details_save_changes_button:{
                break;
            }
            case R.id.account_details_log_out_button:{
                logout();
                break;
            }
            case R.id.account_details_open_orders_button:{
                NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.account_details_fragment_to_open_oders_fragment);
                break;
            }
        }
    }
}