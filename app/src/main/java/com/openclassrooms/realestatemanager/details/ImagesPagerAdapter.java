package com.openclassrooms.realestatemanager.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import java.util.List;

public class ImagesPagerAdapter extends PagerAdapter {

    private List<PropertyImage> imagesUrlList;
    private Context mContext;

    public ImagesPagerAdapter(Context mContext, List<PropertyImage> imagesUrlList) {
        this.mContext = mContext;
        this.imagesUrlList = imagesUrlList;
    }

    @Override
    public int getCount() {
        return imagesUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_pager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.property_image);
        TextView description = (TextView) view.findViewById(R.id.property_description);
        description.setText(imagesUrlList.get(position).getImageDescription());
        Glide.with(mContext)
                .load(imagesUrlList.get(position).getImageUrl())
                .into(imageView);
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
