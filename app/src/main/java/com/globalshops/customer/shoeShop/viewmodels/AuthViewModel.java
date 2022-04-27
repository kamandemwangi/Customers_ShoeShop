package com.globalshops.customer.shoeShop.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.globalshops.customer.shoeShop.models.User;
import com.globalshops.customer.shoeShop.repositories.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private AuthRepository repository;

    @Inject
    public AuthViewModel(AuthRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> createAccount(String email, String password, User user){
        return repository.createAccount(email, password, user);
    }

    public LiveData<Boolean> logIn(String email, String password){
        return repository.logIn(email, password);
    }
    public LiveData<User> getUserDetails(){
        return repository.getUserDetails();
    }
    public void logOut(){
        repository.logOut();
    }
}
