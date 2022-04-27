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
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.models.ShoeViewPager;
import com.globalshops.customer.shoeShop.ui.adapters.ShoeColorsRecyclerAdapter;
import com.globalshops.customer.shoeShop.ui.adapters.ShoeSizesRecyclerAdapter;
import com.globalshops.customer.shoeShop.ui.adapters.view_pager.ShoePagerAdapter;
import com.globalshops.customer.shoeShop.ui.adapters.view_pager.ViewPagerItemFragment;
import com.globalshops.customer.shoeShop.utils.VerticalSpacingItemDecorator;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.globalshops.customer.shoeShop.viewmodels.ShopDetailsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShoeFullDetailsFragment extends Fragment implements ShoeSizesRecyclerAdapter.OnShoeSizesContainer, ShoeColorsRecyclerAdapter.OnShoeColorContainer, View.OnClickListener {
    private static final String TAG = "ShoeFullDetailsFragment";
    private static final String SHoe_SIZE_QUANTITY_LIST_FIELD = "shoeSizeQuantityList";
    private static final String SHOE_SIZES_LIST_FIELD = "shoeSizesList";
    private static final String SHOE_COLOR_QUANTITY_LIST_FIELD = "shoeColorQuantityList";
    private static final String SHOE_COLOR_LIST_FIELD = "shoeColorsList";
    private List<String> shoeSizesList = new ArrayList<>();
    private List<String> shoeColorsList = new ArrayList<>();
    private List<HashMap<String, String>> shoeSizeQuantityList = new ArrayList<>();
    private List<HashMap<String, String>> shoeColorQuantityList = new ArrayList<>();
    private String selectedShoeSize;
    private String selectedShoeColor;
    private String selectedShoeSizeQuantity;
    private String selectedShoeColorQuantity;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ShoeViewModel viewModel;
    private ShopDetailsViewModel shopDetailsViewModel;

    //ui
    private MaterialTextView shoeNameTextView;
    private MaterialTextView shoePriceTagTextView;
    private Chip genderChip;
    private MaterialTextView shortDescTextView;
    private RecyclerView shoeSizesRecyclerView;
    private MaterialTextView unselectedShoeSizeErrorText;
    private RecyclerView shoeColorsRecyclerView;
    private MaterialTextView unselectedShoeColorErrorText;
    private MaterialButton addToBagButton;
    private View incrementDecrementView;
    private MaterialButton decrementButton;
    private MaterialTextView quantityTextView;
    private ProgressBar icnreDecreProgressBar;
    private MaterialButton incrementButton;
    private MaterialButton continueShoppingButton;
    private MaterialButton proceedToPaymentButton;
    private MaterialCardView snackBarContainer;

    private ShoeSizesRecyclerAdapter adapter;
    private ShoeColorsRecyclerAdapter shoeColorsRecyclerAdapter;

    private String shoeName;
    private String shoePriceTag;
    private String shoeProductId;
    private String shoeSize;
    private String shoeImageUrl;

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
        return inflater.inflate(R.layout.fragment_shoe_full_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        shoeNameTextView = view.findViewById(R.id.full_details_shoe_name);
        shoePriceTagTextView = view.findViewById(R.id.full_details_shoe_price_tag);
        genderChip = view.findViewById(R.id.full_details_shoe_gender);
        shortDescTextView = view.findViewById(R.id.full_details_shoe_short_desc);
        shoeSizesRecyclerView = view.findViewById(R.id.shoe_full_details_shoe_sizes_recyclerView);
        unselectedShoeSizeErrorText = view.findViewById(R.id.full_details_shoe_unselected_shoe_size_error_text);
        shoeColorsRecyclerView = view.findViewById(R.id.shoe_full_details_shoe_color_recyclerView);
        unselectedShoeColorErrorText = view.findViewById(R.id.full_details_shoe_unselected_shoe_color_error_text);
        addToBagButton = view.findViewById(R.id.add_to_bag_button);
        incrementDecrementView = view.findViewById(R.id.layout_quantity_increment_decrement);
        decrementButton = view.findViewById(R.id.decrement_button);
        quantityTextView = view.findViewById(R.id.quantity);
        icnreDecreProgressBar = view.findViewById(R.id.icre_decre_progressBar);
        incrementButton = view.findViewById(R.id.increment_button);
        continueShoppingButton = view.findViewById(R.id.continue_shopping_button);
        proceedToPaymentButton = view.findViewById(R.id.proceed_to_payment_button);
        snackBarContainer = view.findViewById(R.id.snack_bar_view);

        addClickListeners();

        observeShoe();
        initializeShoeSizesRecyclerView();
        initializeShoeColorsRecyclerView();

    }

    private void initializeShoeSizesRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        shoeSizesRecyclerView.setLayoutManager(layoutManager);
        adapter = new ShoeSizesRecyclerAdapter(shoeSizesList, this);
        shoeSizesRecyclerView.setAdapter(adapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(2);
        shoeSizesRecyclerView.addItemDecoration(verticalSpacingItemDecorator);
    }

    private void initializeShoeColorsRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        shoeColorsRecyclerView.setLayoutManager(layoutManager);
        shoeColorsRecyclerAdapter = new ShoeColorsRecyclerAdapter(shoeColorsList, this);
        shoeColorsRecyclerView.setAdapter(shoeColorsRecyclerAdapter);
        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(2);
        shoeColorsRecyclerView.addItemDecoration(verticalSpacingItemDecorator);
    }
    private void observeShoe(){
        viewModel.getSingleShoe(getSelectedShopId(), getSelectedShoeProductId()).observe(getViewLifecycleOwner(), new Observer<Shoe>() {
            @Override
            public void onChanged(Shoe shoe) {
                if (shoe != null){

                    if (shoeSizesList.size() > 0){
                        shoeSizesList.clear();
                    }

                    if (shoeColorsList.size() > 0){
                        shoeColorsList.clear();
                    }

                    List<Fragment> fragments = new ArrayList<>();
                        if (shoe.getProductId().equals(getSelectedShoeProductId())){
                            for (int i = 0; i < shoe.getImageUrls().size(); i++){
                                ShoeViewPager viewPager = new ShoeViewPager();
                                viewPager.setImageUrl(shoe.getImageUrls().get(i));
                                ViewPagerItemFragment fragment = ViewPagerItemFragment.getInstance(viewPager);
                                fragments.add(fragment);

                                shoeNameTextView.setText(shoe.getName());
                                shoePriceTagTextView.setText("Ksh."+shoe.getShoePriceTag());
                                genderChip.setText(shoe.getGender());
                                shortDescTextView.setText(shoe.getShortDesc());

                                shoeName = shoe.getName();
                                shoePriceTag = shoe.getShoePriceTag();
                                shoeProductId = shoe.getProductId();
                                shoeImageUrl = shoe.getImageUrls().get(0);



                            }
                            for (int j = 0; j < shoe.getShoeSizesList().size(); j++){
                                if (!shoe.getShoeSizeQuantityList().get(j).get(shoe.getShoeSizesList().get(j)).equals("0")){
                                    shoeSizesList.add(shoe.getShoeSizesList().get(j));
                                    shoeSizeQuantityList.add(shoe.getShoeSizeQuantityList().get(j));
                                    adapter.notifyDataSetChanged();
                                }

                            }
                            for (int k = 0; k < shoe.getShoeColorsList().size(); k++){
                                if (!shoe.getShoeColorQuantityList().get(k).get(shoe.getShoeColorsList().get(k)).equals("0")){
                                    shoeColorsList.add(shoe.getShoeColorsList().get(k));
                                    shoeColorQuantityList.add(shoe.getShoeColorQuantityList().get(k));
                                    shoeColorsRecyclerAdapter.notifyDataSetChanged();
                                }

                            }

                        }

                    ShoePagerAdapter adapter = new ShoePagerAdapter(getParentFragmentManager(), fragments);
                    viewPager.setAdapter(adapter);
                    tabLayout.setupWithViewPager(viewPager, true);
                }
            }
        });
    }
    private void addToBag(){
        if (shoeSizesList.size() < 0){
            Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
            return;
        }else if (shoeColorsList.size() < 0){
            Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
            return;
        }else if (selectedShoeSize == null | selectedShoeColor == null){
            unselectedShoeSizeErrorText.setVisibility(View.VISIBLE);
            unselectedShoeColorErrorText.setVisibility(View.VISIBLE);
            return;
        }else {
            String orderNumber = String.valueOf(System.currentTimeMillis());
            String initialQuantity = "1";
            String initialTotal = shoePriceTag;
            Shoe shoe = new Shoe(shoePriceTag, shoeName, shoeProductId, selectedShoeSize, selectedShoeColor, initialQuantity, initialTotal, shoeImageUrl, orderNumber, getSelectedShopId());

            viewModel.addToBag(shoe, orderNumber).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        showAddToBagSnackBar();
                        //decrementDbShoeSizesQuantity(getSelectedShopId(), getSelectedShoeProductId());
                        //decrementDbShoeColorQuantity(getSelectedShopId(), getSelectedShoeProductId());
                        addToBagButton.setVisibility(View.GONE);
                        //incrementDecrementView.setVisibility(View.VISIBLE);
                        unselectedShoeSizeErrorText.setVisibility(View.GONE);
                        unselectedShoeColorErrorText.setVisibility(View.GONE);
                        continueShoppingButton.setVisibility(View.VISIBLE);
                        proceedToPaymentButton.setVisibility(View.VISIBLE);

                    }
                }
            });
        }
    }

    private void decrementDbShoeSizesQuantity(String shopId, String shoeProductId){
        HashMap<String, String> oldValue = new HashMap<>();
        HashMap<String, String> newValue = new HashMap<>();
        for (int i = 0; i <shoeSizeQuantityList.size(); i++){
            if (shoeSizeQuantityList.get(i).get(selectedShoeSize) != null){
                String shoeSizesQuantity = shoeSizeQuantityList.get(i).get(selectedShoeSize);
                oldValue.put(selectedShoeSize, shoeSizesQuantity);
                int value = Integer.parseInt(shoeSizesQuantity);

            if (value > 0){
                value = value - 1;
            }else {
                Toast.makeText(getContext(), "Stock at 0", Toast.LENGTH_SHORT).show();
                return;
            }

            String shoeQuantityNewValue = String.valueOf(value);
            newValue.put(selectedShoeSize, shoeQuantityNewValue);
            }

        }


        viewModel.decrementDbShoeQuantity(shopId, shoeProductId, oldValue, newValue, selectedShoeSize, SHOE_SIZES_LIST_FIELD, SHoe_SIZE_QUANTITY_LIST_FIELD);

    }

    private void decrementDbShoeColorQuantity(String shopId, String shoeProductId){
        HashMap<String, String> oldValue = new HashMap<>();
        HashMap<String, String> newValue = new HashMap<>();
        for (int i = 0; i <shoeColorQuantityList.size(); i++){
            if (shoeColorQuantityList.get(i).get(selectedShoeColor) != null){
                String shoeColorQuantity = shoeColorQuantityList.get(i).get(selectedShoeColor);
                oldValue.put(selectedShoeColor, shoeColorQuantity);
                int value = Integer.parseInt(shoeColorQuantity);

                if (value > 0){
                    value = value - 1;
                }else {
                    Toast.makeText(getContext(), "Stock at 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                String shoeQuantityNewValue = String.valueOf(value);
                newValue.put(selectedShoeColor, shoeQuantityNewValue);
            }
            viewModel.decrementDbShoeQuantity(shopId, shoeProductId, oldValue, newValue, selectedShoeColor, SHOE_COLOR_LIST_FIELD, SHOE_COLOR_QUANTITY_LIST_FIELD);
        }
    }
    private void incrementOrderQuantity(){
        viewModel.placedOrder(shoeProductId).observe(getViewLifecycleOwner(), new Observer<Shoe>() {
            @Override
            public void onChanged(Shoe shoe) {
                if (shoe != null){
                    String quantity = shoe.getQuantity();
                    String total = shoe.getTotal();


                    if (shoeSizesList.size() < 0){
                        Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (shoeColorsList.size() < 0){
                        Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (selectedShoeSize == null | selectedShoeColor == null){
                        unselectedShoeSizeErrorText.setVisibility(View.VISIBLE);
                        unselectedShoeColorErrorText.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        int intQuantity = Integer.parseInt(quantity);
                        int intTotal = Integer.parseInt(total);

                        intQuantity++;
                        intTotal = intTotal + Integer.parseInt(shoePriceTag);

                        viewModel.updatePlacedOrder(String.valueOf(intQuantity), String.valueOf(intTotal), shoeProductId);
                        decrementDbShoeSizesQuantity(getSelectedShopId(), getSelectedShoeProductId());
                    }
                }
            }
        });
    }

    private String getSelectedShoeProductId(){
        return getArguments().getString("selected_shoe");
    }

    private String getSelectedShopId(){
        return getArguments().getString("selected_shop");
    }

    private Bundle putSelectedShopId(){
        Bundle bundle = new Bundle();
        bundle.putString("selected_shop", getSelectedShopId());
        return bundle;
    }

    private void showAddToBagSnackBar(){
        Snackbar.make(snackBarContainer, "Added to bag successfully", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public String onShoeSizeClick(String checkedChip) {
        selectedShoeSize = checkedChip;
        return selectedShoeSize;
    }

    @Override
    public String onShoeClick(String checkedChip) {
        selectedShoeColor = checkedChip;
        return selectedShoeColor;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_to_bag_button:{
                addToBag();
                break;
            }
            case R.id.increment_button:{
                incrementOrderQuantity();
                break;
            }
            case R.id.continue_shopping_button:{
                //navigate to shoes list
                NavHostFragment.findNavController(ShoeFullDetailsFragment.this).navigate(R.id.shoe_full_details_fragment_to_shoe_list_fragment);
                break;
            }
            case R.id.proceed_to_payment_button:{
                //navigate to shopping bag
                NavHostFragment.findNavController(ShoeFullDetailsFragment.this).navigate(R.id.shoe_full_details_fragment_to_shopping_bag_fragment, putSelectedShopId());
                break;
            }
        }
    }

    private void addClickListeners(){
        addToBagButton.setOnClickListener(this);
        incrementButton.setOnClickListener(this);
        continueShoppingButton.setOnClickListener(this);
        proceedToPaymentButton.setOnClickListener(this);
    }
}