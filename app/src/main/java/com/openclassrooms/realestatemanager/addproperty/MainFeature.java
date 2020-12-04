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
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMainFeatureBinding;


public class MainFeature extends Fragment {

    private FragmentMainFeatureBinding binding;
    private NavController navController;

    public MainFeature newInstance() {return new MainFeature();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        this.onClickNextBtn();
    }

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            NavDirections action = MainFeatureDirections.actionMainFeatureToAddressFeature();
            navController.navigate(action);
        });
    }
}
