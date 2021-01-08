package com.openclassrooms.realestatemanager.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.databinding.ActivityLoginBinding;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.home.HomeActivity;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodel.AgentViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity{

    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 123;
    private AgentViewModel agentViewModel;
    private PropertyViewModel propertyViewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        this.initAgentViewModel();
        this.initPropertyViewModel();
        this.onClickGoogleLoginButton();
        this.onClickMailLoginButton();
        this.initMaxSurface();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.rooting();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            this.handleResponseAfterSignIn(response);
        }
    }

    /** Configure user ViewModel **/
    private void initAgentViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideAgentViewModelFactory();
        agentViewModel = new ViewModelProvider(this, viewModelFactory).get(AgentViewModel.class);
    }

    /** Configure user ViewModel **/
    private void initPropertyViewModel(){
        ViewModelFactory viewModelFactory = Injection.providePropertyViewModelFactory(this, this);
        propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    private void initMaxSurface(){
        propertyViewModel.getMaxSurface().observe(this, integer -> preferences.edit().putInt("MaxSurface", integer).commit());
    }



    /** Rooting **/
    public void rooting(){
        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                HomeActivity.navigate(this);
            }else {
                binding.loadingPanel.setVisibility(View.GONE);
            }
        }, 2*1000); // wait for 2 seconds
    }

    /** Login with Mail **/
    public void onClickMailLoginButton() {
        binding.mailSignIn.setOnClickListener(v -> startSignInActivity(new AuthUI.IdpConfig.EmailBuilder().build()) );
    }

    /** Login with Google **/
    public void onClickGoogleLoginButton() {
        binding.googleSignIn.setOnClickListener(v -> startSignInActivity(new AuthUI.IdpConfig.GoogleBuilder().build()) );
    }

    private void handleResponseAfterSignIn(IdpResponse response) {
        if (response != null) {
            if (this.getCurrentUser() != null){
                this.insertAgentInFireStore();
                HomeActivity.navigate(this);
            }else {
                Utils.showSnackBar(binding.scrollView, getString(R.string.error_unknown_error));
            }
        } else {
            Utils.showSnackBar(binding.scrollView, getString(R.string.error_authentication_canceled));
        }
    }

    /** Login with FirebaseUI **/
    private void startSignInActivity(AuthUI.IdpConfig authUI){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(authUI))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    /** Create user for FireStore **/
    private void insertAgentInFireStore(){
        if (this.getCurrentUser() != null){
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            List<String> defaultProximityPointOfInterest = Arrays.asList("park", "school", "museum", "shopping_mall");
            Agent agent = new Agent();
            agent.setAgentId(uid);
            agent.setAgentName(username);
            agent.setProximityPointOfInterestChoice(defaultProximityPointOfInterest);
            agentViewModel.insertAgent(agent);
        }
    }


    /** Get Current User **/
    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser();}

    /** Used to navigate to this activity **/
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
