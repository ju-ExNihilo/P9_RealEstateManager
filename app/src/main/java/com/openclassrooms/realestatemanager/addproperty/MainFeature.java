package com.openclassrooms.realestatemanager.addproperty;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMainFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MainFeature extends Fragment {

    private FragmentMainFeatureBinding binding;
    private NavController navController;
    private String uriImageSelected;
    private String propertyId;
    private PropertyViewModel propertyViewModel;
    private List<String> propertyTypeList = new LinkedList<>(Arrays.asList("Flat", "House", "Loft", "manor", "castle", "studio apartment"));
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

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
        this.initPropertyViewModel();
        propertyId = MainFeatureArgs.fromBundle(getArguments()).getPropertyId();
        if (!propertyId.equals("null")){
            this.initFormFields();
        }else {
            propertyId = PropertyDataRepository.getPropertyId();
        }
        binding.addImageBtn.setOnClickListener(v -> this.onClickAddFile());
        binding.typeSpinner.attachDataSource(propertyTypeList);
        this.onClickNextBtn();
    }

    private void initFormFields(){
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            binding.locatedCityEditText.setText(property.getPropertyLocatedCity());
            binding.priceEditText.setText(String.valueOf(property.getPropertyPrice()));
            int index = propertyTypeList.indexOf(property.getPropertyType());
            binding.typeSpinner.setSelectedIndex(index);
            uriImageSelected = property.getPropertyPreviewImageUrl();
            propertyId = property.getPropertyId();
        });
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory();
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** *********************************** **/
    /** ****** Add picture Method  ******* **/
    /** ********************************* **/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                uriImageSelected = data.getData().toString();

                Toast.makeText(this.getActivity(), getString(R.string.toast_title_image_chosen), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this.getActivity(), getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        if (!EasyPermissions.hasPermissions(this.getActivity(), PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    /** *********************************** **/
    /** ****** Validate add Method  ****** **/
    /** ********************************* **/

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            String locatedCity = binding.locatedCityEditText.getText().toString();
            String propertyType = binding.typeSpinner.getSelectedItem().toString();
            String propertyPrice = binding.priceEditText.getText().toString();
            if (!locatedCity.isEmpty() && !propertyType.isEmpty() && !propertyPrice.isEmpty() && uriImageSelected != null){
                Property property = new Property();
                property.setPropertyId(propertyId);
                property.setPropertyLocatedCity(locatedCity);
                property.setPropertyPrice(Float.parseFloat(propertyPrice));
                property.setPropertyType(propertyType);
                property.setPropertyPreviewImageUrl(uriImageSelected);
                property.setAgentId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                propertyViewModel.createProperty(property);
                MainFeatureDirections.ActionMainFeatureToAddressFeature action = MainFeatureDirections.actionMainFeatureToAddressFeature();
                action.setPropertyId(propertyId);
                navController.navigate(action);
            }else {
                Toast.makeText(this.getActivity(), "Please add all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
