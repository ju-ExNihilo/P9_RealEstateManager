package com.openclassrooms.realestatemanager.details;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;
    private NavController navController;
    private String propertyId;
    private PropertyViewModel propertyViewModel;

    public DetailsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        propertyId = getArguments().getString("propertyId");
        this.initPropertyViewModel();
        this.initMainFeature();
        this.initPicture();
    }


    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    private void initMainFeature(){
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            binding.typeProperty.setText(property.getPropertyType());
            binding.locationProperty.setText(property.getPropertyLocatedCity());
            binding.priceProperty.setText(String.valueOf(property.getPropertyPrice()));
        });
    }

    private void initPicture(){
        propertyViewModel.getAllImagesByPropertyIdForDetails(propertyId).observe(getViewLifecycleOwner(), propertyImages -> {
            binding.viewPager.setAdapter(new ImagesPagerAdapter(getContext(), propertyImages));
        });

    }

}
