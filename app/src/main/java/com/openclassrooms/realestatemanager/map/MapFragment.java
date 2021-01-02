package com.openclassrooms.realestatemanager.map;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
import com.bumptech.glide.util.Util;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private FragmentMapBinding binding;
    private PropertyViewModel propertyViewModel;
    private NavController navController;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LatLng latLng;
    private double longitude, latitude;
    private int radius;
    private final float[] results = new float[1];
    private static final String PERMS_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int RC_LOCATION_PERMS = 100;

    public MapFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        navController = Navigation.findNavController(view);
        radius = 5000;
        binding.mapLayout.setAnimation(fadeInAnim);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        this.initPropertyViewModel();
        this.getLocationPermissions();
        binding.focusBtn.setOnClickListener(v -> mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14)));
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** ***************************** **/
    /** ***** Current Location  ***** **/
    /** ***************************** **/

    @AfterPermissionGranted(RC_LOCATION_PERMS)
    public void getLocationPermissions() {
        if (!EasyPermissions.hasPermissions(this.getActivity(), PERMS_LOCATION)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_location_access), RC_LOCATION_PERMS, PERMS_LOCATION);
            return;
        }
        this.getCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /** Get Current User Location **/
    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() >0){
                            int index = locationResult.getLocations().size() -1 ;
                            latitude = locationResult.getLocations().get(index).getLatitude();
                            longitude = locationResult.getLocations().get(index).getLongitude();
                            latLng = new LatLng(latitude, longitude);
                            mapFragment.getMapAsync(MapFragment.this::onMapReady);
                        }else {
                            Utils.showSnackBar(binding.mapLayout, "Unable to find location. Please try later");
                        }
                    }

                    @Override
                    public void onLocationAvailability(LocationAvailability locationAvailability) {
                        super.onLocationAvailability(locationAvailability);
                        Utils.showSnackBar(binding.mapLayout, "Unable to find location. Please try later");
                    }
                }, Looper.getMainLooper());

    }

    private void getProximityProperty(){
        propertyViewModel.getAllProperty().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null){
                for (Property property : properties){
                    Location.distanceBetween(property.getLatitude(), property.getLongitude(), latitude, longitude, results);
                    int distance = (int)results[0];
                    if (distance <= radius){
                        if (property.isSale()){
                            addMarker(new LatLng(property.getLatitude(), property.getLongitude()), property.getPropertyType(),
                                    R.drawable.location_property_sale, property.getPropertyId());
                        }else {
                            addMarker(new LatLng(property.getLatitude(), property.getLongitude()), property.getPropertyType(),
                                    R.drawable.location_property_free, property.getPropertyId());
                        }

                    }
                }
            }
        });
    }

    /** ***************************** **/
    /** ***** Google Map Method ***** **/
    /** ***************************** **/

    private void addMarker(LatLng latLng, String title, int drawableId, String tag){
        Marker myPosition = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(Utils.getBitmap(getResources().getDrawable(drawableId, null)))));
        myPosition.setTag(tag);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!marker.getTag().equals(getString(R.string.my_position))) {
            Bundle bundle = new Bundle();
            bundle.putString("propertyId", (String) marker.getTag());
            navController.navigate(R.id.detailsFragment, bundle);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        mMap.setOnInfoWindowClickListener(this);
        addMarker(latLng, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), R.drawable.location_agent, getString(R.string.my_position));
        this.getProximityProperty();
    }
}
