package com.openclassrooms.realestatemanager.details;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDetailsBinding binding;
    private NavController navController;
    private String propertyId;
    private PropertyViewModel propertyViewModel;
    private GoogleMap mMap;
    private LatLng latLng;
    private SupportMapFragment mapFragment;
    private DatePickerDialog pickerDate;
    private Animation fadeInAnim;

    public DetailsFragment() {}


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
        propertyId = getArguments().getString("propertyId");
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.detailsView.setAnimation(fadeInAnim);
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
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** *********************************** **/
    /** ******* init Fields Method ******* **/
    /** ********************************* **/

    private void initMainFeature(){
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            boolean isAgent = property.getAgentId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
            binding.typeProperty.setText(property.getPropertyType());
            binding.locationProperty.setText(property.getPropertyLocatedCity());
            if (property.isSale())
                this.isSale();
            binding.navigationBar.setVisibility(isAgent ? View.VISIBLE : View.GONE);
            binding.otherFeatureStatus.setText(property.isSale() ? getString(R.string.sale) : getString(R.string.free));
            binding.priceProperty.setText(Utils.formatPrice(property.getPropertyPrice(), getString(R.string.usd)));
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
                binding.otherFeatureSaleDate.setText(propertyFeature.getSaleDate().isEmpty() ? getString(R.string.not_sale) : propertyFeature.getSaleDate());
            }else {
                binding.cardDescription.setVisibility(View.GONE);
                binding.descriptionTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.drawable.arrow_down, null), null );
            }
        });
    }

    private void initPicture(){
        propertyViewModel.getAllImagesByPropertyIdForDetails(propertyId).observe(getViewLifecycleOwner(), propertyImages -> {
            binding.viewPager.setAdapter(new ImagesPagerAdapter(getContext(), propertyImages));
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
                    this.datePicker();
                    break;
            }
            return true;
        });
    }

    private void updateNavigation(){
        Bundle bundle = new Bundle();
        bundle.putString("propertyId", propertyId);
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
                    this.isSale();
                }else {
                    Utils.showSnackBar(binding.detailsView, getString(R.string.set_property));
                }
            });
        }else {
            Utils.showSnackBar(binding.detailsView, getString(R.string.select_date));
        }
    }

    private void datePicker(){
        propertyViewModel.getAPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            if (!property.isSale()){
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                pickerDate = new DatePickerDialog(this.getActivity(),R.style.myDatePickerStyle,(view, year1, monthOfYear, dayOfMonth) -> {
                    String saleDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    setSaleProperty(saleDate);
                }, year, month, day);
                pickerDate.show();
                pickerDate.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#c8a97e"));
                pickerDate.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#c8a97e"));
            }
        });

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

    private void isSale(){
        binding.navigationBar.getMenu().getItem(1).setTitle(R.string.sale_maj);
        binding.navigationBar.getMenu().getItem(1);
        this.runConfetti();
    }

    private void accordionTransition(){
        binding.addressTitle.setOnClickListener(v -> animate(binding.cardAddress, binding.addressTitle));
        binding.mainFeatureTitle.setOnClickListener(v -> animate(binding.cardMainFeature, binding.mainFeatureTitle));
        binding.descriptionTitle.setOnClickListener(v -> animate(binding.cardDescription, binding.descriptionTitle));
        binding.otherFeatureTitle.setOnClickListener(v -> animate(binding.otherFeatureCard, binding.otherFeatureTitle));
        binding.pointOfInterestTitle.setOnClickListener(v -> animate(binding.pointOfInterestCard, binding.pointOfInterestTitle));
        //Map
        binding.geoLocalisationTitle.setOnClickListener(v -> binding.mapPanel.setVisibility(View.VISIBLE));
        binding.closeMapPanel.setOnClickListener(v -> binding.mapPanel.setVisibility(View.GONE));
    }

    private void animate(View card, TextView title){
        if (card.getVisibility() == View.VISIBLE){
            Utils.collapse(card);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.arrow_down, null), null );
        }else {
            Utils.expand(card);
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.arrow_up, null), null);
        }
    }

}
