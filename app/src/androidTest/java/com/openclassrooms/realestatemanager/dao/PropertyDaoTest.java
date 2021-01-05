package com.openclassrooms.realestatemanager.dao;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PropertyDaoTest {

    private PropertyDatabase propertyDatabase;
    private static long ID = 1;
    private static String PROPERTY_ID = "PROPERTY_ID";
    private static String PROPERTY_TYPE = "Flat";
    private static String PROPERTY_CITY = "London";
    private static String UPDATED_URL = "urlTest";
    private static Property PROPERTY_DEMO = new Property(ID,PROPERTY_ID,"julien", PROPERTY_TYPE, PROPERTY_CITY, 500, "https://firebasestorage.googleapis.com/v0/b/realestatemanager-90248.appspot.com/o/" +
            "f0377259-cd70-4764-ab9c-8a199ab85396?alt=media&token=97b33525-0f9c-4216-a058-ad676723636c", 43.1200927, 6.13288, false);
    private int numberItem;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        propertyDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        numberItem = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllPropertyProvider()).size();
    }

    @After
    public void closeDb() throws Exception {
        propertyDatabase.close();
    }

    @Test
    public void insertGetAndDeleteProperty() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);
        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertTrue(property.getPropertyId().equals(PROPERTY_ID));
        assertTrue(property.getPropertyType().equals(PROPERTY_TYPE));
        assertTrue(property.getPropertyLocatedCity().equals(PROPERTY_CITY));
        assertTrue(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllPropertyProvider()).size() == numberItem+1);
        propertyDatabase.propertyDao().deletePropertyProvider(ID);
        assertTrue(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllPropertyProvider()).size() == numberItem);
    }

    @Test
    public void updatePreviewImageUrl() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);
        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertTrue(property.getPropertyId().equals(PROPERTY_ID));
        assertTrue(property.getPropertyType().equals(PROPERTY_TYPE));
        assertTrue(property.getPropertyLocatedCity().equals(PROPERTY_CITY));
        assertTrue(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllPropertyProvider()).size() == numberItem+1);
        this.propertyDatabase.propertyDao().updatePreviewImageUrl(UPDATED_URL, PROPERTY_ID);
        Property property1 = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertTrue(property1.getPropertyPreviewImageUrl().equals(UPDATED_URL));
        propertyDatabase.propertyDao().deletePropertyProvider(ID);
        assertTrue(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllPropertyProvider()).size() == numberItem);
    }
}
