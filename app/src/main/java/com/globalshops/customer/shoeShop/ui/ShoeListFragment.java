package com.globalshops.customer.shoeShop.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.ui.adapters.ShoeListRecyclerAdapter;
import com.globalshops.customer.shoeShop.utils.VerticalSpacingItemDecorator;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShoeListFragment extends Fragment implements ShoeListRecyclerAdapter.OnShoeContainer {
    private List<Shoe> shoeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShoeListRecyclerAdapter adapter;
    private View progressBarView;

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
        return inflater.inflate(R.layout.fragment_shoe_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.shoe_list_recyclerView);
        progressBarView = view.findViewById(R.id.layout_progressBar);
        initializeRecyclerView();
        observeShoes();

    }

    private void initializeRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShoeListRecyclerAdapter(getActivity(), shoeList, this);
        recyclerView.setAdapter(adapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(verticalSpacingItemDecorator);
    }

    private void observeShoes(){
        progressBarView.setVisibility(View.VISIBLE);
        viewModel.getShoes(getIncomingShopId()).observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
            @Override
            public void onChanged(List<Shoe> shoes) {
                if (shoes != null){
                    if (shoeList.size() > 0){
                        shoeList.clear();
                    }
                    for (Shoe shoe : shoes){
                        shoeList.add(shoe);
                        progressBarView.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private String getIncomingShopId(){
        return getArguments().getString("selected_shop");
    }

    private Bundle putSelectedShoeBundle(int position){
        Bundle bundle = new Bundle();
        bundle.putString("selected_shoe", shoeList.get(position).getProductId());
        bundle.putString("selected_shop", getIncomingShopId());

        return bundle;
    }
    @Override
    public void onShoeClick(int position) {
        NavHostFragment.findNavController(ShoeListFragment.this).navigate(R.id.shoe_list_fragment_to_shoe_full_details_fragment, putSelectedShoeBundle(position));
    }
}