package com.openclassrooms.realestatemanager.addproperty;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.openclassrooms.realestatemanager.databinding.PropertyImageItemBinding;
import com.openclassrooms.realestatemanager.models.PropertyImage;

public class PropertyImageAdapter extends FirestoreRecyclerAdapter<PropertyImage, PropertyImageAdapter.PropertyImageHolder> {

    public interface OnDataChange {void onDataChanged();}
    public interface OnClearBtnClicked{void onClickedClearBtn(String propertyImageId);}
    private final OnDataChange onDataChange;
    private final OnClearBtnClicked onClearBtnClicked;

    public PropertyImageAdapter(@NonNull FirestoreRecyclerOptions<PropertyImage> options, OnDataChange onDataChange, OnClearBtnClicked onClearBtnClicked) {
        super(options);
        this.onDataChange = onDataChange;
        this.onClearBtnClicked = onClearBtnClicked;
    }

    @Override
    protected void onBindViewHolder(@NonNull PropertyImageHolder holder, int position, @NonNull PropertyImage model) {
        Glide.with(holder.binding.propertyImage.getContext())
                .load(model.getImageUrl())
                .into(holder.binding.propertyImage);

        holder.binding.deleteBtn.setOnClickListener(v -> onClearBtnClicked.onClickedClearBtn(model.getPropertyImageId()));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        onDataChange.onDataChanged();
    }

    @NonNull
    @Override
    public PropertyImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PropertyImageHolder(PropertyImageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    public class PropertyImageHolder extends RecyclerView.ViewHolder {

        PropertyImageItemBinding binding;

        public PropertyImageHolder(PropertyImageItemBinding itemPropertyBinding) {
            super(itemPropertyBinding.getRoot());
            binding = itemPropertyBinding;
        }
    }
}
