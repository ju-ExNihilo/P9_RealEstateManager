package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.realestatemanager.contentprovider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class PropertyContentProviderTest {
    // FOR DATA
    private ContentResolver mContentResolver;
    private PropertyDatabase propertyDatabase;
    private int numberItems;

    // DATA SET FOR TEST
    private static long PROPERTY_ID = 1;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        propertyDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
        numberItems = propertyDatabase.propertyDao().getPropertyWithCursor().getCount();
    }

    @After
    public void closeDb() throws Exception {
        propertyDatabase.close();
    }

    @Test
    public void getItems() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, PROPERTY_ID),
                null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(numberItems));
        cursor.close();
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = mContentResolver.insert(PropertyContentProvider.URI_ITEM, generateItem());
        // TEST
        Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, PROPERTY_ID),
                null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(numberItems + 1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("propertyType")), is("Flat"));
        // After : Adding demo item
        mContentResolver.delete(userUri, null, null);
        cursor = mContentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_ITEM, PROPERTY_ID),
                null, null, null, null);
        assertThat(cursor.getCount(), is(numberItems));
    }

    private ContentValues generateItem(){
        final ContentValues contentValues = new ContentValues();
        contentValues.put("id", PROPERTY_ID);
        contentValues.put("propertyId", "1");
        contentValues.put("agentId", "OiW2p7Tj4lZeCuyztR9oLDYuLZ92");
        contentValues.put("propertyType", "Flat");
        contentValues.put("propertyLocatedCity", "London");
        contentValues.put("propertyPrice", 1000000);
        contentValues.put("propertyPreviewImageUrl", "https://firebasestorage.googleapis.com/v0/b/" +
                "realestatemanager-90248.appspot.com/o/18e7815d-346a-4087-b982-06cbe485e260?alt=media&token=bc8105db-200e-4c0d-af10-5dfdb4c10531");
        contentValues.put("latitude", 6.46123);
        contentValues.put("longitude", 43.45668);
        contentValues.put("isSale", false);
        return contentValues;
    }

}
