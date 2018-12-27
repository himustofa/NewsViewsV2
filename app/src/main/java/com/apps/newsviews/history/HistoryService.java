package com.apps.newsviews.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.apps.newsviews.database.SQLiteDAO;
import com.apps.newsviews.utility.ConstantKey;

import java.sql.Timestamp;
import java.util.ArrayList;

public class HistoryService {

    private ArrayList<HistoryModel> arrayList;
    private SQLiteDAO dao;

    public HistoryService(Context context) {
        arrayList = new ArrayList<>();
        dao = new SQLiteDAO(context);
    }

    //Adding single object
    public long addData(HistoryModel model){
        final ContentValues values = new ContentValues();
        values.put(ConstantKey.HISTORY_COLUMN1, model.getHistoryItem());
        values.put(ConstantKey.HISTORY_COLUMN2, String.valueOf(new Timestamp(System.currentTimeMillis())));

        return dao.addData(ConstantKey.HISTORY_TABLE_NAME, values);
    }

    //Getting all objects
    public ArrayList<HistoryModel> getAllData(){
        arrayList = new ArrayList<>();
        Cursor cursor = dao.getAllData(ConstantKey.SELECT_HISTORY_TABLE);
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(ConstantKey.COLUMN_ID));
                String item = cursor.getString(cursor.getColumnIndex(ConstantKey.HISTORY_COLUMN1));
                String createdAt = cursor.getString(cursor.getColumnIndex(ConstantKey.HISTORY_COLUMN2));

                HistoryModel model = new HistoryModel(id, item,createdAt);
                arrayList.add(model);
            }while(cursor.moveToNext());
        }
        return arrayList;
    }


}
