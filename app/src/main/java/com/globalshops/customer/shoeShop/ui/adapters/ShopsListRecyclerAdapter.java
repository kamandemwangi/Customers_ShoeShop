package com.globalshops.customer.shoeShop.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.ShopDetails;
import com.google.android.material.textview.MaterialTextView;


import java.util.List;

public class ShopsListRecyclerAdapter extends RecyclerView.Adapter<ShopsListRecyclerAdapter.ShopsListViewHolder> {
    private Context context;
    private List<ShopDetails> shopsList;
    private OnShopContainer onShopContainer;

    public ShopsListRecyclerAdapter(Context context, List<ShopDetails> shopsList, OnShopContainer onShopContainer) {
        this.context = context;
        this.shopsList = shopsList;
        this.onShopContainer = onShopContainer;
    }

    @NonNull
    @Override
    public ShopsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_shops_list_viewholder, viewGroup, false);
        return new ShopsListViewHolder(view, onShopContainer);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsListViewHolder holder, int i) {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.outline_error_24)
                .fitCenter();

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(shopsList.get(i).getProfileImageUrl())
                .placeholder(R.drawable.outline_store_24)
                .centerCrop()
                .into(holder.shopProfileImage);

        holder.shopName.setText(shopsList.get(i).getName());
        holder.shopLocation.setText(shopsList.get(i).getStreet());
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public class ShopsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView shopProfileImage;
        private MaterialTextView shopName;
        private MaterialTextView shopLocation;
        private OnShopContainer onShopContainer;

        public ShopsListViewHolder(@NonNull View itemView, OnShopContainer onShopContainer) {
            super(itemView);

            shopProfileImage = itemView.findViewById(R.id.shop_profile_image);
            shopName = itemView.findViewById(R.id.shop_name);
            shopLocation = itemView.findViewById(R.id.shop_location);

            this.onShopContainer = onShopContainer;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onShopContainer.onShopClick(getAdapterPosition());
        }
    }

    public interface OnShopContainer{
        void onShopClick(int position);
    }
}
