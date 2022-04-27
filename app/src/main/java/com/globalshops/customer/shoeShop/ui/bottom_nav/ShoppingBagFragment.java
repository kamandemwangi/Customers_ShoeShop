package com.globalshops.customer.shoeShop.ui.bottom_nav;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.ui.adapters.ShoppingBagRecyclerAdapter;
import com.globalshops.customer.shoeShop.utils.VerticalSpacingItemDecorator;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ShoppingBagFragment extends Fragment implements View.OnClickListener {
    private List<Shoe> shoeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MaterialButton nextButton;
    private ShoppingBagRecyclerAdapter adapter;
    private MaterialTextView totals;
    private MaterialCardView totalsCardView;

    private ShoeViewModel viewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ShoeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_bag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.shopping_bag_recyclerView);
        nextButton = view.findViewById(R.id.shopping_bag_next_button);
        totals = view.findViewById(R.id.shopping_bag_totals);
        totalsCardView = view.findViewById(R.id.shopping_bag_totals_cardView);

        addClickListeners();
        observeShoes();
        observeShoppingBagTotals();
        //hideViews();
    }

    private void initializeRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShoppingBagRecyclerAdapter(shoeList, getContext(), viewModel, getViewLifecycleOwner());
        recyclerView.setAdapter(adapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(verticalSpacingItemDecorator);

    }

    private void observeShoes(){
        viewModel.getShoppingBagShoes().observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
            @Override
            public void onChanged(List<Shoe> shoes) {
                if (shoeList.size() > 0){
                    shoeList.clear();
                }
                if (shoes != null){
                    for (Shoe shoe : shoes){
                        shoeList.add(shoe);
                    }
                    initializeRecyclerView();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void observeShoppingBagTotals(){
        viewModel.getShoppingBagTotals().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null){
                    totals.setText("Ksh."+String.valueOf(aDouble));
                }else if (aDouble < 0){
                    totals.setText("Ksh.0");
                }else {
                    totals.setText("Ksh. Error");
                }
            }
        });
    }

    private String getSelectedShopId(){
        return getArguments().getString("selected_shop");
    }

    private Bundle putSelectedShopIdBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("selected_shop",getSelectedShopId() );
        return bundle;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shopping_bag_next_button:{
                NavHostFragment.findNavController(ShoppingBagFragment.this).navigate(R.id.shopping_bag_fragment_to_deliver_service_fragment, putSelectedShopIdBundle());
                break;
            }
        }
    }
    private void addClickListeners(){
        nextButton.setOnClickListener(this);
    }
    private void hideViews(){
        if (shoeList.size() == 0){
            totalsCardView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }else {
            totalsCardView.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }
}