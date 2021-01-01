package com.openclassrooms.realestatemanager.addproperty;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentImagesFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import com.openclassrooms.realestatemanager.utils.AlertDialogUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static android.app.Activity.RESULT_OK;


public class ImagesFeature extends Fragment implements PropertyImageAdapter.OnDataChange, PropertyImageAdapter.OnClearBtnClicked,
        AlertDialogUtils.OnClickButtonInpuDialog, AlertDialogUtils.OnClickButtonAlertDialog{

    private FragmentImagesFeatureBinding binding;
    private NavController navController;
    private PropertyViewModel propertyViewModel;
    private String propertyId;
    private PropertyImageAdapter adapter;
    private AlertDialogUtils alertDialogUtils;
    private Uri photoUri = null;
    private String uriImageSelected;
    private List<PropertyImage> imageList = new ArrayList<>();
<<<<<<< HEAD
=======
    private Bundle bundle = new Bundle();
>>>>>>> DetailsProperty


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
<<<<<<< HEAD
        propertyId = OtherFeatureArgs.fromBundle(getArguments()).getPropertyId();
=======
        propertyId = getArguments().getString("propertyId");
        bundle.putString("propertyId", propertyId);
>>>>>>> DetailsProperty
        alertDialogUtils = new AlertDialogUtils(this, this);
        this.initPropertyViewModel();
        this.initRecyclerView();
        binding.addImageBtn.setOnClickListener(v -> this.onClickAddFile());
        this.onClickValidateBtn();
        this.onClickBackBtn();
        //this.insertPointOfInterest();
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

    private void insertPointOfInterest(){
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            if (property.getLongitude() != 0.0 && property.getLatitude() != 0.0){
                String location = property.getLatitude() + "," + property.getLongitude();
                propertyViewModel.getProximityPointOfInterest(location, propertyId);
            }
        });
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
        if (requestCode == Utils.RC_CHOOSE_PHOTO || requestCode == Utils.RC_CAMERA_RESULT) {
            if (resultCode == RESULT_OK) {
                uriImageSelected = data.getData().toString();
                inputDialog();
            } else {
                Toast.makeText(this.getActivity(), getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onClickAddFile() {
        choiceDialog();
    }


    @AfterPermissionGranted(Utils.RC_IMAGE_PERMS)
    private void onGallerySelect(){
        if (!EasyPermissions.hasPermissions(this.getContext(), Utils.PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), Utils.RC_IMAGE_PERMS, Utils.PERMS);
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Utils.RC_CHOOSE_PHOTO);
    }

    @AfterPermissionGranted(Utils.RC_CAMERA_PERMS)
    private void onCameraSelect() throws IOException {
        if (!EasyPermissions.hasPermissions(this.getContext(), Utils.PERMS_CAMERA)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_camera_access), Utils.RC_CAMERA_PERMS, Utils.PERMS_CAMERA);
            return;
        }
        Intent mediaChooser =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mediaChooser.resolveActivity(getActivity().getPackageManager()) != null){
            String uuid = UUID.randomUUID().toString();
            File photoDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File photoFile = File.createTempFile(uuid, ".png", photoDir);
            photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    getActivity().getApplicationContext().getPackageName()+".provider", photoFile);
            mediaChooser.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(mediaChooser, Utils.RC_CAMERA_RESULT);
        }
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
            for (PropertyImage propertyImage : imageList){
                propertyViewModel.uploadImageInFirebase(propertyId, propertyImage);
            }
            Utils.displayNotification(getString(R.string.property_added), getString(R.string.added_correctly), getContext());
            HomeActivity.navigate(getActivity());
        });
    }

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
<<<<<<< HEAD
            ImagesFeatureDirections.ActionImagesFeatureToOtherFeature action = ImagesFeatureDirections.actionImagesFeatureToOtherFeature();
            action.setPropertyId(propertyId);
            navController.navigate(action);
=======
            navController.navigate(R.id.otherFeature, bundle);
>>>>>>> DetailsProperty
        });
    }

    @Override
    public void onDataChanged() {}

    @Override
    public void onClickedClearBtn(String propertyImageId) {
        propertyViewModel.deleteImage(propertyId, propertyImageId);
    }

    /** ********************************* **/
    /** ***** Alert Dialog Method  ***** **/
    /** ******************************* **/

    private void inputDialog(){
        alertDialogUtils.showAlertInputDialog(this.getContext(),getString(R.string.description), getString(R.string.description_dialogue_text),
                getString(R.string.add), getString(R.string.cancel),getString(R.string.description), InputType.TYPE_CLASS_TEXT, R.drawable.border_radius_white,
                R.drawable.description, 1);
    }

    private void choiceDialog(){
        alertDialogUtils.showAlertDialog(this.getContext(),getString(R.string.image_choise_dialog_title), getString(R.string.image_choise_dialog_text),
                getString(R.string.gallery), getString(R.string.camera), R.drawable.border_radius_white, R.drawable.camera, 2);
    }

    @Override
    public void onClickedPositiveButtonInputDialog(DialogInterface dialog, TextInputEditText textInputEditText, int dialogIdForSwitch) {
        String imageDescription = textInputEditText.getText().toString();
        PropertyImage propertyImage = new PropertyImage();
        if (photoUri != null){
            propertyImage.setImageUrl(photoUri.toString());
        }else {
            propertyImage.setImageUrl(uriImageSelected);
        }
        propertyImage.setPropertyId(propertyId);
        propertyImage.setImageDescription(imageDescription);
        String propertyImageId = propertyViewModel.getPropertyImageId(propertyId);
        propertyImage.setPropertyImageId(propertyImageId);
        propertyViewModel.insertImageToProperty(propertyId,propertyImage);
        imageList.add(propertyImage);
        photoUri = null;
        dialog.dismiss();
    }

    @Override
    public void onClickedNegativeButtonInputDialog(DialogInterface dialog) { dialog.dismiss();}


    @Override
    public void positiveButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch) {
        onGallerySelect();
        dialog.dismiss();
    }

    @Override
    public void negativeButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch) throws IOException {
        onCameraSelect();
        dialog.dismiss();
    }
}
