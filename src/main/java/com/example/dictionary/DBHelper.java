package com.example.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    public static final String DATABASE_NAME = "av_all_v3";
    public static final int DATABASE_VERSION = 1;

    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";

    public SQLiteDatabase mDB;

    private final String TABLE_ENG_VIET = "av";
    private final String TABLE_VIET_ENG = "av";
    private final String BOOKMARK = "bookmark";

    private final String COL_KEY = "word";
    private final String COL_VALUE = "value";


    public DBHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

        DATABASE_LOCATION = "data/data/" + mContext.getPackageName() + "/database/";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;

        if (!isExistingDB()) {
            File dbLocation = new File(DATABASE_LOCATION);
            dbLocation.mkdir();

            extractAssetToDatabaseDirectory(DATABASE_NAME);
        }

        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
    }

    boolean isExistingDB() {
        File file = new File(DATABASE_FULL_PATH);
        return file.exists();
    }

    public void extractAssetToDatabaseDirectory(String filename) throws IOException {
        int length;
        InputStream sourceDatabase = this.mContext.getAssets().open(filename);
        File destinationPath = new File(DATABASE_FULL_PATH);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        while ((length = sourceDatabase.read(buffer)) > 0) {
            destination.write(buffer, 0, length);
        }

        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //query all word list base on dictionary type
    public ArrayList<String> getWord(int dicType) {
        String tableName = getTableName(dicType);
        String q = "SELECT * FROM " + tableName;
        Cursor result = mDB.rawQuery(q, null);
        ArrayList<String> source = new ArrayList<>();

        while (result.moveToNext()) {
            source.add(result.getString((result.getColumnIndex(COL_KEY))));
        }
        return source;
    }

    public Word getWord(String key, int dicType) {
        String tableName = getTableName(dicType);
        String q = "SELECT * FROM " + tableName + "WHERE upper([word]) = upper(?)";
        Cursor result = mDB.rawQuery(q, new String[] {key });

        Word word = new Word();
        while (result.moveToNext()) {
            word.key = result.getString((result.getColumnIndex(COL_KEY)));
            word.value = result.getString((result.getColumnIndex(COL_VALUE)));
        }
        return word;
    }

    //insert word to bookmark - add to your new word list
    //tạo table mới "bookmark" trong file av_all_v3.db để lưu từ

    //
    public void addBookmark(Word word) {
        try {
            //thêm từ mới vào table bookmark trong file db
            String q = "INSERT INTO bookmark([" + COL_KEY + "],[" + COL_VALUE + "]) VALUES (?, ?) ; ";
            mDB.execSQL(q, new Object[]{word.key, word.value});
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //xoa tu trong bookmark
    public void removeBookmark(Word word) {
        try {
            //thêm từ mới vào table bookmark trong file db
            String q = "delete from bookmark where upper ([" + COL_KEY + "]) = upper(?) and [" + COL_VALUE + " ] = ?;";
            mDB.execSQL(q, new Object[]{word.key, word.value});
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public String getTableName(int dicType) {
        String tableName = "";
        if (dicType == R.id.action_eng_viet) {
            tableName = TABLE_ENG_VIET;
        } else if (dicType == R.id.action_viet_eng) {
            tableName = TABLE_VIET_ENG;
        }
        return tableName;
    }

    //query all word from bookmark
    public ArrayList<String> getAllWordFromBookmark(String key) {
        String q = "select * from bookmark order by [date] desc;";
        Cursor result = mDB.rawQuery(q, new String[] {key });

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString((result.getColumnIndex(COL_KEY))));
        }
        return source;
    }

    //tu da duoc danh dau khi bookmark chua
    public boolean isWordMark(Word word) {
        String q = "SELECT * FROM bookmark where upper([key]) = upper(?) and [value] = ?";
        Cursor result = mDB.rawQuery(q, new String[]{word.key, word.value});

        return result.getCount() > 0;
    }

    //query word from bookmark by key
    public Word getWordFromBookmark(String key) {
        String q = "SELECT * FROM bookmark where upper([key]) = upper(?)";
        Cursor result = mDB.rawQuery(q, null);
        Word word = new Word();
        while (result.moveToNext()) {
            word.key = result.getString((result.getColumnIndex(COL_KEY)));
            word.value = result.getString((result.getColumnIndex(COL_VALUE)));
        }
        return word;
    }

}
