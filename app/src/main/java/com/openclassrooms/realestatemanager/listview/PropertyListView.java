package com.openclassrooms.realestatemanager.listview;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListViewBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;


public class PropertyListView extends Fragment implements AdapterProperty.OnPropertyClicked{

    private FragmentPropertyListViewBinding binding;
    private PropertyViewModel propertyViewModel;
    private NavController navController;
    private Animation fadeInAnim;
    private String isMyProperty = "all";

    public PropertyListView newInstance() {
        return new PropertyListView();
    }


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
        if (getArguments() != null){
            isMyProperty = getArguments().getString("MyProperty");
        }
        this.initPropertyViewModel();

        this.initRecyclerView();
        if (!isMyProperty.equals("MyProperty")){
            this.getAllProperty();
        }else {
            this.getAllPropertyFromRoom();
            this.initSelectedItem(1);
        }

        this.initFabButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMyProperty.equals("MyProperty")){
            this.initSelectedItem(0);
        }else {
            this.initSelectedItem(1);
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
        propertyViewModel.getAllPropertyFromRoom("kk").observe(getViewLifecycleOwner(),properties -> {
            for (Property property : properties){
                propertyViewModel.deleteFromRoom(property.getPropertyId());
            }
        });
    }

    private void getAllPropertyFromRoom(){
        propertyViewModel.getAllPropertyFromRoom(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), this::setAdapter);
    }

    private void setAdapter(List<Property> propertyList){
        if (propertyList != null){
            binding.listProperty.setAdapter(new AdapterProperty(propertyList, this));
        }else {
            Log.i("DEBUGGGG", "no Data");
        }
    }

    @Override
    public void onClickedProperty(String propertyId) {
        Bundle bundle = new Bundle();
        bundle.putString("propertyId", propertyId);
        navController.navigate(R.id.detailsFragment, bundle);
    }
}
