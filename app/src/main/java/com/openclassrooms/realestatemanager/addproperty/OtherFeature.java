package com.openclassrooms.realestatemanager.addproperty;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMainFeatureBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentOtherFeatureBinding;
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.*;


public class OtherFeature extends Fragment {

    private FragmentOtherFeatureBinding binding;
    private NavController navController;
    private String[] pointOfInterest = new String[]{"School", "Parc", "Hospital", "Mall", "Shopping", "Medecin"};
    private List<String> dataset = new LinkedList<>(Arrays.asList("Free", "Sale"));
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
        this.onClickBackBtn();
        this.onClickValidateBtn();
        binding.pointInterestTags.setTagList(pointOfInterest);
        binding.pointInterestTags.setTagBackgroundColor(Color.parseColor("#c8a97e"));
        binding.pointInterestTags.setTagTextColor(Color.WHITE);
        binding.statusSpinner.attachDataSource(dataset);
        this.datePicker(binding.dateEditText);
    }

    private void onClickBackBtn(){
        binding.backBtn.setOnClickListener(v -> {
            NavDirections action = OtherFeatureDirections.actionOtherFeatureToAddressFeature();
            navController.navigate(action);
        });
    }

    private void onClickValidateBtn(){
        binding.validateBtn.setOnClickListener(v -> {
            Utils.displayNotification("Property Added", "The property has been added correctly", getContext());
            HomeActivity.navigate(getActivity());
        });
    }

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
