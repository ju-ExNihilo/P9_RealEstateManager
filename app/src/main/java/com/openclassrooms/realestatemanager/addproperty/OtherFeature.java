package com.openclassrooms.realestatemanager.addproperty;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMainFeatureBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentOtherFeatureBinding;
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.utils.Utils;


public class OtherFeature extends Fragment {

    private FragmentOtherFeatureBinding binding;
    private NavController navController;

    public OtherFeature newInstance() {return new OtherFeature();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtherFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        this.onClickBackBtn();
        this.onClickValidateBtn();
    }

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            NavDirections action = OtherFeatureDirections.actionOtherFeatureToAddressFeature();
            navController.navigate(action);
        });
    }

    private void onClickValidateBtn(){
        binding.validateBtn.setOnClickListener(v -> {
            Utils.displayNotification("Property Added", "The property has been added correctly", getContext());
            HomeActivity.navigate(getActivity());
        });
    }
}
