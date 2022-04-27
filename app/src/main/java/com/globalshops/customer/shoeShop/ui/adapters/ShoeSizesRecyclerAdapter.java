package com.globalshops.customer.shoeShop.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.globalshops.customer.shoeShop.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ShoeSizesRecyclerAdapter extends RecyclerView.Adapter<ShoeSizesRecyclerAdapter.ShoeSizesRecyclerViewHolder> {
    private static final String TAG = "ShoeSizesRecyclerAdapter";
    private List<String> containerArray = new ArrayList<>();
    private List<MaterialCardView> cardViewList = new ArrayList<>();
    private List<String> shoeSizesList;
    private OnShoeSizesContainer onShoeSizesContainer;

    public ShoeSizesRecyclerAdapter(List<String> shoeSizesList, OnShoeSizesContainer onShoeSizesContainer) {
        this.shoeSizesList = shoeSizesList;
        this.onShoeSizesContainer = onShoeSizesContainer;
    }

    @NonNull
    @Override
    public ShoeSizesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_custom_shoe_size_chip_viewholder, viewGroup, false);
        return new ShoeSizesRecyclerViewHolder(view, onShoeSizesContainer);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeSizesRecyclerViewHolder holder, int i) {
        holder.chipTextViewHolder.setText(shoeSizesList.get(i));
    }

    @Override
    public int getItemCount() {
        return shoeSizesList.size();
    }

    public class ShoeSizesRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView container;
        private MaterialTextView chipTextViewHolder;
        private OnShoeSizesContainer onShoeSizesContainer;

        public ShoeSizesRecyclerViewHolder(@NonNull View itemView, OnShoeSizesContainer onShoeSizesContainer) {
            super(itemView);
            container = itemView.findViewById(R.id.chip_container);
            chipTextViewHolder = itemView.findViewById(R.id.chip_text_holder);

            this.onShoeSizesContainer = onShoeSizesContainer;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

             if (!container.isChecked()) {
                 container.setChecked(true);
                 containerArray.add(shoeSizesList.get(getAdapterPosition()));
                 cardViewList.add(container);
                 if (!containerArray.get(0).equals(shoeSizesList.get(getAdapterPosition()))) {
                     cardViewList.get(0).setChecked(false);
                     containerArray.remove(0);
                     cardViewList.remove(0);
                 }
             }
            onShoeSizesContainer.onShoeSizeClick(getCheckedChip());
        }

        private String getCheckedChip(){
            if(container.isChecked()){
                return shoeSizesList.get(getAdapterPosition());
            }else {
                return null;
            }
        }
    }



    public interface OnShoeSizesContainer{
        String onShoeSizeClick(String checkedChip);
    }
}
