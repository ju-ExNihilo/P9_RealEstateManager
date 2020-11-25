package com.openclassrooms.realestatemanager.injection;

import com.openclassrooms.realestatemanager.factory.ViewModelFactory;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;

public class Injection {

    public static PropertyDataRepository providePropertyRepository(){return new PropertyDataRepository();}

    public static ViewModelFactory providePropertyViewModelFactory(){
        PropertyDataRepository propertyDataRepository = providePropertyRepository();
        return new ViewModelFactory(propertyDataRepository);
    }
}
