package com.openclassrooms.realestatemanager.settings;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSettingsBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.utils.AlertDialogUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.AgentViewModel;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment implements AlertDialogUtils.OnClickButtonInputDialog, AlertDialogUtils.OnClickItemListAlertDialog,
        AlertDialogUtils.OnClickItemSpinnerAlertDialog,CheckBoxAdapter.OnItemClicked{

    private FragmentSettingsBinding binding;
    private AgentViewModel agentViewModel;
    private AlertDialogUtils alertDialogUtils;
    private List<String> pointOfInterests;
    private SharedPreferences preferences;
    List<String> pointInterests = Arrays.asList("school", "hospital", "museum", "park", "shopping_mall", "theatre",
            "airport", "bank", "police", "doctor", "train_station", "university", "post_office", "pharmacy", "parking");

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
        binding.settingScope.setOnClickListener(v -> initLocalisationScope(getString(R.string.scope_dialog_title)));
    }

    /** Configure toolbar **/
    private void configureToolbar(){
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
    }

    /** Configure user ViewModel **/
    private void initAgentViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideAgentViewModelFactory();
        agentViewModel = new ViewModelProvider(this, viewModelFactory).get(AgentViewModel.class);
    }

    /** ********************************** **/
    /** ******** Dialog  Method  ******** **/
    /** ******************************** **/

    private void initCheckboxesList(){
        agentViewModel.getCurrentUserData(agentViewModel.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), agent -> {
            alertDialogUtils.showAlertListDialog(getContext(), getString(R.string.point_of_interest_dialog_title), R.drawable.border_radius_white, R.drawable.location_agent,
                    new CheckBoxAdapter(getContext(), R.layout.item_assignment_dialog_list_layout, pointInterests, agent.getProximityPointOfInterestChoice(),
                            this::onClickedPItem));
            pointOfInterests = agent.getProximityPointOfInterestChoice();
        });
    }

    private void currencyChoose(){
        alertDialogUtils.showAlertSpinnerDialog(getContext(), getString(R.string.select_currency), getString(R.string.select_currency_message),
                getString(R.string.validate),getString(R.string.cancel), R.drawable.border_radius_white, R.drawable.sale );
    }

    private void initLocalisationScope(String message){
        alertDialogUtils.showAlertInputDialog(getContext(), getString(R.string.update_scope), message, getString(R.string.validate),getString(R.string.cancel),
                getString(R.string.scope), InputType.TYPE_CLASS_NUMBER, R.drawable.border_radius_white, R.drawable.location_agent , 1);
    }

    private void updateScope(TextInputEditText textInputEditText){
        if (!textInputEditText.getText().toString().isEmpty()){
            int scope = Integer.parseInt(textInputEditText.getText().toString());
            if (scope >= 2 && scope <= 25){
                int finalScope = scope*1000;
                preferences.edit().putInt(Utils.SCOPE, finalScope).commit();
                Utils.showSnackBar(binding.settingsLayout, getString(R.string.scope_updated));
            }else {
                initLocalisationScope(getString(R.string.scope_update_error));
            }
        }else {
            Utils.showSnackBar(binding.settingsLayout, getString(R.string.scope_not_update));
        }
    }

    /** ********************************** **/
    /** **** Dialog Callback Method  **** **/
    /** ******************************** **/

    @Override
    public void onClickedPositiveButtonInputDialog(DialogInterface dialog, TextInputEditText textInputEditText, int dialogIdForSwitch) {
        updateScope(textInputEditText);
    }

    @Override
    public void positiveButtonDialogClicked(DialogInterface dialog) {
        agentViewModel.updatePointOfInterest(agentViewModel.getCurrentUser().getUid(), pointOfInterests);
        Utils.showSnackBar(binding.settingsLayout, getString(R.string.point_of_interest_updated));
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
        Utils.showSnackBar(binding.settingsLayout, getString(R.string.currency_updated));
    }

    @Override
    public void negativeSpinnerButtonDialogClicked(DialogInterface dialog) { dialog.dismiss();}
    @Override
    public void negativeButtonDialogClicked(DialogInterface dialog) { dialog.dismiss();}
    @Override
    public void onClickedNegativeButtonInputDialog(DialogInterface dialog) { dialog.dismiss();}

}
