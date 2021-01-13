package com.openclassrooms.realestatemanager.dao;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PointInterestDaoTest {

    private PropertyDatabase propertyDatabase;
    private int numberItem;
    private final long ID = 1;
    private final String PROPERTY_ID = "PROPERTY_ID";
    private final String POINT_INTEREST_ID = "POINT_INTEREST_ID";
    private final Property PROPERTY_DEMO = new Property(ID,PROPERTY_ID,"julien", "Flat", "London", 500,
            "https://firebasestorage.googleapis.com/v0/b/realestatemanager-90248.appspot.com/o/" +
                    "f0377259-cd70-4764-ab9c-8a199ab85396?alt=media&token=97b33525-0f9c-4216-a058-ad676723636c", 43.1200927, 6.13288, false);
    private final PointOfInterest POINT_INTEREST_DEMO = new PointOfInterest(ID, POINT_INTEREST_ID, PROPERTY_ID, "School");


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        propertyDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);
        numberItem = LiveDataTestUtil.getValue(propertyDatabase.addressDao().getAllAddressForTest()).size();
    }

    @After
    public void closeDb(){
        propertyDatabase.close();
    }

    @Test
    public void insertGetAndDeletePointOfInterest() throws InterruptedException {
        this.propertyDatabase.pointOfInterestDao().insertPointOfInterest(POINT_INTEREST_DEMO);

        PointOfInterest pointOfInterest = LiveDataTestUtil.getValue(propertyDatabase.pointOfInterestDao().getAPointOfInterest(PROPERTY_ID));
        assertEquals(pointOfInterest.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.pointOfInterestDao().getAllPointOfInterestForTest()).size(), numberItem + 1);

        propertyDatabase.pointOfInterestDao().deletePointOfInterestForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.pointOfInterestDao().getAllPointOfInterestForTest()).size(), numberItem);
        propertyDatabase.propertyDao().deletePropertyForTest(ID);
    }

    @Test
    public void deleteCascade() throws InterruptedException {
        this.propertyDatabase.pointOfInterestDao().insertPointOfInterest(POINT_INTEREST_DEMO);

        PointOfInterest pointOfInterest = LiveDataTestUtil.getValue(propertyDatabase.pointOfInterestDao().getAPointOfInterest(PROPERTY_ID));
        assertEquals(pointOfInterest.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.pointOfInterestDao().getAllPointOfInterestForTest()).size(), numberItem + 1);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.pointOfInterestDao().getAllPointOfInterestForTest()).size(), numberItem);
    }
}
