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
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.databinding.FragmentAddressFeatureBinding;

public class AddressFeature extends Fragment {

    private FragmentAddressFeatureBinding binding;
    private NavController navController;

    public AddressFeature newInstance() {return new AddressFeature();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        this.onClickBackBtn();
    }

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            NavDirections action = AddressFeatureDirections.actionAddressFeatureToMainFeature();
            navController.navigate(action);
        });
    }
}
