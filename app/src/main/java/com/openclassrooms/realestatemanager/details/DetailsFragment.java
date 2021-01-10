package com.openclassrooms.realestatemanager.details;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import com.openclassrooms.realestatemanager.utils.AlertDialogUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.AgentViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_APPEND;

public class DetailsFragment extends Fragment implements OnMapReadyCallback , AlertDialogUtils.OnSelectDateListener{

    private FragmentDetailsBinding binding;
    private NavController navController;
    private String propertyId;
    private PropertyViewModel propertyViewModel;
    private AgentViewModel agentViewModel;
    private GoogleMap mMap;
    private LatLng latLng;
    private SupportMapFragment mapFragment;
    private Animation fadeInAnim;
    private SharedPreferences preferences;
    private AlertDialogUtils dialogUtils;

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
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.detailsView.setAnimation(fadeInAnim);
        if (getArguments() != null){
            propertyId = getArguments().getString(Utils.PROPERTY_ID);
            preferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE, MODE_APPEND);
            dialogUtils = new AlertDialogUtils(this);
            this.configureToolbar();
            this.initAgentViewModel();
            this.initPropertyViewModel();
            this.initMainFeature();
            this.initPicture();
            this.initAddress();
            this.initOtherFeature();
            this.accordionTransition();
            this.initPointOfInterest();
            this.configureBottomNavigationView();
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            binding.defaultImage.setVisibility(View.GONE);
        }else {
            binding.defaultImage.setVisibility(View.VISIBLE);
        }

    }

    /** Configure toolbar **/
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

    private void initAgentViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideAgentViewModelFactory();
        agentViewModel = new ViewModelProvider(this, viewModelFactory).get(AgentViewModel.class);
    }

    /** *********************************** **/
    /** ******* init Fields Method ******* **/
    /** ********************************* **/

    private void initMainFeature(){
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            boolean isAgent = property.getAgentId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
            binding.typeProperty.setText(property.getPropertyType());
            binding.locationProperty.setText(property.getPropertyLocatedCity());
            if (property.isSold())
                this.isSold();
            binding.navigationBar.setVisibility(isAgent ? View.VISIBLE : View.GONE);
            binding.otherFeatureStatus.setText(property.isSold() ? getString(R.string.sale) : getString(R.string.free));
            String currency = preferences.getString(Utils.CURRENCY, "USD");
            binding.priceProperty.setText(Utils.formatPrice(property.getPropertyPrice(), currency, property.getInsertCurrency()));
            this.initAgentName(property.getAgentId());
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

    private void initAgentName(String agentId){
        agentViewModel.getCurrentUserData(agentId).observe(getViewLifecycleOwner(), agent -> {
            binding.otherFeatureAgent.setText(agent.getAgentName());
        });
    }

    private void initPointOfInterest(){
        propertyViewModel.getPointOfInterestById(propertyId).observe(getViewLifecycleOwner(), pointOfInterests -> {
            if (pointOfInterests != null){
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
                    binding.descriptionTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.drawable.arrow_down, null), null );
                }
                binding.descriptionProperty.setText(propertyFeature.getPropertyDescription());
                binding.otherFeatureSurface.setText(propertyFeature.getPropertySurface() + getString(R.string.metric));
                binding.otherFeatureNumberRoom.setText(String.valueOf(propertyFeature.getNumberOfRooms()));
                binding.otherFeatureBedroom.setText(String.valueOf(propertyFeature.getNumberOfBedrooms()));
                binding.otherFeatureBathroom.setText(String.valueOf(propertyFeature.getNumberOfBathrooms()));
                binding.otherFeatureEntranceDate.setText(propertyFeature.getEntranceDate());
                binding.otherFeatureSaleDate.setText(propertyFeature.getSoldDate().isEmpty() ? getString(R.string.not_sale) : propertyFeature.getSoldDate());
            }else {
                binding.cardDescription.setVisibility(View.GONE);
                binding.descriptionTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.drawable.arrow_down, null), null );
            }
        });
    }

    private void initPicture(){
        propertyViewModel.getAllImagesByPropertyIdForDetails(propertyId).observe(getViewLifecycleOwner(), propertyImages -> {
            if (propertyImages == null){
                List<PropertyImage> propertyImageList = new ArrayList<>();
                PropertyImage propertyImage = new PropertyImage();
                propertyImage.setImageUrl(getString(R.string.default_image_url) + R.drawable.no_image);
                propertyImageList.add(propertyImage);
                binding.viewPager.setAdapter(new ImagesPagerAdapter(getContext(), propertyImageList));
            }else {
                binding.viewPager.setAdapter(new ImagesPagerAdapter(getContext(), propertyImages));
            }
        });
    }

    /** *********************************** **/
    /** ********* init Map Method ******** **/
    /** ********************************* **/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            if (property.getLatitude() != 0 && property.getLongitude() != 0){
                latLng = new LatLng(property.getLatitude(), property.getLongitude());
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                mMap.addMarker(new MarkerOptions().position(latLng));
            }else {
                binding.noAddressText.setVisibility(View.VISIBLE);
                binding.noAddressMap.setVisibility(View.GONE);
            }
        });

    }

    /** ********************************** **/
    /** ***** NavigationBar Method  ***** **/
    /** ******************************** **/

    private void configureBottomNavigationView(){
        binding.navigationBar.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case R.id.update_details:
                    updateNavigation();
                    break;
                case R.id.set_sale:
                    dialogUtils.datePicker(getContext());
                    break;
            }
            return true;
        });
    }

    private void updateNavigation(){
        Bundle bundle = new Bundle();
        bundle.putString(Utils.PROPERTY_ID, propertyId);
        navController.navigate(R.id.mainFeature, bundle);
    }

    private void setSaleProperty(String saleDate){
        if (saleDate != null){
            propertyViewModel.getPropertyFeatureById(propertyId).observe(getViewLifecycleOwner(), propertyFeature -> {
                if (propertyFeature != null){
                    propertyViewModel.propertySale(propertyId, propertyFeature.getPropertyFeatureId(), saleDate);
                    this.initOtherFeature();
                    this.initMainFeature();
                    Utils.showSnackBar(binding.detailsView, getString(R.string.congratulation));
                    this.isSold();
                }else {
                    Utils.showSnackBar(binding.detailsView, getString(R.string.set_property));
                }
            });
        }else {
            Utils.showSnackBar(binding.detailsView, getString(R.string.select_date));
        }
    }

    /** ********************************** **/
    /** ********* Utils Method  ********* **/
    /** ******************************** **/

    private void runConfetti(){
        binding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, binding.viewKonfetti.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);
    }

    private void isSold(){
        binding.navigationBar.getMenu().getItem(1).setTitle(R.string.sale_maj);
        binding.navigationBar.getMenu().getItem(1);
        this.runConfetti();
    }

    private void accordionTransition(){
        binding.addressTitle.setOnClickListener(v -> Utils.animate(binding.cardAddress, binding.addressTitle, getContext()));
        binding.mainFeatureTitle.setOnClickListener(v -> Utils.animate(binding.cardMainFeature, binding.mainFeatureTitle, getContext()));
        binding.descriptionTitle.setOnClickListener(v -> Utils.animate(binding.cardDescription, binding.descriptionTitle, getContext()));
        binding.otherFeatureTitle.setOnClickListener(v -> Utils.animate(binding.otherFeatureCard, binding.otherFeatureTitle, getContext()));
        binding.pointOfInterestTitle.setOnClickListener(v -> Utils.animate(binding.pointOfInterestCard, binding.pointOfInterestTitle, getContext()));
        //Map
        binding.geoLocalisationTitle.setOnClickListener(v -> binding.mapPanel.setVisibility(View.VISIBLE));
        binding.closeMapPanel.setOnClickListener(v -> binding.mapPanel.setVisibility(View.GONE));
    }

    /** ********************************** **/
    /** **** Dialog Callback Method  **** **/
    /** ******************************** **/

    @Override
    public void onSelectDate(String date) {
        setSaleProperty(date);
    }
}
