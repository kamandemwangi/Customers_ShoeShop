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
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.models.ShopDetails;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OpenOrdersRecyclerAdapter extends RecyclerView.Adapter<OpenOrdersRecyclerAdapter.OpenOrdersViewHolder> {
    private List<Shoe> shoeList;
    private ShopDetails shopDetails;
    private Context context;

    public OpenOrdersRecyclerAdapter(List<Shoe> shoeList, ShopDetails shopDetails, Context context) {
        this.shoeList = shoeList;
        this.shopDetails = shopDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public OpenOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_open_orders_view_holder, parent, false);
        return new OpenOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenOrdersViewHolder holder, int position) {
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.outline_error_24)
                    .fitCenter();

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(shoeList.get(position).getImageUrlAt0())
                    .placeholder(R.drawable.add_shoe_placeholder)
                    .circleCrop()
                    .into(holder.shoeImageView);
            holder.shoeQuantityTextView.setText("Quantity: "+shoeList.get(position).getQuantity());
            holder.shoeSizeTextView.setText("Size: "+shoeList.get(position).getShoeSize());
            holder.shoeColorTextView.setText("Color: "+shoeList.get(position).getShoeColor());
            holder.shoePriceTagTextView.setText("Price: "+shoeList.get(position).getShoePriceTag());
            holder.shoeOrderTextView.setText("Order no: "+shoeList.get(position).getOrderNumber());
            holder.shopStreetTextView.setText("Shop: "+shopDetails.getStreet()+",");
            holder.shopBuildingTextView.setText(shopDetails.getBusinessBuilding()+",");
            holder.shopShopNumberTextView.setText(shopDetails.getShopNumber());

    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    public class OpenOrdersViewHolder extends RecyclerView.ViewHolder {
        private ImageView shoeImageView;
        private MaterialTextView shoeQuantityTextView;
        private MaterialTextView shoeSizeTextView;
        private MaterialTextView shoeColorTextView;
        private MaterialTextView shoePriceTagTextView;
        private MaterialTextView shoeOrderTextView;
        private MaterialTextView shopStreetTextView;
        private MaterialTextView shopBuildingTextView;
        private MaterialTextView shopShopNumberTextView;

        public OpenOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            shoeImageView = itemView.findViewById(R.id.open_orders_imageView);
            shoeQuantityTextView  = itemView.findViewById(R.id.open_orders_quantity);
            shoeSizeTextView = itemView.findViewById(R.id.open_orders_shoe_size);
            shoeColorTextView = itemView.findViewById(R.id.open_orders_shoe_color);
            shoePriceTagTextView = itemView.findViewById(R.id.open_oders_shoe_price_tag);
            shoeOrderTextView = itemView.findViewById(R.id.open_orders_order_number);
            shopStreetTextView = itemView.findViewById(R.id.open_orders_shop_street);
            shopBuildingTextView = itemView.findViewById(R.id.open_orders_shop_building);
            shopShopNumberTextView = itemView.findViewById(R.id.open_orders_shopNumber);
        }
    }
}
