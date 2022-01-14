package com.example.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ViJpHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "vietnamese_japanese.db";
    public static final int DATABASE_VERSION = 1;
    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";
    private final String TBL_VJ = "vietnamese_japanese";
    private final String TBL_BOOKMARK = "bookmark";
    private final String COL_KEY = "word";
    private final String COL_VALUE = "content";

    public SQLiteDatabase mDB;

    public ViJpHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        DATABASE_LOCATION = "data/data/" + context.getPackageName() + "/database";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;

        if (!isExistingDB()) {
            try {
                File dbLocation = new File(DATABASE_LOCATION);
                dbLocation.mkdirs();
                extractAssetToDatabaseDirectory(DATABASE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
    }

    public void extractAssetToDatabaseDirectory(String fileName) throws IOException {
        int length;
        InputStream sourceDatabase = this.context.getAssets().open(fileName);
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

    boolean isExistingDB() {
        File file = new File(DATABASE_FULL_PATH);
        return file.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //query all
    public ArrayList<String> getWord() {
        String q = "SELECT [word], [content] FROM " + TBL_VJ;
        Cursor result = mDB.rawQuery(q, null);

        ArrayList<String> source = new ArrayList<>();
        while ((result.moveToNext())) {
            source.add(result.getString((result.getColumnIndex(COL_KEY))));
        }
        return source;
    }

    //query 1 word
    public Word getWord(String key) {
        String q = "SELECT [word], [content] FROM " + TBL_VJ + " WHERE upper([word]) = upper(?)";
        Cursor result = mDB.rawQuery(q, new String[]{key});

        Word word = new Word();

        while ((result.moveToNext())) {
            word.key = result.getString((result.getColumnIndex(COL_KEY)));
            word.value = result.getString((result.getColumnIndex(COL_VALUE)));
        }

        return word;
    }

    public void addBookMark(Word word) {
        try {
            String q = "INSERT INTO " + TBL_BOOKMARK + " ([" + COL_KEY + "],[" + COL_VALUE + "]) VALUES (?, ?);";
            mDB.execSQL(q, new Object[]{word.key, word.value});

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBookMark(Word word) {
        try {
            String q = "DELETE FROM " + TBL_BOOKMARK + " WHERE upper([" + COL_KEY + "]) = upper(?) AND [" + COL_VALUE + "] = ?;";
            mDB.execSQL(q, new Object[]{word.key, word.value});

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllWordFromBookMark() {
        String q = "SELECT * FROM " + TBL_BOOKMARK + " ORDER BY [date] DESC;";
        Cursor result = mDB.rawQuery(q, null);

        Word word = new Word();

        ArrayList<String> source = new ArrayList<>();

        while ((result.moveToNext())) {
            source.add(result.getString((result.getColumnIndex(COL_KEY))));
        }

        return source;
    }

    public Word getWordFromBookMark(String key) {
        System.out.println("---------Key--------" + key + "----------TBL_BOOKMARK----" + TBL_BOOKMARK);    //ban van hoa
        String q = "SELECT * FROM " + TBL_BOOKMARK + " WHERE upper([word]) = upper (?)";
        Cursor result = mDB.rawQuery(q, new String[]{key});

        Word word = null;
        while ((result.moveToNext())) {
            word = new Word();
            word.key = result.getString((result.getColumnIndex(COL_KEY)));
            word.value = result.getString((result.getColumnIndex(COL_VALUE)));
        }
        return null;
    }
}
