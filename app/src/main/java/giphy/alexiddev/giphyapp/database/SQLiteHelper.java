package giphy.alexiddev.giphyapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import giphy.alexiddev.giphyapp.model.Data;
import giphy.alexiddev.giphyapp.model.Example;
import giphy.alexiddev.giphyapp.model.Meta;

/**
 * Created by v.aleksandrenko on 18.07.2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MyGifDatabase.db";
    public static final String TABLE_NAME = "GIFS";
    public static final String GIF_ID = "ID";
    public static final String COLUMN_URL_GIF = "URL_GIF";
    private SQLiteDatabase database;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + GIF_ID + " VARCHAR," + COLUMN_URL_GIF + " VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertGif(Example example) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GIF_ID, example.getData().getId());
        contentValues.put(COLUMN_URL_GIF, example.getData().getImageUrl());
        database.insert(TABLE_NAME, null, contentValues);
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        database.close();
    }

    public void deleteGif(Example example) {
        database = this.getReadableDatabase();
        database.execSQL("delete from " + TABLE_NAME + " where " + GIF_ID + " = '" + example.getData().getId() + "'");
        database.close();
    }

    public ArrayList<Example> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Example> gifs = new ArrayList<>();
        Example example;
        Data data;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                data = new Data(cursor.getString(0), cursor.getString(1));
                example = new Example(data);
                gifs.add(example);
            }
        }
        cursor.close();
        database.close();
        return gifs;
    }

}

