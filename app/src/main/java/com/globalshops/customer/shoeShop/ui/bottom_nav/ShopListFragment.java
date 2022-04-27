package com.globalshops.customer.shoeShop.ui.bottom_nav;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.models.ShopDetails;
import com.globalshops.customer.shoeShop.ui.adapters.ShopsListRecyclerAdapter;
import com.globalshops.customer.shoeShop.utils.VerticalSpacingItemDecorator;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.globalshops.customer.shoeShop.viewmodels.ShopDetailsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ShopListFragment extends Fragment implements ShopsListRecyclerAdapter.OnShopContainer {
    private List<ShopDetails> shopDetailsList = new ArrayList<>();
    private ShopsListRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View progressBarView;

    private ShopDetailsViewModel viewModel;
    private ShoeViewModel shoeViewModel;

    private String dbShopId;
    private boolean selectedAnotherShop = false;

    @Inject
    FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ShopDetailsViewModel.class);
        shoeViewModel = new ViewModelProvider(this).get(ShoeViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.shop_list_recyclerView);
        progressBarView = view.findViewById(R.id.layout_progressBar);

        isLoggedIn();
        initializeRecyclerView();
        observeShopsList();
    }

    private void initializeRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopsListRecyclerAdapter(getActivity(), shopDetailsList, this);
        recyclerView.setAdapter(adapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(verticalSpacingItemDecorator);
    }

    private void observeShopsList(){
        progressBarView.setVisibility(View.VISIBLE);
        viewModel.getShopDetails().observe(getViewLifecycleOwner(), new Observer<List<ShopDetails>>() {
            @Override
            public void onChanged(List<ShopDetails> shopDetails) {
                if (shopDetails != null){

                    if (shopDetailsList.size() > 0){
                        shopDetailsList.clear();
                    }
                    for (ShopDetails shopDetails1 : shopDetails){
                        shopDetailsList.add(shopDetails1);
                    }
                    adapter.notifyDataSetChanged();
                    progressBarView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void differentShopSelection(int position){
        viewModel.getSelectedShopId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null){
                    dbShopId = s;
                    shoeViewModel.getShoppingBagShoes().observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
                        @Override
                        public void onChanged(List<Shoe> shoes) {
                            if (shoes != null && !s.equals(shopDetailsList.get(position).getShopId())){
                                selectedAnotherShop = true;
                             }else {
                            }
                        }
                    });
                }else {
                    addSelectedShopIdToDB(position);
                }

            }
        });

        if (selectedAnotherShop){
            showEmptyShoppingBagDialog(position);
        }else {
            addSelectedShopIdToDB(position);
            NavHostFragment.findNavController(ShopListFragment.this).navigate(R.id.shoe_list_fragment_to_shoe_list_fragment, putShopBundle(position));
        }

    }

    private void showEmptyShoppingBagDialog(int position){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        AlertDialog dialog;

        dialogBuilder.setTitle("Alert")
                .setMessage("Your shopping bag will empty from choosing a different store")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                emptyShoppingBag(position);
            }
        }).show();

//        dialog = dialogBuilder.create();
//        dialog.show();
    }

    private void emptyShoppingBag(int position){
        shoeViewModel.getShoppingBagShoes().observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
            @Override
            public void onChanged(List<Shoe> shoes) {
                if (shoes != null){
                    for (Shoe shoe : shoes){
                        String orderNumber = shoe.getOrderNumber();
                        shoeViewModel.emptyShoppingBag(orderNumber);
                        addSelectedShopIdToDB(position);
                        NavHostFragment.findNavController(ShopListFragment.this).navigate(R.id.shoe_list_fragment_to_shoe_list_fragment, putShopBundle(position));
                    }
                }
            }
        });
    }

    private void addSelectedShopIdToDB(int position){
        Map<String, Object> shopId = new HashMap<>();
        shopId.put("shopId", shopDetailsList.get(position).getShopId());
        viewModel.addSelectedShopId(shopId);
    }

    @Override
    public void onShopClick(int position) {
        differentShopSelection(position);

    }


    private Bundle putShopBundle(int position){
        Bundle bundle = new Bundle();
        bundle.putString("selected_shop", shopDetailsList.get(position).getShopId());
        return bundle;
    }

    private void isLoggedIn(){
        if (auth.getCurrentUser() == null){
            NavHostFragment.findNavController(ShopListFragment.this).navigate(R.id.shop_list_fragment_to_login_flow);
        }else {
        }
    }
}