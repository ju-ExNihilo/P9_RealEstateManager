package com.openclassrooms.realestatemanager.addproperty;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddressFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import fr.juju.googlemaplibrary.model.FinalPlace;
import fr.juju.googlemaplibrary.repository.GooglePlaceRepository;

public class AddressFeature extends Fragment {

    private FragmentAddressFeatureBinding binding;
    private NavController navController;
    private String propertyId;
    private String addressId;
    private PropertyViewModel propertyViewModel;
    private Bundle bundle = new Bundle();
    private Animation fadeInAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        propertyId = getArguments().getString(Utils.PROPERTY_ID);
        bundle.putString(Utils.PROPERTY_ID, propertyId);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.addressFeatureLayout.setAnimation(fadeInAnim);
        this.initPropertyViewModel();
        this.initFormFields();
        this.onClickBackBtn();
        this.onClickNextBtn();
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** *********************************** **/
    /** ***** Navigation Form Method ***** **/
    /** ********************************* **/

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            navController.navigate(R.id.mainFeature, bundle);
        });
    }

    private void initFormFields(){
        propertyViewModel.getPropertyAddressById(propertyId).observe(getViewLifecycleOwner(), address -> {
            if (address != null){
                binding.numberAddressEditText.setText(String.valueOf(address.getNumberOfWay()));
                binding.wayAddressEditText.setText(address.getWay());
                binding.postcodeAddressEditText.setText(String.valueOf(address.getPostCode()));
                binding.additionalAddressFieldsEditText.setText(address.getAdditionalAddressField());
                addressId = address.getAddressId();
            }else {
                addressId = propertyViewModel.getAddressId(propertyId);
            }
        });
    }

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            String numberOfWay = binding.numberAddressEditText.getText().toString();
            String way = binding.wayAddressEditText.getText().toString();
            String postCode = binding.postcodeAddressEditText.getText().toString();
            String additionalAddressField = binding.additionalAddressFieldsEditText.getText().toString();
            if (!numberOfWay.isEmpty() && !way.isEmpty() && !postCode.isEmpty()){
                Address address = new Address();
                address.setAddressId(addressId);
                address.setNumberOfWay(Integer.parseInt(numberOfWay));
                address.setWay(way);
                address.setPostCode(Integer.parseInt(postCode));
                address.setAdditionalAddressField(additionalAddressField);
                address.setPropertyId(propertyId);
                //insert
                propertyViewModel.insetAddressToProperty(propertyId, address);
                //navigation
                navController.navigate(R.id.otherFeature, bundle);
            }else {
                Toast.makeText(this.getActivity(), getResources().getString(R.string.need_all_fields), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
