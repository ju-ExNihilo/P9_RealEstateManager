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

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PropertyDaoTest {

    private PropertyDatabase propertyDatabase;
    private int numberItem;
    private final long ID = 1;
    private final String PROPERTY_ID = "PROPERTY_ID";
    private final String PROPERTY_TYPE = "Flat";
    private final String PROPERTY_CITY = "London";
    private final String UPDATED_URL = "urlTest";
    private final double UPDATED_LATITUDE = 42.5667;
    private final double UPDATED_LONGITUDE = 6.747557;
    private final boolean UPDATED_SOLD = true;
    private final Property PROPERTY_DEMO = new Property(ID,PROPERTY_ID,"julien", PROPERTY_TYPE, PROPERTY_CITY, 500,
            "https://firebasestorage.googleapis.com/v0/b/realestatemanager-90248.appspot.com/o/" +
            "f0377259-cd70-4764-ab9c-8a199ab85396?alt=media&token=97b33525-0f9c-4216-a058-ad676723636c", 43.1200927, 6.13288, false);


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        propertyDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        numberItem = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size();
    }

    @After
    public void closeDb(){
        propertyDatabase.close();
    }

    @Test
    public void insertGetAndDeleteProperty() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);

        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getPropertyId(), PROPERTY_ID);
        assertEquals(property.getPropertyType(), PROPERTY_TYPE);
        assertEquals(property.getPropertyLocatedCity(), PROPERTY_CITY);

        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem + 1);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem);
    }

    @Test
    public void updatePreviewImageUrl() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);

        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem + 1);

        this.propertyDatabase.propertyDao().updatePreviewImageUrl(UPDATED_URL, PROPERTY_ID);
        property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getPropertyPreviewImageUrl(), UPDATED_URL);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem);
    }

    @Test
    public void updateLatitude() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);

        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem + 1);

        this.propertyDatabase.propertyDao().updateLatitude(UPDATED_LATITUDE, PROPERTY_ID);
        property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getLatitude(), UPDATED_LATITUDE, 0.0);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem);
    }

    @Test
    public void updateLongitude() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);

        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem + 1);

        this.propertyDatabase.propertyDao().updateLongitude(UPDATED_LONGITUDE, PROPERTY_ID);
        property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getLongitude(), UPDATED_LONGITUDE, 0.0);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem);
    }

    @Test
    public void updateSold() throws InterruptedException {
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);

        Property property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem + 1);

        this.propertyDatabase.propertyDao().updateSold(UPDATED_SOLD, PROPERTY_ID);
        property = LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAProperty(PROPERTY_ID));
        assertEquals(property.isSold(), UPDATED_SOLD);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyDao().getAllProperty()).size(), numberItem);
    }
}
