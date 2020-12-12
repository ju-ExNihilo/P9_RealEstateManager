package com.openclassrooms.realestatemanager.addproperty;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMainFeatureBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPointOfInterestFeatureBinding;

public class PointOfInterestFeature extends Fragment {

    private FragmentPointOfInterestFeatureBinding binding;

    public PointOfInterestFeature newInstance() {return new PointOfInterestFeature();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPointOfInterestFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
