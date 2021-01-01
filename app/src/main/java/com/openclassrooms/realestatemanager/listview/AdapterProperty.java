package com.openclassrooms.realestatemanager.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.ItemPropertyBinding;
import com.openclassrooms.realestatemanager.models.Property;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class AdapterProperty extends RecyclerView.Adapter<AdapterProperty.PropertyViewHolder> {

    private final List<Property> propertyList;

    public AdapterProperty(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterProperty.PropertyViewHolder(ItemPropertyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {

        Property property = propertyList.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("USD"));
        String price = format.format(property.getPropertyPrice());

        holder.binding.propertyType.setText(property.getPropertyType());
        holder.binding.propertyPrice.setText(price.substring(0, price.length()-2));
        holder.binding.propertyTown.setText(property.getPropertyLocatedCity());
        Glide.with(holder.binding.propertyPicture.getContext())
                .load(property.getPropertyPreviewImageUrl())
                .into(holder.binding.propertyPicture);
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
