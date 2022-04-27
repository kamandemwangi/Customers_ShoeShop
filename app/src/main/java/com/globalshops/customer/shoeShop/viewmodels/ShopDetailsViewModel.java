package com.globalshops.customer.shoeShop.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.globalshops.customer.shoeShop.models.ShopDetails;
import com.globalshops.customer.shoeShop.repositories.ShopDetailsRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ShopDetailsViewModel extends ViewModel {
    public ShopDetailsRepository repository;

    @Inject
    public ShopDetailsViewModel(ShopDetailsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<ShopDetails>> getShopDetails(){
        return repository.getShopDetails();
    }

    public void addSelectedShopId(Map<String, Object> shopId){
        repository.addSelectedShopId(shopId);
    }

    public LiveData<String> getSelectedShopId(){
        return repository.getSelectedShopId();
    }

    public LiveData<ShopDetails> getSingleShopDetails(String shopId){
        return repository.getSingleShopDetails(shopId);
    }
}
