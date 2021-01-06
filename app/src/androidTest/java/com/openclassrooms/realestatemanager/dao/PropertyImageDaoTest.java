package com.openclassrooms.realestatemanager.dao;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyImage;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PropertyImageDaoTest {

    private PropertyDatabase propertyDatabase;
    private int numberItem;
    private final long ID = 1;
    private final String PROPERTY_ID = "PROPERTY_ID";
    private final String PROPERTY_IMAGE_ID = "PROPERTY_IMAGE_ID";
    private final String DESCRIPTION = "DESCRIPTION";
    private final String DESCRIPTION_UPDATE = "DESCRIPTION_UPDATE";
    private final Property PROPERTY_DEMO = new Property(ID,PROPERTY_ID,"julien", "Flat", "London", 500,
            "https://firebasestorage.googleapis.com/v0/b/realestatemanager-90248.appspot.com/o/" +
                    "f0377259-cd70-4764-ab9c-8a199ab85396?alt=media&token=97b33525-0f9c-4216-a058-ad676723636c", 43.1200927, 6.13288, false);
    private final PropertyImage PROPERTY_IMAGE_DEMO = new PropertyImage(ID, PROPERTY_IMAGE_ID, PROPERTY_ID, "url_test", DESCRIPTION);


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        propertyDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        this.propertyDatabase.propertyDao().insertProperty(PROPERTY_DEMO);
        numberItem = LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size();
    }

    @After
    public void closeDb(){
        propertyDatabase.close();
    }

    @Test
    public void insertGetAndDeleteAddress() throws InterruptedException {
        this.propertyDatabase.propertyImageDao().insertPropertyImage(PROPERTY_IMAGE_DEMO);

        PropertyImage propertyImage = LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAPropertyImage(PROPERTY_ID));
        assertEquals(propertyImage.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size(), numberItem + 1);

        propertyDatabase.propertyImageDao().deletePropertyImageForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size(), numberItem);
        propertyDatabase.propertyDao().deletePropertyForTest(ID);
    }

    @Test
    public void deleteCascade() throws InterruptedException {
        this.propertyDatabase.propertyImageDao().insertPropertyImage(PROPERTY_IMAGE_DEMO);

        PropertyImage propertyImage = LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAPropertyImage(PROPERTY_ID));
        assertEquals(propertyImage.getPropertyId(), PROPERTY_ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size(), numberItem + 1);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size(), numberItem);
    }

    public void updateImageDescription() throws InterruptedException {
        this.propertyDatabase.propertyImageDao().insertPropertyImage(PROPERTY_IMAGE_DEMO);

        PropertyImage propertyImage = LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAPropertyImage(PROPERTY_ID));
        assertEquals(propertyImage.getPropertyId(), PROPERTY_ID);
        assertEquals(propertyImage.getImageDescription(), DESCRIPTION);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size(), numberItem + 1);

        propertyDatabase.propertyImageDao().updateImageDescription(DESCRIPTION_UPDATE, PROPERTY_ID, PROPERTY_IMAGE_ID);
        propertyImage = LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAPropertyImage(PROPERTY_ID));
        assertEquals(propertyImage.getPropertyId(), PROPERTY_ID);
        assertEquals(propertyImage.getImageDescription(), DESCRIPTION);

        propertyDatabase.propertyDao().deletePropertyForTest(ID);
        assertEquals(LiveDataTestUtil.getValue(propertyDatabase.propertyImageDao().getAllPropertyImageForTest()).size(), numberItem);
    }
}
