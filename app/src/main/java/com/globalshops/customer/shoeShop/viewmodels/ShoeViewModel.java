package com.globalshops.customer.shoeShop.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.repositories.ShoeRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ShoeViewModel extends ViewModel {
    private ShoeRepository repository;

    @Inject
    public ShoeViewModel(ShoeRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Shoe>> getShoes(String shopId){
        return repository.getShoes(shopId);
    }

    public LiveData<Boolean> addToBag(Shoe shoe, String orderNumber){
        return repository.addToBag(shoe, orderNumber);
    }

    public LiveData<Shoe> getSingleShoe(String shopId, String productId){
        return repository.getSingleShoe(shopId, productId);
    }

    public void decrementDbShoeQuantity(String shopId, String productId, Map<String, String> oldValue, Map<String, String> newValue, String arrayValue, String shoesListField, String field){
        repository.decrementDbShoeQuantity(shopId, productId, oldValue, newValue, arrayValue, shoesListField, field);
    }
    public LiveData<Shoe> placedOrder(String productId){
        return repository.getPlaceOrder(productId);
    }
    public void updatePlacedOrder(String quantity, String total, String productId){
        repository.updatePlaceOrder(quantity, total, productId);
    }

    public LiveData<List<Shoe>> getShoppingBagShoes(){
        return repository.getShoppingBagShoes();
    }
    public void deleteShoppingBagSingleShoe(String orderNumber, String shoePriceTag){
        repository.deleteShoppingBagSingleShoe(orderNumber, shoePriceTag);
    }
    public LiveData<Double> getShoppingBagTotals(){
        return repository.getShoppingBagTotals();
    }

    public LiveData<Boolean> addOrderToShop(String shopId, String orderNumber, Shoe shoe){
        return repository.addOrdersToShops(shopId, orderNumber, shoe);
    }

    public void emptyShoppingBag(String orderNumber){
        repository.emptyShoppingBag(orderNumber);
    }

    public LiveData<Shoe> getShoeOrderStatus(String shopId, String orderNumber){
        return repository.getOrderStatus(shopId, orderNumber);
    }

    public LiveData<List<Shoe>> getOpenOrders(){
        return repository.getOpenOrders();
    }

}
