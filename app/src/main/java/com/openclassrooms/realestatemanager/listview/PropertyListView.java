package com.openclassrooms.realestatemanager.listview;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.util.Util;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListViewBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.FeatureForSearch;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_APPEND;


public class PropertyListView extends Fragment implements AdapterProperty.OnPropertyClicked{

    private FragmentPropertyListViewBinding binding;
    private PropertyViewModel propertyViewModel;
    private NavController navController;
    private Animation fadeInAnim;
    private String isMyProperty = "all";
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertyListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        binding.listLayout.setAnimation(fadeInAnim);
        preferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE, MODE_APPEND);
        setHasOptionsMenu(true);
        this.configureToolbar();
        if (getArguments() != null){
            isMyProperty = getArguments().getString(Utils.MY_PROPERTY);
        }
        this.initPropertyViewModel();

        this.initRecyclerView();
        if (!isMyProperty.equals(Utils.MY_PROPERTY)){
            this.getAllProperty();
        }else {
            this.getAllPropertyFromRoom();
            this.initSelectedItem(1);
        }

        this.initFabButton();
        this.searchProperty();
        Utils.datePicker(binding.dateSearchEditText, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMyProperty.equals(Utils.MY_PROPERTY)){
            this.initSelectedItem(0);
        }else {
            this.initSelectedItem(1);
        }
        this.initSeekBarForSurface();
    }

    private void configureToolbar(){
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            animate(binding.dropCard);
            return false;
        });
    }

    private void initSeekBarForSurface(){
        int maxSurface = preferences.getInt(Utils.MAX_SURFACE, 10000);
        binding.surfaceSelect.setCount(maxSurface);
        binding.surfaceSelect.setEnd(maxSurface);
        binding.surfaceTitleSearch.setText(getString(R.string.surface_update_change, binding.surfaceSelect.getStart(), binding.surfaceSelect.getEnd()));
        binding.surfaceSelect.setOnChangeRangeListener((simpleRangeView, i, i1) -> binding.surfaceTitleSearch.setText(getString(R.string.surface_update_change, i, i1)));
    }

    private void searchProperty(){
        binding.searchBtn.setOnClickListener(v -> {
            String locatedCity = binding.townEditText.getText().toString();
            String minPrice = binding.minPriceEditText.getText().toString();
            String maxPrice = binding.maxPriceEditText.getText().toString();
            String dateStart = binding.dateSearchEditText.getText().toString();
            String numberOfPics = binding.nbrPicEditText.getText().toString();
            String pointOfInterest = binding.pointOfInterestSearchEditText.getText().toString();
            List<String> finalPointOfInterest = (pointOfInterest.isEmpty()) ? Arrays.asList(getString(R.string.empty_string)) : Arrays.asList(pointOfInterest.split(" "));

            locatedCity =  (locatedCity.isEmpty()) ? getString(R.string.empty_string) : locatedCity;
            dateStart =  (dateStart.isEmpty()) ? getString(R.string.default_date) : dateStart;
            float finalMinPrice =  (minPrice.isEmpty()) ? 0 : Float.parseFloat(minPrice);
            float finalMaxPrice =  (maxPrice.isEmpty()) ? 0 : Float.parseFloat(maxPrice);
            float minSurface = (float) binding.surfaceSelect.getStart();
            float maxSurface = (float) binding.surfaceSelect.getEnd();
            int finalNumberOfPics =  (numberOfPics.isEmpty()) ? 0 : Integer.parseInt(numberOfPics);

            propertyViewModel.searchMethod(locatedCity, finalMinPrice, finalMaxPrice, minSurface, maxSurface, dateStart, finalPointOfInterest, finalNumberOfPics)
                    .observe(getViewLifecycleOwner(), this::setAdapter);
            animate(binding.dropCard);
        });
    }

    private void animate(View view){
        if (view.getVisibility() == View.VISIBLE){
            binding.dropDownMenu.setVisibility(View.INVISIBLE);
            Utils.collapse(view);
        }else {
            Utils.expand(view, binding.dropDownMenu);
        }
    }

    private void initSelectedItem(int index){
        NavigationView navigationView = ((AppCompatActivity)getActivity()).findViewById(R.id.nav_view);
        Utils.setSelectedNavigationItem(index, navigationView);
    }

    private void initFabButton(){
        binding.floatingActionButton.setOnClickListener(v -> navController.navigate(R.id.mainFeature));
    }

    /** Configure user ViewModel **/
    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** Configure RecyclerView **/
    private void initRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.listProperty.setLayoutManager(layoutManager);
    }

    private void getAllProperty(){
        propertyViewModel.getAllProperty().observe(getViewLifecycleOwner(), this::setAdapter);
    }

    private void cleanDb(){
        propertyViewModel.getAllPropertyFromRoom().observe(getViewLifecycleOwner(),properties -> {
            for (Property property : properties){
                propertyViewModel.deleteFromRoom(property.getPropertyId());
            }
        });
    }

    private void getAllPropertyFromRoom(){
        propertyViewModel.getAllPropertyFromRoom().observe(getViewLifecycleOwner(), this::setAdapter);
    }

    private void setAdapter(List<Property> propertyList){
        if (propertyList != null){
            binding.listProperty.setAdapter(new AdapterProperty(propertyList, this));
        }else {
            Utils.showSnackBar(binding.listLayout, getString(R.string.sorry_no_data));
        }
    }

    @Override
    public void onClickedProperty(String propertyId) {
        Bundle bundle = new Bundle();
        bundle.putString(Utils.PROPERTY_ID, propertyId);
        navController.navigate(R.id.detailsFragment, bundle);
    }
}
