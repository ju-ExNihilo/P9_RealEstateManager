package com.openclassrooms.realestatemanager.listview;

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
import com.openclassrooms.realestatemanager.databinding.FragmentMyPropertyListViewBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListViewBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPropertyListView extends Fragment implements AdapterProperty.OnPropertyClicked{

    private FragmentMyPropertyListViewBinding binding;
    private PropertyViewModel propertyViewModel;
    private NavController navController;
    private Animation fadeInAnim;

    public MyPropertyListView newInstance() {
        return new MyPropertyListView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPropertyListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        binding.listLayout.setAnimation(fadeInAnim);
        this.initPropertyViewModel();
        this.initRecyclerView();
        this.getAllProperty();
        this.initFabButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
        Utils.setSelectedNavigationItem(0, navigationView);
    }

    private void initFabButton() {
        binding.floatingActionButton.setOnClickListener(v -> navController.navigate(R.id.mainFeature));
    }

    /**
     * Configure user ViewModel
     **/
    private void initPropertyViewModel() {
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(getViewLifecycleOwner(), this.getContext());
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /**
     * Configure RecyclerView
     **/
    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.listProperty.setLayoutManager(layoutManager);
    }

    private void getAllProperty() {
        propertyViewModel.getAllPropertyFromRoom(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), this::setAdapter);
    }

    private void setAdapter(List<Property> propertyList) {
        if (propertyList != null) {
            binding.listProperty.setAdapter(new AdapterProperty(propertyList, this));
        } else {
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
