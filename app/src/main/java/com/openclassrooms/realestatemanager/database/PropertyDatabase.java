package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.openclassrooms.realestatemanager.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;

@Database(entities = Property.class, version = 1, exportSchema = false)
public abstract class PropertyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile PropertyDatabase INSTANCE;

    // --- DAO ---
    public abstract PropertyDao propertyDao();

    // --- INSTANCE ---
    public static PropertyDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (PropertyDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PropertyDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(){
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
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

                db.insert("Property", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

}
