package com.openclassrooms.realestatemanager.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class CheckBoxAdapter extends ArrayAdapter<String> {

    public interface OnItemClicked{void onClickedPItem(String item, boolean isChecked);}

    private final Context context;
    private final List<String> pointOfInterest;
    private final List<String> userPointOfInterest;
    private final OnItemClicked onItemClicked;


    public CheckBoxAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, List<String> userPointOfInterest, OnItemClicked onItemClicked) {
        super(context, resource, objects);
        this.pointOfInterest = objects;
        this.context = context;
        this.userPointOfInterest = userPointOfInterest;
        this.onItemClicked = onItemClicked;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_assignment_dialog_list_layout, parent, false);
        String item = pointOfInterest.get(position);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox_point_of_interest);
        checkBox.setText(item);
        checkBox.setTag(item + "id");
        if (userPointOfInterest.contains(item))
            checkBox.setChecked(true);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> onItemClicked.onClickedPItem(item, isChecked));
        return rowView;
    }

    @Override
    public int getCount() {
        return pointOfInterest.size();
    }
}
