package com.openclassrooms.realestatemanager.listview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ItemPropertyBinding;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class AdapterProperty extends RecyclerView.Adapter<AdapterProperty.PropertyViewHolder> {

    public interface OnPropertyClicked{void onClickedProperty(String propertyId);}

    private final List<Property> propertyList;
    private final OnPropertyClicked onPropertyClicked;

    public AdapterProperty(List<Property> propertyList, OnPropertyClicked onPropertyClicked) {
        this.propertyList = propertyList;
        this.onPropertyClicked = onPropertyClicked;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterProperty.PropertyViewHolder(ItemPropertyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.binding.propertyType.setText(property.getPropertyType());
        holder.binding.propertyPrice.setText(Utils.formatPrice(property.getPropertyPrice(), "USD"));
        holder.binding.propertyTown.setText(property.getPropertyLocatedCity());
        if (property.isSold()){
            Glide.with(holder.binding.propertyPicture.getContext())
                    .load(R.drawable.sale_pic)
                    .into(holder.binding.propertyPicture);
        }else {
            Glide.with(holder.binding.propertyPicture.getContext())
                    .load(property.getPropertyPreviewImageUrl())
                    .into(holder.binding.propertyPicture);
        }

        holder.itemView.setOnClickListener(v -> onPropertyClicked.onClickedProperty(property.getPropertyId()));
        Log.i("DEBUGGG", property.toString());
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder {

        ItemPropertyBinding binding;

        public PropertyViewHolder(ItemPropertyBinding itemPropertyBinding) {
            super(itemPropertyBinding.getRoot());
            binding = itemPropertyBinding;
        }
    }
}