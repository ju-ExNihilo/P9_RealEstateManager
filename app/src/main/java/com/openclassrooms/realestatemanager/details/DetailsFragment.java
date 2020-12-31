package com.openclassrooms.realestatemanager.details;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.*;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.ArrayList;
import java.util.List;

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
        this.initAddress();
        this.initOtherFeature();
        this.accordionTransition();
        this.initUpdateBtn();
        this.initPointOfInterest();
    }

    private void initUpdateBtn(){
        Bundle bundle = new Bundle();
        bundle.putString("propertyId", propertyId);
        binding.updateAddress.setOnClickListener(v -> navController.navigate(R.id.addressFeature, bundle));
        binding.updateMainFeature.setOnClickListener(v -> navController.navigate(R.id.mainFeature, bundle));
        binding.updateOtherFeature.setOnClickListener(v -> navController.navigate(R.id.otherFeature, bundle));
    }

    private void accordionTransition(){
        binding.addressTitle.setOnClickListener(v -> animate(binding.cardAddress, binding.addressTitle));
        binding.mainFeatureTitle.setOnClickListener(v -> animate(binding.cardMainFeature, binding.mainFeatureTitle));
        binding.descriptionTitle.setOnClickListener(v -> animate(binding.cardDescription, binding.descriptionTitle));
        binding.otherFeatureTitle.setOnClickListener(v -> animate(binding.otherFeatureCard, binding.otherFeatureTitle));
        binding.pointOfInterestTitle.setOnClickListener(v -> animate(binding.pointOfInterestCard, binding.pointOfInterestTitle));
    }

    private void animate(View card, TextView title){
        if (card.getVisibility() == View.VISIBLE){
            card.setVisibility(View.GONE);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_down), null );
        }else {
            card.setVisibility(View.VISIBLE);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_up), null);
        }
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

    private void initAddress(){
        propertyViewModel.getPropertyAddressById(propertyId).observe(getViewLifecycleOwner(), address -> {
            if (address != null){
                binding.addressNumber.setText(String.valueOf(address.getNumberOfWay()));
                binding.addressWay.setText(address.getWay());
                binding.addressPostcode.setText(String.valueOf(address.getPostCode()));
                binding.addressAdditional.setText(address.getAdditionalAddressField());
            }
        });
    }

    private void initPointOfInterest(){
        propertyViewModel.getPointOfInterestById(propertyId).observe(getViewLifecycleOwner(), pointOfInterests -> {
            if (pointOfInterests != null){
                List<String> nameList = new ArrayList<>();
                for (PointOfInterest pointOfInterest : pointOfInterests){
                    binding.chipCloud.addChip(pointOfInterest.getPointOfInterestName());
                }

            }
        });
    }

    private void initOtherFeature(){
        propertyViewModel.getPropertyFeatureById(propertyId).observe(getViewLifecycleOwner(), propertyFeature -> {
            if (propertyFeature != null){
                if (propertyFeature.getPropertyDescription().isEmpty()){
                    binding.cardDescription.setVisibility(View.GONE);
                    binding.descriptionTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_down), null );
                }
                binding.descriptionProperty.setText(propertyFeature.getPropertyDescription());
                binding.otherFeatureStatus.setText(propertyFeature.isSale() ? "Sale" : "Free");
                binding.otherFeatureSurface.setText(propertyFeature.getPropertySurface() + " m²");
                binding.otherFeatureNumberRoom.setText(String.valueOf(propertyFeature.getNumberOfRooms()));
                binding.otherFeatureBedroom.setText(String.valueOf(propertyFeature.getNumberOfBedrooms()));
                binding.otherFeatureBathroom.setText(String.valueOf(propertyFeature.getNumberOfBathrooms()));
                binding.otherFeatureEntranceDate.setText(propertyFeature.getEntranceDate());
                binding.otherFeatureSaleDate.setText(propertyFeature.getSaleDate().isEmpty() ? "Not Sale" : propertyFeature.getSaleDate());
            }else {
                binding.cardDescription.setVisibility(View.GONE);
                binding.descriptionTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_down), null );
            }
        });
    }

    private void initPicture(){
        propertyViewModel.getAllImagesByPropertyIdForDetails(propertyId).observe(getViewLifecycleOwner(), propertyImages -> {
            if (propertyImages == null){
                List<PropertyImage> propertyImageList = new ArrayList<>();
                PropertyImage propertyImage = new PropertyImage();
                Uri uri = Uri.parse("android.resource://com.openclassrooms.realestatemanager/" + R.drawable.no_image);
                propertyImage.setImageUrl(uri.toString());
                propertyImageList.add(propertyImage);
                binding.viewPager.setAdapter(new ImagesPagerAdapter(getContext(), propertyImageList));
            }else {
                binding.viewPager.setAdapter(new ImagesPagerAdapter(getContext(), propertyImages));
            }
        });
    }
}
