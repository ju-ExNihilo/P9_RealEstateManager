package com.openclassrooms.realestatemanager.addproperty;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentOtherFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PropertyFeature;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.*;


public class OtherFeature extends Fragment {

    private FragmentOtherFeatureBinding binding;
    private NavController navController;
    private PropertyViewModel propertyViewModel;
    private String propertyId;
    private String propertyFeatureId;
    private Bundle bundle = new Bundle();
    private DatePickerDialog pickerDate;
    private Animation fadeInAnim;

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
        propertyId = getArguments().getString("propertyId");
        bundle.putString("propertyId", propertyId);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.otherFeatureLayout.setAnimation(fadeInAnim);
        this.initPropertyViewModel();
        this.onClickBackBtn();
        this.onClickNextBtn();
        this.datePicker(binding.dateEditText);
        this.initFromFields();
        this.updateLocation();
    }

    private void initFromFields(){
        propertyViewModel.getPropertyFeatureById(propertyId).observe(getViewLifecycleOwner(), propertyFeature -> {
            if (propertyFeature != null){
                binding.roomsEditText.setText(String.valueOf(propertyFeature.getNumberOfRooms()));
                binding.bathroomsEditText.setText(String.valueOf(propertyFeature.getNumberOfBathrooms()));
                binding.bedroomsEditText.setText(String.valueOf(propertyFeature.getNumberOfBedrooms()));
                binding.dateEditText.setText(propertyFeature.getEntranceDate());
                binding.surfaceEditText.setText(String.valueOf(propertyFeature.getPropertySurface()));
                binding.descriptionEditText.setText(propertyFeature.getPropertyDescription());
                propertyFeatureId = propertyFeature.getPropertyFeatureId();
            }else {
                propertyFeatureId = propertyViewModel.getPropertyFeatureId(propertyId);
            }
        });
    }

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
            navController.navigate(R.id.addressFeature, bundle);
        });
    }

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            String numberOfRooms = binding.roomsEditText.getText().toString();
            String numberOfBathrooms = binding.bathroomsEditText.getText().toString();
            String numberOfBedRooms = binding.bedroomsEditText.getText().toString();
            String entranceDate = binding.dateEditText.getText().toString();
            String propertySurface = binding.surfaceEditText.getText().toString();
            String propertyDescription = binding.descriptionEditText.getText().toString();
            //insert
            propertyViewModel.insertFeatureToProperty(propertyId,
                    initPropertyFeature(numberOfRooms, numberOfBathrooms, numberOfBedRooms, entranceDate, propertySurface , propertyDescription));
            //navigation
            navController.navigate(R.id.imagesFeature, bundle);

        });
    }

    private PropertyFeature initPropertyFeature(String numberOfRooms, String numberOfBathrooms, String numberOfBedRooms,
                                                String entranceDate, String propertySurface , String propertyDescription){
        PropertyFeature propertyFeature = new PropertyFeature();
        if (!numberOfRooms.isEmpty())
            propertyFeature.setNumberOfRooms(Integer.parseInt(numberOfRooms));
        if (!numberOfBathrooms.isEmpty())
            propertyFeature.setNumberOfBathrooms(Integer.parseInt(numberOfBathrooms));
        if (!numberOfBedRooms.isEmpty())
            propertyFeature.setNumberOfBedrooms(Integer.parseInt(numberOfBedRooms));
        propertyFeature.setEntranceDate(entranceDate);
        if (!propertySurface.isEmpty())
            propertyFeature.setPropertySurface(Float.parseFloat(propertySurface));
        propertyFeature.setSoldDate("Not Sale");
        propertyFeature.setPropertyDescription(propertyDescription);
        propertyFeature.setPropertyId(propertyId);
        propertyFeature.setPropertyFeatureId(propertyFeatureId);

        return propertyFeature;
    }

    /** *********************************** **/
    /** *********** Utils Method ********* **/
    /** ********************************* **/

    private void datePicker(TextInputEditText date){
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            pickerDate = new DatePickerDialog(this.getActivity(),R.style.myDatePickerStyle,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }, year, month, day);
            pickerDate.show();
            pickerDate.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#c8a97e"));
            pickerDate.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#c8a97e"));
        });
    }
}
