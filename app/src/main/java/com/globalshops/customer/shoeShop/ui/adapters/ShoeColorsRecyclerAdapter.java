package com.globalshops.customer.shoeShop.ui.adapters;

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

public class ShoeColorsRecyclerAdapter extends RecyclerView.Adapter<ShoeColorsRecyclerAdapter.ShoeColorsViewRecyclerViewHolder> {
    private List<String> containerArray = new ArrayList<>();
    private List<MaterialCardView> cardViewList = new ArrayList<>();

    private List<String> shoeColorsList;
    private OnShoeColorContainer onShoeColorContainer;

    public ShoeColorsRecyclerAdapter(List<String> shoeColorsList, OnShoeColorContainer onShoeColorContainer) {
        this.shoeColorsList = shoeColorsList;
        this.onShoeColorContainer = onShoeColorContainer;
    }

    @NonNull
    @Override
    public ShoeColorsViewRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_custom_shoe_size_chip_viewholder, viewGroup, false);
        return new ShoeColorsViewRecyclerViewHolder(view, onShoeColorContainer);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeColorsViewRecyclerViewHolder holder, int i) {
        holder.chipTextViewHolder.setText(shoeColorsList.get(i));
    }

    @Override
    public int getItemCount() {
        return shoeColorsList.size();
    }

    public class ShoeColorsViewRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView container;
        private MaterialTextView chipTextViewHolder;
        private OnShoeColorContainer onShoeColorContainer;

        public ShoeColorsViewRecyclerViewHolder(@NonNull View itemView, OnShoeColorContainer onShoeColorContainer) {
            super(itemView);
            container = itemView.findViewById(R.id.chip_container);
            chipTextViewHolder = itemView.findViewById(R.id.chip_text_holder);

            this.onShoeColorContainer = onShoeColorContainer;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!container.isChecked()){
                container.setChecked(true);
                containerArray.add(shoeColorsList.get(getAdapterPosition()));
                cardViewList.add(container);
                if (!containerArray.get(0).equals(shoeColorsList.get(getAdapterPosition()))){
                    cardViewList.get(0).setChecked(false);
                    containerArray.remove(0);
                    cardViewList.remove(0);
                }
            }
            onShoeColorContainer.onShoeClick(getCheckedChip());
        }

        private String getCheckedChip(){
            if(container.isChecked()){
                return shoeColorsList.get(getAdapterPosition());
            }else {
                return null;
            }
        }
    }

    public interface OnShoeColorContainer{
        String onShoeClick(String checkedChip);
    }
}
