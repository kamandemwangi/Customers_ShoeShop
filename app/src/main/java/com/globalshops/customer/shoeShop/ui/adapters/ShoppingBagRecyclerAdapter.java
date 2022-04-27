package com.globalshops.customer.shoeShop.ui.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ShoppingBagRecyclerAdapter extends RecyclerView.Adapter<ShoppingBagRecyclerAdapter.ShoppingBagRecyclerViewHolder> {
    private List<Shoe> shoeList;
    private Context context;
    private ShoeViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    public ShoppingBagRecyclerAdapter(List<Shoe> shoeList, Context context, ShoeViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.shoeList = shoeList;
        this.context = context;
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ShoppingBagRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_shopping_bag_view_holder, viewGroup, false);
        return new ShoppingBagRecyclerViewHolder(view, viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingBagRecyclerViewHolder holder, int i) {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.outline_error_24)
                .fitCenter();

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(shoeList.get(i).getImageUrlAt0())
                .placeholder(R.drawable.add_shoe_placeholder)
                .circleCrop()
                .into(holder.shoeImageView);
        holder.shoeNameTextView.setText(shoeList.get(i).getName());
        holder.shoePriceTagTextView.setText("Ksh. "+shoeList.get(i).getShoePriceTag());
        holder.shoeQuantityTextView.setText("Quantity "+shoeList.get(i).getQuantity());

    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    public class ShoppingBagRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView shoeImageView;
        private MaterialTextView shoeNameTextView;
        private MaterialTextView shoePriceTagTextView;
        private MaterialTextView shoeQuantityTextView;
        private MaterialButton removeProductButton;

        private ShoeViewModel viewModel;

        public ShoppingBagRecyclerViewHolder(@NonNull View itemView, ShoeViewModel viewModel) {
            super(itemView);
            shoeImageView = itemView.findViewById(R.id.shopping_bag_image_view);
            shoeNameTextView = itemView.findViewById(R.id.shopping_bag_shoe_name);
            shoePriceTagTextView = itemView.findViewById(R.id.shopping_bag_shoe_price_tag);
            shoeQuantityTextView = itemView.findViewById(R.id.shopping_bag_shoe_quantity);
            removeProductButton = itemView.findViewById(R.id.shopping_bag_remove_product_button);
            this.viewModel = viewModel;
            addClickListeners();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.shopping_bag_remove_product_button:{
                    //remove shoe
                    removeShoe();
                    break;
                }
            }
        }
        private void removeShoe(){
            viewModel.deleteShoppingBagSingleShoe(shoeList.get(getAdapterPosition()).getOrderNumber(), shoeList.get(getAdapterPosition()).getShoePriceTag());
            notifyDataSetChanged();
        }
        private void addClickListeners(){
            removeProductButton.setOnClickListener(this);
        }
    }
}
