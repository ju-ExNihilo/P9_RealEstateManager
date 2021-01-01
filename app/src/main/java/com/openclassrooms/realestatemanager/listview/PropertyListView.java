package com.openclassrooms.realestatemanager.listview;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListViewBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;


public class PropertyListView extends Fragment implements AdapterProperty.OnPropertyClicked{

    private FragmentPropertyListViewBinding binding;
    private PropertyViewModel propertyViewModel;
    private NavController navController;

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
        this.initPropertyViewModel();
        this.initRecyclerView();
        this.getAllProperty();
        this.initFabButton();
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
