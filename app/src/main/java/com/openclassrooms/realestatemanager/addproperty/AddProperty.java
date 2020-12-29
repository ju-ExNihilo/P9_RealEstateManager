package com.openclassrooms.realestatemanager.addproperty;

<<<<<<< HEAD
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
=======
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding;
>>>>>>> addProperty

public class AddProperty extends AppCompatActivity {

    ActivityAddPropertyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPropertyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
<<<<<<< HEAD
=======
        this.initToolbar();

    }

    private void initToolbar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    /** Used to navigate to this activity **/
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddProperty.class);
        ActivityCompat.startActivity(activity, intent, null);
>>>>>>> addProperty
    }
}
