package com.openclassrooms.realestatemanager.addproperty;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentImagesFeatureBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPointOfInterestFeatureBinding;
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.utils.Utils;


public class ImagesFeature extends Fragment {

    private FragmentImagesFeatureBinding binding;

    public ImagesFeature newInstance() {return new ImagesFeature();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImagesFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    private void onClickValidateBtn(){
        Utils.displayNotification("Property Added", "The property has been added correctly", getContext());
        HomeActivity.navigate(getActivity());
    }
}
