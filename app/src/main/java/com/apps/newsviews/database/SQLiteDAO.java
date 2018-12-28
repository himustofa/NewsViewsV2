package com.apps.newsviews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apps.newsviews.utility.ConstantKey;

public final class SQLiteDAO {

    private static final String DATABASE_NAME = "news_views";
    private static final int DATABASE_VERSION = 1; //After table creating and column adding then must be increment database version

    private final SQLiteDatabase database;
    private final SQLiteOpenHelper helper;

    public SQLiteDAO(final Context context) {
        this.helper = new SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
            @Override
            public void onCreate(final SQLiteDatabase db) {
                db.execSQL(ConstantKey.CREATE_HISTORY_TABLE);
                db.execSQL(ConstantKey.INSERT_HISTORY_DATA1);
                db.execSQL(ConstantKey.INSERT_HISTORY_DATA2);
                db.execSQL(ConstantKey.INSERT_HISTORY_DATA3);
                db.execSQL(ConstantKey.INSERT_HISTORY_DATA4);
            }
            @Override
            public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
                db.execSQL(ConstantKey.DROP_HISTORY_TABLE);
                this.onCreate(db);
            }
        };
        this.database = this.helper.getWritableDatabase();
    }

    public long addData(String tableName, ContentValues values) {
        long data = this.database.insert(tableName, null, values);
        //this.database.close();
        return data;
    }

    public Cursor getAllData(String query) {
        final Cursor cursor = this.database.rawQuery(query,null);
        //this.database.close();
        return cursor;
    }

}
