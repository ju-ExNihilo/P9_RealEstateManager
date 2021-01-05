package com.openclassrooms.realestatemanager.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.realestatemanager.contentprovider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyRelation;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class PropertyContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;
    private PropertyDatabase propertyDatabase;
    private PropertyDataRepository propertyDataRepository;
    private int numberItems;

    // DATA SET FOR TEST
    private static long PROPERTY_ID = 1;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws InterruptedException {
        propertyDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = ApplicationProvider.getApplicationContext().getContentResolver();
        this.configurePropertyRepository();
        numberItems = LiveDataTestUtil.getValue(this.propertyDataRepository.getAllPropertyFromRoomForTest()).size();
    }

    @After
    public void closeDb() throws Exception {
        propertyDatabase.close();
    }

    @Test
    public void getItems(){
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, PROPERTY_ID),
                null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(numberItems));
        cursor.close();
    }

    /** Configure property Repository **/
    private void configurePropertyRepository(){
        LifecycleOwner lifecycle = mock(LifecycleOwner.class);;
        propertyDataRepository = Injection.providePropertyRepository(lifecycle, ApplicationProvider.getApplicationContext());
    }


}
