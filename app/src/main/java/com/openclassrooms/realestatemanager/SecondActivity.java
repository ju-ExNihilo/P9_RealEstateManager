package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbarMain);
        this.initDrawerLayout();
        this.iniNavigationView();
    }

    @Override
    public void onBackPressed() {
        if (binding.layoutDrawer.isDrawerOpen(GravityCompat.START)){
            binding.layoutDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add_property:
                break;
            case R.id.map:
                break;
            case R.id.all_property:
                break;
            case R.id.my_property:
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
                .addOnSuccessListener(aVoid -> MainActivity.navigate(this));
    }

    private void initDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.layoutDrawer, binding.toolbarMain,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.layoutDrawer.addDrawerListener(toggle);
        toggle.syncState();
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
        Intent intent = new Intent(activity, SecondActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
