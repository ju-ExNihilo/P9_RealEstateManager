package com.openclassrooms.realestatemanager.map;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentOtherFeatureBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private Animation fadeInAnim;

    public MapFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.mapLayout.setAnimation(fadeInAnim);
    }
}
