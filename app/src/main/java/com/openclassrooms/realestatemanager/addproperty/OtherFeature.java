package com.openclassrooms.realestatemanager.addproperty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentOtherFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PropertyFeature;
import com.openclassrooms.realestatemanager.utils.AlertDialogUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

public class OtherFeature extends Fragment implements AlertDialogUtils.OnSelectDateListener{

    private FragmentOtherFeatureBinding binding;
    private NavController navController;
    private PropertyViewModel propertyViewModel;
    private String propertyId;
    private String propertyFeatureId;
    private final Bundle bundle = new Bundle();
    private AlertDialogUtils dialogUtils;

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
        propertyId = getArguments().getString(Utils.PROPERTY_ID);
        bundle.putString(Utils.PROPERTY_ID, propertyId);
        Animation fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.otherFeatureLayout.setAnimation(fadeInAnim);
        dialogUtils = new AlertDialogUtils(this);
        this.initPropertyViewModel();
        this.onClickBackBtn();
        this.onClickNextBtn();
        this.initFromFields();
        this.updateLocation();
        binding.dateEditText.setOnClickListener(v -> dialogUtils.datePicker(getContext()));
    }

    /** ****** init form with fields of property if var propertyId get id of property ***** **/
    private void initFromFields(){
        propertyViewModel.getPropertyFeatureById(propertyId).observe(getViewLifecycleOwner(), propertyFeature -> {
            if (propertyFeature != null){
                binding.roomsEditText.setText(String.valueOf(propertyFeature.getNumberOfRooms()));
                binding.bathroomEditText.setText(String.valueOf(propertyFeature.getNumberOfBathrooms()));
                binding.bedroomEditText.setText(String.valueOf(propertyFeature.getNumberOfBedrooms()));
                binding.dateEditText.setText(propertyFeature.getEntranceDate());
                binding.surfaceEditText.setText(String.valueOf(propertyFeature.getPropertySurface()));
                binding.descriptionEditText.setText(propertyFeature.getPropertyDescription());
                propertyFeatureId = propertyFeature.getPropertyFeatureId();
            }else {
                propertyFeatureId = propertyViewModel.getPropertyFeatureId(propertyId);
            }
        });
    }

    /** ****************************************** **/
    /** ** Get property location to google Api ** **/
    /** **************************************** **/

    private void updateLocation(){
        propertyViewModel.getPropertyAddressById(propertyId).observe(getViewLifecycleOwner(), address -> {
            if (address != null){
                String addressCompact = String.valueOf(address.getNumberOfWay()) + ' ' + address.getWay() + ' ' + address.getPostCode();
                propertyViewModel.updateLatLng(propertyId, addressCompact.toLowerCase().replace(" ", "+"));
            }
        });
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** *********************************** **/
    /** ***** Navigation Form Method ***** **/
    /** ********************************* **/

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.addressFeature, true).build();
            navController.navigate(R.id.addressFeature, bundle, navOptions);
        });
    }

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            String numberOfRooms = binding.roomsEditText.getText().toString();
            String numberOfBathrooms = binding.bathroomEditText.getText().toString();
            String numberOfBedRooms = binding.bedroomEditText.getText().toString();
            String entranceDate = binding.dateEditText.getText().toString();
            String propertySurface = binding.surfaceEditText.getText().toString();
            String propertyDescription = binding.descriptionEditText.getText().toString();
            //insert
            propertyViewModel.insertFeatureToProperty(propertyId,
                    initPropertyFeature(numberOfRooms, numberOfBathrooms, numberOfBedRooms, entranceDate, propertySurface , propertyDescription));
            //navigation
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.imagesFeature, true).build();
            navController.navigate(R.id.imagesFeature, bundle, navOptions);

        });
    }

    /** ****** init property feature without null pointer exception ***** **/
    private PropertyFeature initPropertyFeature(String numberOfRooms, String numberOfBathrooms, String numberOfBedRooms,
                                                String entranceDate, String propertySurface , String propertyDescription){
        PropertyFeature propertyFeature = new PropertyFeature();
        if (!numberOfRooms.isEmpty())
            propertyFeature.setNumberOfRooms(Integer.parseInt(numberOfRooms));
        if (!numberOfBathrooms.isEmpty())
            propertyFeature.setNumberOfBathrooms(Integer.parseInt(numberOfBathrooms));
        if (!numberOfBedRooms.isEmpty())
            propertyFeature.setNumberOfBedrooms(Integer.parseInt(numberOfBedRooms));
        if (!entranceDate.isEmpty())
            propertyFeature.setEntranceDate(entranceDate);
        else
            propertyFeature.setEntranceDate(Utils.getTodayDate());
        if (!propertySurface.isEmpty())
            propertyFeature.setPropertySurface(Float.parseFloat(propertySurface));
        propertyFeature.setSoldDate(getString(R.string.not_sale));
        propertyFeature.setPropertyDescription(propertyDescription);
        propertyFeature.setPropertyId(propertyId);
        propertyFeature.setPropertyFeatureId(propertyFeatureId);

        return propertyFeature;
    }

    /** ********************************** **/
    /** *******  Callback Method  ******* **/
    /** ******************************** **/

    @Override
    public void onSelectDate(String date) {
        binding.dateEditText.setText(date);
    }
}
