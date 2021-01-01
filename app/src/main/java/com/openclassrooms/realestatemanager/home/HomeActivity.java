package com.openclassrooms.realestatemanager.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityHomeBinding;
import com.openclassrooms.realestatemanager.login.LoginActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        this.configureToolbar();
        this.iniNavigationView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setSelectedNavigationItem(0);
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutDrawer.isDrawerOpen(GravityCompat.START)){
            binding.layoutDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void setSelectedNavigationItem(int id){
        switch (id){
            case 0:
                binding.navView.getMenu().getItem(0).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGold)));
                binding.navView.getMenu().getItem(1).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGray)));
                binding.navView.getMenu().getItem(2).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGray)));
                break;
            case 1:
                binding.navView.getMenu().getItem(0).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGray)));
                binding.navView.getMenu().getItem(1).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGold)));
                binding.navView.getMenu().getItem(2).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGray)));
                break;
            case 2:
                binding.navView.getMenu().getItem(0).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGray)));
                binding.navView.getMenu().getItem(1).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGray)));
                binding.navView.getMenu().getItem(2).setIconTintList(ColorStateList.valueOf(getColor(R.color.colorGold)));
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                navController.navigateUp();
                break;
            case R.id.all_property:
                navController.navigate(R.id.propertyListView);
                this.setSelectedNavigationItem(0);
                break;
            case R.id.map:
                navController.navigate(R.id.mapFragment);
                this.setSelectedNavigationItem(2);
                break;
            case R.id.my_property:
                this.setSelectedNavigationItem(1);
                break;
            case R.id.logout:
                this.logout();
                break;
            default:
                break;
        }
        binding.layoutDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(aVoid -> LoginActivity.navigate(this));
    }

    private void configureToolbar(){
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(binding.layoutDrawer).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void iniNavigationView(){
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        View header = binding.navView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.user_name);
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null){
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }else {
            name.setText("");
        }
    }



    /** Used to navigate to this activity **/
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
