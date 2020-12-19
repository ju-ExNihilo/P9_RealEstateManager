package com.openclassrooms.realestatemanager.addproperty;

import android.Manifest;
import android.content.Intent;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentImagesFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;


public class ImagesFeature extends Fragment implements PropertyImageAdapter.OnDataChange, PropertyImageAdapter.OnClearBtnClicked{

    private FragmentImagesFeatureBinding binding;
    private NavController navController;
    private PropertyViewModel propertyViewModel;
    private String propertyId;
    private PropertyImageAdapter adapter;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    public ImagesFeature newInstance() {return new ImagesFeature();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImagesFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        propertyId = OtherFeatureArgs.fromBundle(getArguments()).getPropertyId();
        this.initPropertyViewModel();
        this.initRecyclerView();
        binding.addImageBtn.setOnClickListener(v -> this.onClickAddFile());
        this.onClickValidateBtn();
        this.onClickBackBtn();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
                String uriImageSelected = data.getData().toString();
                PropertyImage propertyImage = new PropertyImage();
                propertyImage.setImageUrl(uriImageSelected);
                propertyImage.setPropertyId(propertyId);
                propertyViewModel.insertImageToProperty(propertyId,propertyImage);
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
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** *********************************** **/
    /** ******* RecyclerView Method ****** **/
    /** ********************************* **/

    private void initRecyclerView(){
        binding.imageList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new PropertyImageAdapter(propertyViewModel.getAllImagesByPropertyId(propertyId), this, this);
        binding.imageList.setAdapter(adapter);
    }

    /** *********************************** **/
    /** ***** Navigation Form Method ***** **/
    /** ********************************* **/

    private void onClickValidateBtn(){
        binding.validateBtn.setOnClickListener(v -> {
            Utils.displayNotification("Property Added", "The property has been added correctly", getContext());
            HomeActivity.navigate(getActivity());
        });
    }

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            ImagesFeatureDirections.ActionImagesFeatureToPointOfInterestFeature action = ImagesFeatureDirections.actionImagesFeatureToPointOfInterestFeature();
            action.setPropertyId(propertyId);
            navController.navigate(action);
        });
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onClickedClearBtn(String propertyImageId) {
        propertyViewModel.deleteImage(propertyId, propertyImageId);
    }
}
