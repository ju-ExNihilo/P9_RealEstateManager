package com.openclassrooms.realestatemanager.settings;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSettingsBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.utils.AlertDialogUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.AgentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements AlertDialogUtils.OnClickButtonInputDialog, AlertDialogUtils.OnClickItemListAlertDialog,
        AlertDialogUtils.OnClickItemSpinnerAlertDialog,CheckBoxAdapter.OnItemClicked{

    private FragmentSettingsBinding binding;
    private AgentViewModel agentViewModel;
    private AlertDialogUtils alertDialogUtils;
    private List<String> pointOfInterests;
    private SharedPreferences preferences;
    List<String> pointInterests = Arrays.asList("school", "hospital", "museum", "park", "shopping_mall", "theatre", "airport");
    //, "bank", "police", "doctor", "train_station", "university", "post_office", "pharmacy", "parking"

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.settingsLayout.startAnimation(fadeInAnim);
        preferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE, MODE_PRIVATE);
        alertDialogUtils = new AlertDialogUtils(this, this, this);
        this.configureToolbar();
        this.initAgentViewModel();
        binding.settingPointOfInterest.setOnClickListener(v -> initCheckboxesList());
        binding.settingCurrency.setOnClickListener(v -> currencyChoose());
    }

    private void configureToolbar(){
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
    }

    /** Configure user ViewModel **/
    private void initAgentViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideAgentViewModelFactory();
        agentViewModel = new ViewModelProvider(this, viewModelFactory).get(AgentViewModel.class);
    }

    private void initCheckboxesList(){
        agentViewModel.getCurrentUserData(agentViewModel.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), agent -> {
            alertDialogUtils.showAlertListDialog(getContext(), "Select point of Interest", R.drawable.border_radius_white, R.drawable.location_agent,
                    new CheckBoxAdapter(getContext(), R.layout.item_assignment_dialog_list_layout, pointInterests, agent.getProximityPointOfInterestChoice(),
                            this::onClickedPItem));
            pointOfInterests = agent.getProximityPointOfInterestChoice();
        });
    }

    private void currencyChoose(){
        alertDialogUtils.showAlertSpinnerDialog(getContext(), "Select currency", "Choose the currency of app", "Validate",
                "Cancel", R.drawable.border_radius_white, R.drawable.sale );
    }

    @Override
    public void onClickedPositiveButtonInputDialog(DialogInterface dialog, TextInputEditText textInputEditText, int dialogIdForSwitch) {

    }

    @Override
    public void positiveButtonDialogClicked(DialogInterface dialog) {
        agentViewModel.updatePointOfInterest(agentViewModel.getCurrentUser().getUid(), pointOfInterests);
        dialog.dismiss();
    }

    @Override
    public void onClickedPItem(String item, boolean isChecked) {
        if (isChecked){
            pointOfInterests.add(item);
        }else {
            if (pointOfInterests.contains(item))
                pointOfInterests.remove(item);
        }
    }

    @Override
    public void positiveSpinnerButtonDialogClicked(DialogInterface dialog, Spinner spinner) {
        preferences.edit().putString(Utils.CURRENCY, spinner.getSelectedItem().toString()).commit();
        Log.i("DEBUGGG", spinner.getSelectedItem().toString());
    }

    @Override
    public void negativeSpinnerButtonDialogClicked(DialogInterface dialog) { dialog.dismiss();}
    @Override
    public void negativeButtonDialogClicked(DialogInterface dialog) { dialog.dismiss();}
    @Override
    public void onClickedNegativeButtonInputDialog(DialogInterface dialog) { dialog.dismiss();}

}
