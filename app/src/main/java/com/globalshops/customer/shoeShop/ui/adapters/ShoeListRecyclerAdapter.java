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
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ShoeListRecyclerAdapter extends RecyclerView.Adapter<ShoeListRecyclerAdapter.ShoeListViewHolder> {
    private Context context;
    private List<Shoe> shoeList;
    private OnShoeContainer onShoeContainer;

    public ShoeListRecyclerAdapter(Context context, List<Shoe> shoeList, OnShoeContainer onShoeContainer) {
        this.context = context;
        this.shoeList = shoeList;
        this.onShoeContainer = onShoeContainer;
    }

    @NonNull
    @Override
    public ShoeListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_shoe_list_viewholder, viewGroup, false);
        return new ShoeListViewHolder(view, onShoeContainer);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeListViewHolder holder, int i) {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.outline_error_24)
                .fitCenter();

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(shoeList.get(i).getImageUrls().get(0))
                .placeholder(R.drawable.add_shoe_placeholder)
                .centerCrop()
                .into(holder.shoeImageView);
        holder.shoeName.setText(shoeList.get(i).getName());
        holder.shoePriceTag.setText("Ksh."+shoeList.get(i).getShoePriceTag());
    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    public class ShoeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView shoeImageView;
        private MaterialTextView shoeName;
        private MaterialTextView shoePriceTag;
        private OnShoeContainer onShoeContainer;

        public ShoeListViewHolder(@NonNull View itemView, OnShoeContainer onShoeContainer) {
            super(itemView);

            shoeImageView = itemView.findViewById(R.id.shoe_list_image_view);
            shoeName = itemView.findViewById(R.id.shoe_list_name);
            shoePriceTag = itemView.findViewById(R.id.shoe_list_price_tag);
            this.onShoeContainer = onShoeContainer;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onShoeContainer.onShoeClick(getAdapterPosition());
        }
    }

    public interface OnShoeContainer{
        void onShoeClick(int position);
    }
}
