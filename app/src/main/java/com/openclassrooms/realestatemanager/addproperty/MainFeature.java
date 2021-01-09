package com.openclassrooms.realestatemanager.addproperty;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMainFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.AlertDialogUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_APPEND;

public class MainFeature extends Fragment implements AlertDialogUtils.OnClickButtonAlertDialog {

    private FragmentMainFeatureBinding binding;
    private NavController navController;
    private String uriImageSelected;
    private String propertyId = Utils.NULL_STRING;
    private PropertyViewModel propertyViewModel;
    private AlertDialogUtils alertDialogUtils;
    private SharedPreferences preferences;
    private Uri photoUri = null;
    private final List<String> propertyTypeList = new LinkedList<>(Arrays.asList("Flat", "House", "Loft", "manor", "castle", "studio apartment"));

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
        Animation fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.mainFeatureLayout.setAnimation(fadeInAnim);
        preferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE, MODE_APPEND);
        this.initPropertyViewModel();
        this.configureToolbar();
        alertDialogUtils = new AlertDialogUtils(this);
        if (getArguments() != null){
            propertyId = getArguments().getString(Utils.PROPERTY_ID);
        }
        if (!propertyId.equals(Utils.NULL_STRING)){
            this.initFormFields();
        }else {
            propertyId = propertyViewModel.getPropertyId();
        }
        binding.addImageBtn.setOnClickListener(v -> this.onClickAddFile());
        binding.typeSpinner.attachDataSource(propertyTypeList);
        this.onClickNextBtn();
        this.choosePanelBtnLister();
    }

    /** ****** init form with fields of property if var propertyId get id of property ***** **/
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

    /** ****** configure toolbar Method ***** **/
    private void configureToolbar(){
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
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
        if (requestCode == Utils.RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                uriImageSelected = data.getData().toString();
                binding.choosePicPanel.setVisibility(View.VISIBLE);
                Glide.with(binding.choosenPic.getContext())
                        .load(uriImageSelected)
                        .into(binding.choosenPic);

            } else {
                Toast.makeText(this.getActivity(), getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Utils.RC_CAMERA_RESULT) {
            if (resultCode == RESULT_OK) {
                Bitmap bit= BitmapFactory.decodeFile(uriImageSelected);
                binding.choosePicPanel.setVisibility(View.VISIBLE);
                binding.choosenPic.setImageBitmap(bit);

            } else {
                Toast.makeText(this.getActivity(), getString(R.string.toast_title_no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onClickAddFile() {
        choiceDialog();
    }

    private void choosePanelBtnLister(){
        binding.changePicBtn.setOnClickListener(v -> binding.choosePicPanel.setVisibility(View.GONE));
        binding.validatPicBtn.setOnClickListener(v -> {
            binding.choosePicPanel.setVisibility(View.GONE);
            Toast.makeText(this.getActivity(), getString(R.string.toast_title_image_chosen), Toast.LENGTH_SHORT).show();
        });
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
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), Utils.RC_CAMERA_PERMS, Utils.PERMS_CAMERA);
            return;
        }
        Intent mediaChooser =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mediaChooser.resolveActivity(getActivity().getPackageManager()) != null){
            String uuid = UUID.randomUUID().toString();
            File photoDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File photoFile = File.createTempFile(uuid, ".png", photoDir);
            uriImageSelected = photoFile.getAbsolutePath();
            photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    getActivity().getApplicationContext().getPackageName()+".provider", photoFile);
            mediaChooser.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(mediaChooser, Utils.RC_CAMERA_RESULT);
        }
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
                String currency = preferences.getString(Utils.CURRENCY, "USD");
                Property property = new Property();
                property.setPropertyId(propertyId);
                property.setPropertyLocatedCity(locatedCity);
                property.setPropertyPrice(Float.parseFloat(propertyPrice));
                property.setPropertyType(propertyType);
                property.setPropertyPreviewImageUrl(uriImageSelected);
                property.setSold(false);
                property.setInsertCurrency(currency);
                property.setAgentId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                propertyViewModel.createProperty(property);
                if (photoUri != null){
                    propertyViewModel.uploadImageInFirebase(propertyId, photoUri);
                }else {
                    Uri uri = Uri.parse(uriImageSelected);
                    propertyViewModel.uploadImageInFirebase(propertyId, uri);
                }
                Bundle bundle = new Bundle();
                bundle.putString(Utils.PROPERTY_ID, propertyId);
                navController.navigate(R.id.addressFeature, bundle);
            }else {
                Toast.makeText(this.getActivity(), getResources().getString(R.string.need_all_fields), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** ********************************* **/
    /** ***** Alert Dialog Method  ***** **/
    /** ******************************* **/

    private void choiceDialog(){
        alertDialogUtils.showAlertDialog(this.getContext(),getString(R.string.image_choise_dialog_title), getString(R.string.image_choise_dialog_text),
                getString(R.string.gallery), getString(R.string.camera), R.drawable.border_radius_white, R.drawable.camera, 2);
    }

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
