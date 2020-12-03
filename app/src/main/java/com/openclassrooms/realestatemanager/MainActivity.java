package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

import java.util.Collections;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.onClickGoogleLoginButton();
        this.onClickMailLoginButton();
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

    /** Rooting **/
    public void rooting(){
        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                SecondActivity.navigate(this);
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
                SecondActivity.navigate(this);
            }else {
                showSnackBar(binding.scrollView, getString(R.string.error_unknown_error));
            }
        } else {
            showSnackBar(binding.scrollView, getString(R.string.error_authentication_canceled));
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

    /** Get Current User **/
    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser();}

    private void showSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /** Used to navigate to this activity **/
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
