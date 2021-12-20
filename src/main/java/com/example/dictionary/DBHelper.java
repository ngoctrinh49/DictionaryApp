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
    //tên file db
    public static final String DATABASE_NAME = "av_all_v3.db";
    public static final int DATABASE_VERSION = 1;
    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";
    public SQLiteDatabase mDB;
    private final String TABLE_ENG_VIET = "av";
    private final String TABLE_VIET_ENG = "va";
    private final String BOOKMARK = "bookmark";
    private final String COLUMN_KEY = "word";           //tên cột từ mới trong bảng
    private final String COLUMN_VALUE = "mean";        //tên cột nghĩa của từ mới

    //hàm khởi tạo
    public DBHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        //vị trí hàm .db
        DATABASE_LOCATION = "data/data/" + mContext.getPackageName() + "/database/";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;         //data/data/com.example.dictionary/database/av_all_v3.db
        //nếu chưa tạo thành công file
        if (!isExistingDB()) {
            File dbLocation = new File(DATABASE_LOCATION);
            dbLocation.mkdir();
            extractAssetToDatabaseDirectory(DATABASE_NAME);
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
        System.out.println("-----------------------------\n" + mDB + "\n-----------------------------\n");
    }

    //phương thức kiểm tra file đã có
    boolean isExistingDB() {
        File file = new File(DATABASE_FULL_PATH);
        return file.exists();
    }

    public void extractAssetToDatabaseDirectory(String filename) throws IOException {
        int length;
        //đọc dữ liệu tù thư mục asset (đọc dữ liệu bytes)
        InputStream sourceDatabase = this.mContext.getAssets().open(filename);      //mở file
        File destinationPath = new File(DATABASE_FULL_PATH);
        OutputStream destination = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[4096];
        while ((length = sourceDatabase.read(buffer)) > 0) {
            //ghi length byte từ mảng byte tham số vào output stream bắt đầu từ 0
            destination.write(buffer, 0, length);
        }

        sourceDatabase.close();     //đóng file
        destination.flush();        //xoá dữ liệu được lưu trong Output stream và ghi dữ liệu xuống đich
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

    public ArrayList<String> getWordFromAV() {
        String q = "SELECT * FROM av";
        Cursor result = mDB.rawQuery(q, null);
        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString((result.getColumnIndex(COLUMN_KEY))));
        }
        return source;
    }

    //1. query 100 từ đầu tiên trong file av
    public ArrayList<String> getWord(int dicType) {                                       //dictype: 2131230782
        String tableName = getTableName(dicType);                                        //av
        int numberOfWords = 100;                                                        //số lượng từ hiển thị
        //String query = "SELECT * FROM " + tableName + " LIMIT " + numberOfWords;        //chu y dau cach
        String query = "SELECT * FROM " + tableName;
        Cursor result = mDB.rawQuery(query, null);
        ArrayList<String> words = new ArrayList<>();           //mảng lưu từ được truy vấn
        //moveToNext chuyển con trỏ đến dòng tiếp theo trong danh sách
        while (result.moveToNext()) {
            int numberOfWord = result.getColumnIndex((COLUMN_KEY));
            //thêm vào mảng
            words.add(result.getString((numberOfWord)));
        }
        return words;           //return mảng chứa từ trong tabel av của db
    }

    public Word getWord(String key, int dicType) {
        String tableName = getTableName(dicType);
        String q = "SELECT * FROM " + tableName + "WHERE upper([word]) = upper(?)";
        System.out.println("----------------------------------query: \n" + "\n----------------------------" + q);
        Cursor result = mDB.rawQuery(q, new String[] {key});
        Word word = new Word();
        while (result.moveToNext()) {
            word.key = result.getString((result.getColumnIndex(COLUMN_KEY)));
            word.value = result.getString((result.getColumnIndex(COLUMN_VALUE)));
        }
        return word;
    }

    //insert word to bookmark - add to your new word list
    //tạo table mới "bookmark" trong file av_all_v3.db để lưu từ
    public void addBookmark(Word word) {
        try {
            //thêm từ mới vào table bookmark trong file db
            String q = "INSERT INTO bookmark([" + COLUMN_KEY + "],[" + COLUMN_VALUE + "]) VALUES (?, ?) ; ";
            mDB.execSQL(q, new Object[]{word.key, word.value});
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //xoá từ đã lưu
    public void removeBookmark(Word word) {
        try {
            //thêm từ mới vào table bookmark trong file db
            String q = "delete from bookmark where upper ([" + COLUMN_KEY + "]) = upper(?) and [" + COLUMN_VALUE + " ] = ?;";
            mDB.execSQL(q, new Object[]{word.key, word.value});
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //3. lấy tên bảng trong file
    public String getTableName(int dicType) {
        String tableName = "";
        if (dicType == R.id.action_eng_viet) {
            tableName = TABLE_ENG_VIET;           //TABLE_ENG_VIET =  av
        } else if (dicType == R.id.action_viet_eng) {
            tableName = TABLE_VIET_ENG;
        }
        return tableName;
    }

    //query all word from BOOKMARK
    public ArrayList<String> getAllWordFromBookmark(String key) {
        String q = "select * from bookmark order by [date] desc;";
        Cursor result = mDB.rawQuery(q, new String[] {key });

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString((result.getColumnIndex(COLUMN_KEY))));
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
        Cursor result = mDB.rawQuery(q, new String[] {key});
        Word word = null;
        while (result.moveToNext()) {
            word = new Word();
            word.key = result.getString((result.getColumnIndex(COLUMN_KEY)));
            word.value = result.getString((result.getColumnIndex(COLUMN_VALUE)));
        }
        return word;
    }
}
