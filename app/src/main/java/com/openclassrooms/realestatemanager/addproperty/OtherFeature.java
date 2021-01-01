package com.openclassrooms.realestatemanager.addproperty;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PropertyFeature;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.*;


public class OtherFeature extends Fragment {

    private FragmentOtherFeatureBinding binding;
    private NavController navController;
    private PropertyViewModel propertyViewModel;
    private String propertyId;
    private String propertyFeatureId;
<<<<<<< HEAD

    private List<String> statusItemsList = new LinkedList<>(Arrays.asList("Free", "Sale"));
=======
    private Bundle bundle = new Bundle();
>>>>>>> DetailsProperty
    private DatePickerDialog pickerDate;

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
<<<<<<< HEAD
        propertyId = OtherFeatureArgs.fromBundle(getArguments()).getPropertyId();
        this.initPropertyViewModel();
        this.onClickBackBtn();
        this.onClickNextBtn();
        binding.statusSpinner.attachDataSource(statusItemsList);
=======
        propertyId = getArguments().getString("propertyId");
        bundle.putString("propertyId", propertyId);
        this.initPropertyViewModel();
        this.onClickBackBtn();
        this.onClickNextBtn();
>>>>>>> DetailsProperty
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
<<<<<<< HEAD
                if (propertyFeature.isSale()){
                    binding.statusSpinner.setSelectedIndex(1);
                }else {
                    binding.statusSpinner.setSelectedIndex(0);
                }
=======
>>>>>>> DetailsProperty
                propertyFeatureId = propertyFeature.getPropertyFeatureId();
            }else {
                propertyFeatureId = propertyViewModel.getPropertyFeatureId(propertyId);
            }
        });
    }

    private void updateLocation(){
        propertyViewModel.getPropertyAddressById(propertyId).observe(getViewLifecycleOwner(), address -> {
<<<<<<< HEAD
            String addressCompact = String.valueOf(address.getNumberOfWay()) + ' ' + address.getWay() + ' ' + address.getPostCode();
            propertyViewModel.updateLatLng(propertyId, addressCompact.toLowerCase().replace(" ", "+"));
=======
            if (address != null){
                String addressCompact = String.valueOf(address.getNumberOfWay()) + ' ' + address.getWay() + ' ' + address.getPostCode();
                propertyViewModel.updateLatLng(propertyId, addressCompact.toLowerCase().replace(" ", "+"));
            }
>>>>>>> DetailsProperty
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
<<<<<<< HEAD
            OtherFeatureDirections.ActionOtherFeatureToAddressFeature action = OtherFeatureDirections.actionOtherFeatureToAddressFeature();
            action.setPropertyId(propertyId);
            navController.navigate(action);
=======
            navController.navigate(R.id.addressFeature, bundle);
>>>>>>> DetailsProperty
        });
    }

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            String numberOfRooms = binding.roomsEditText.getText().toString();
            String numberOfBathrooms = binding.bathroomsEditText.getText().toString();
            String numberOfBedRooms = binding.bedroomsEditText.getText().toString();
            String entranceDate = binding.dateEditText.getText().toString();
<<<<<<< HEAD
            String isSale = binding.statusSpinner.getSelectedItem().toString();
=======
>>>>>>> DetailsProperty
            String propertySurface = binding.surfaceEditText.getText().toString();
            String propertyDescription = binding.descriptionEditText.getText().toString();
            //insert
            propertyViewModel.insertFeatureToProperty(propertyId,
<<<<<<< HEAD
                    initPropertyFeature(numberOfRooms, numberOfBathrooms, numberOfBedRooms, entranceDate, isSale, propertySurface , propertyDescription));
            //navigation
            OtherFeatureDirections.ActionOtherFeatureToImagesFeature action = OtherFeatureDirections.actionOtherFeatureToImagesFeature();
            action.setPropertyId(propertyId);
            navController.navigate(action);
=======
                    initPropertyFeature(numberOfRooms, numberOfBathrooms, numberOfBedRooms, entranceDate, propertySurface , propertyDescription));
            //navigation
            navController.navigate(R.id.imagesFeature, bundle);
>>>>>>> DetailsProperty

        });
    }

    private PropertyFeature initPropertyFeature(String numberOfRooms, String numberOfBathrooms, String numberOfBedRooms,
<<<<<<< HEAD
                                                String entranceDate, String isSale, String propertySurface , String propertyDescription){
=======
                                                String entranceDate, String propertySurface , String propertyDescription){
>>>>>>> DetailsProperty
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
<<<<<<< HEAD
        propertyFeature.setSale(!isSale.equals("Free"));
=======
        propertyFeature.setSaleDate("Not Sale");
>>>>>>> DetailsProperty
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
