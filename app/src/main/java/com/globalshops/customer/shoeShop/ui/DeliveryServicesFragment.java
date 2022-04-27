package com.globalshops.customer.shoeShop.ui;

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
import android.widget.Toast;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.globalshops.customer.shoeShop.viewmodels.ShopDetailsViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DeliveryServicesFragment extends Fragment implements View.OnClickListener {
    private MaterialCardView homeOfficeDeliveryCardView;
    private MaterialCardView pickFromTheShopCardView;

    private ShoeViewModel viewModel;
    private ShopDetailsViewModel shopDetailsViewModel;

    private boolean isOrderAdded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ShoeViewModel.class);

        shopDetailsViewModel = new ViewModelProvider(this).get(ShopDetailsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeOfficeDeliveryCardView = view.findViewById(R.id.home_office_delivery_cardView);
        pickFromTheShopCardView = view.findViewById(R.id.pick_from_shop_cardView);

        addClickListeners();
        //Toast.makeText(getContext(), getSelectedShopId(), Toast.LENGTH_SHORT).show();
    }

    private void addOrdersToShops(){
        shopDetailsViewModel.getSelectedShopId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null){
                    viewModel.getShoppingBagShoes().observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
                        @Override
                        public void onChanged(List<Shoe> shoes) {
                            if (shoes != null){
                                for(Shoe shoe : shoes){
                                    viewModel.addOrderToShop(s, shoe.getOrderNumber(), shoe).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                        @Override
                                        public void onChanged(Boolean aBoolean) {
                                            if (aBoolean){
                                                isOrderAdded = aBoolean;
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });
                }
            }
        });
        if (isOrderAdded){
            NavHostFragment.findNavController(DeliveryServicesFragment.this).navigate(R.id.delivery_service_fragment_to_payment_fragment);
        }


    }

    private String getSelectedShopId(){
        return getArguments().getString("selected_shop");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_office_delivery_cardView:{
                Toast.makeText(getContext(), "Service currently unavailable", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.pick_from_shop_cardView:{
                //navigate to payment fragment
                addOrdersToShops();
                break;
            }
        }
    }

    private void addClickListeners(){
        homeOfficeDeliveryCardView.setOnClickListener(this);
        pickFromTheShopCardView.setOnClickListener(this);
    }
}