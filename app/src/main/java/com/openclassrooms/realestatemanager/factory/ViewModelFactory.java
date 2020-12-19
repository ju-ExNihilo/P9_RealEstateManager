package com.openclassrooms.realestatemanager.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.openclassrooms.realestatemanager.repository.AgentRepository;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.viewmodel.AgentViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private PropertyDataRepository propertyDataRepository;
    private AgentRepository agentRepository;

    public ViewModelFactory(PropertyDataRepository propertyDataRepository) {
        this.propertyDataRepository = propertyDataRepository;
    }

    public ViewModelFactory(AgentRepository agentRepository){
        this.agentRepository = agentRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)){
            return (T) new PropertyViewModel(propertyDataRepository);
        }else if (modelClass.isAssignableFrom(AgentViewModel.class)){
            return (T) new AgentViewModel(agentRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
