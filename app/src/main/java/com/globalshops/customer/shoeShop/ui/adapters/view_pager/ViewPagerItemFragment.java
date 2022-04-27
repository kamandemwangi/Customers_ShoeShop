package com.globalshops.customer.shoeShop.ui.adapters.view_pager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.models.ShoeViewPager;

public class ViewPagerItemFragment extends Fragment {
    private static final String TAG = "ViewPagerItemFragment";
    private ImageView imageView;

   // private Shoe shoe;
    private ShoeViewPager shoeViewPager;

    public static ViewPagerItemFragment getInstance(ShoeViewPager shoeViewPager){
        ViewPagerItemFragment fragment = new ViewPagerItemFragment();

        if (shoeViewPager != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("shoe_view_pager", shoeViewPager);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            shoeViewPager = getArguments().getParcelable("shoe_view_pager");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.layout_viewpager_item_viewholder, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.view_pager_image_view);
        initialize();
    }

    private void initialize(){
        if (shoeViewPager != null){
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.outline_error_24)
                    .fitCenter();


                Glide.with(getActivity())
                        .setDefaultRequestOptions(requestOptions)
                        .load(shoeViewPager.getImageUrl())
                        .placeholder(R.drawable.add_shoe_placeholder)
                        .centerCrop()
                        .into(imageView);

            Log.d(TAG, "initialize: "+shoeViewPager.getImageUrl());


        }
    }
}
