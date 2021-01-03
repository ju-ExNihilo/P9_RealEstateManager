package com.openclassrooms.realestatemanager.injection;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LifecycleOwner;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.repository.AgentRepository;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import fr.juju.googlemaplibrary.repository.GooglePlaceRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static AgentRepository provideAgentRepository(){return new AgentRepository();}

    public static ViewModelFactory provideAgentViewModelFactory(){
        AgentRepository agentDataRepository = provideAgentRepository();
        return new ViewModelFactory(agentDataRepository);
    }

    public static PropertyDataRepository providePropertyRepository(LifecycleOwner owner, Context context){
        GooglePlaceRepository googlePlaceRepository = new GooglePlaceRepository(owner, context.getString(R.string.google_maps_key));
        PropertyDatabase propertyDatabase = PropertyDatabase.getInstance(context);
        Executor executor = provideExecutor();
        return new PropertyDataRepository(googlePlaceRepository, owner, propertyDatabase.propertyDao(), executor);
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory providePropertyViewModelFactory(LifecycleOwner owner, Context context){
        PropertyDataRepository propertyDataRepository = providePropertyRepository(owner, context);
        return new ViewModelFactory(propertyDataRepository);
    }
}
