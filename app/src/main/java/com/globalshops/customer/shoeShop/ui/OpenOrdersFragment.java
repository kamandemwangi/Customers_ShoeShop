package com.globalshops.customer.shoeShop.ui;

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
import android.widget.Toast;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.models.ShopDetails;
import com.globalshops.customer.shoeShop.ui.adapters.OpenOrdersRecyclerAdapter;
import com.globalshops.customer.shoeShop.utils.VerticalSpacingItemDecorator;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.globalshops.customer.shoeShop.viewmodels.ShopDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class OpenOrdersFragment extends Fragment {
    private List<Shoe> shoeList = new ArrayList<>();
    private ShopDetails mShopDetails = new ShopDetails();
    private RecyclerView recyclerView;
    private OpenOrdersRecyclerAdapter adapter;

    private ShoeViewModel shoeViewModel;
    private ShopDetailsViewModel shopDetailsViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shoeViewModel  = new ViewModelProvider(this).get(ShoeViewModel.class);
        shopDetailsViewModel = new ViewModelProvider(this).get(ShopDetailsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open__orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.open_orders_recyclerView);
        observeOpenOrders();
    }

    private void initializeRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OpenOrdersRecyclerAdapter(shoeList, mShopDetails, getContext());
        recyclerView.setAdapter(adapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(verticalSpacingItemDecorator);

    }

    private void observeOpenOrders(){
        shoeViewModel.getOpenOrders().observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
            @Override
            public void onChanged(List<Shoe> shoes) {
                if (shoes != null){
                    if (shoeList.size() > 0) {
                        shoeList.clear();
                    }
                    for (Shoe shoe : shoes){
                        shopDetailsViewModel.getSingleShopDetails(shoe.getShopId()).observe(getViewLifecycleOwner(), new Observer<ShopDetails>() {
                            @Override
                            public void onChanged(ShopDetails shopDetails) {
                                if (shopDetails != null){
                                    mShopDetails = shopDetails;
                                }else {
                                    Toast.makeText(getContext(), "Shop null", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        shoeList.add(shoe);
                        initializeRecyclerView();
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}