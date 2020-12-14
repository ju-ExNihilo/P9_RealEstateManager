package com.openclassrooms.realestatemanager.addproperty;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.openclassrooms.realestatemanager.databinding.FragmentPointOfInterestFeatureBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;
import com.skyhope.materialtagview.TagView;
import com.skyhope.materialtagview.model.TagModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointOfInterestFeature extends Fragment {

    private FragmentPointOfInterestFeatureBinding binding;
    private NavController navController;
    private PropertyViewModel propertyViewModel;
    private String propertyId;
    private final List<String> pointOfInterestItemsList = new ArrayList<>(Arrays.asList("School", "Parc", "Hospital", "Mall", "Shopping", "Medecin"));


    public PointOfInterestFeature newInstance() {return new PointOfInterestFeature();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPointOfInterestFeatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        propertyId = OtherFeatureArgs.fromBundle(getArguments()).getPropertyId();
        this.initPropertyViewModel();
        this.initFormFields();
        binding.pointInterestTags.setTagList(pointOfInterestItemsList);
        binding.pointInterestTags.setTagBackgroundColor(Color.parseColor("#c8a97e"));
        binding.pointInterestTags.setTagTextColor(Color.WHITE);
        this.onClickBackBtn();
        this.onClickNextBtn();
    }

    private void initFormFields(){
        propertyViewModel.getPointOfInterestById(propertyId).observe(getViewLifecycleOwner(), pointOfInterests -> {
            if (pointOfInterests != null){
                for (PointOfInterest pointOfInterest : pointOfInterests){
                    if (pointOfInterestItemsList.contains(pointOfInterest.getGetPointOfInterestName())){
                        int i = pointOfInterestItemsList.indexOf(pointOfInterest.getGetPointOfInterestName());
                        binding.pointInterestTags.onGetSelectTag(i, pointOfInterest.getGetPointOfInterestName());
                    }else {
                        binding.customEditText.setText(pointOfInterest.getGetPointOfInterestName());
                    }
                }
            }
        });
        propertyViewModel.resetPointOfInterest(propertyId);
    }

    /** *********************************** **/
    /** ****** init ViewModel Method ***** **/
    /** ********************************* **/

    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory();
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    /** *********************************** **/
    /** ***** Navigation Form Method ***** **/
    /** ********************************* **/

    private void savePointOfInterest(){
        List<TagModel> pointOfInterestSelected = binding.pointInterestTags.getSelectedTags();
        TagModel tagModelCustom = new TagModel();
        String customPointOfInterest = binding.customEditText.getText().toString();
        if (!customPointOfInterest.isEmpty()){
            tagModelCustom.setTagText(customPointOfInterest);
            pointOfInterestSelected.add(tagModelCustom);
        }
        for (TagModel tagModel : pointOfInterestSelected){
            PointOfInterest pointOfInterest = new PointOfInterest();
            pointOfInterest.setPropertyId(propertyId);
            pointOfInterest.setGetPointOfInterestName(tagModel.getTagText());
            pointOfInterest.setPointOfInterestId(tagModel.getTagText() + "Id");
            propertyViewModel.insertPointOfInterestToProperty(propertyId, pointOfInterest);
        }
    }

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            //Insert
            this.savePointOfInterest();
            //Navigation
            PointOfInterestFeatureDirections.ActionPointOfInterestFeatureToOtherFeature action =
                    PointOfInterestFeatureDirections.actionPointOfInterestFeatureToOtherFeature();
            action.setPropertyId(propertyId);
            navController.navigate(action);
        });
    }

    private void onClickNextBtn(){
        binding.nextBtn.setOnClickListener(v -> {
            //Insert
            this.savePointOfInterest();
            //Navigation
            PointOfInterestFeatureDirections.ActionPointOfInterestFeatureToImagesFeature action =
                    PointOfInterestFeatureDirections.actionPointOfInterestFeatureToImagesFeature();
            action.setPropertyId(propertyId);
            navController.navigate(action);
        });
    }
}
