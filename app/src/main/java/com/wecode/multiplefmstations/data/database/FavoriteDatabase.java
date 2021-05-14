package com.wecode.multiplefmstations.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wecode.multiplefmstations.data.network.responses.Radio;

@Database(entities = Radio.class, version = 2,exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase instance;

    public abstract FavoriteDao favoriteDao();

    //synchronized because only one thread can access this class
    public static synchronized FavoriteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavoriteDatabase.class,
                    "favorite_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
