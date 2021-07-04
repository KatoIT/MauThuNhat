package com.example.mauthunhat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class An_Sqlite extends SQLiteOpenHelper {
    private static final String DATABASENAME = "An.sqlite";
    private static final int VERSION = 1;
    public final String TABLENAME = "Product_181203458";
    public final String COL_0 = "Id";
    public final String COL_1 = "Name";
    public final String COL_2 = "Description";
    public final String COL_3 = "Image";
    public final String COL_4 = "Status";

    public An_Sqlite(Context context) {
        super(context, DATABASENAME, null, VERSION);
    }

    // AUTOINCREMENT    : tự động sinh khóa
    // PRIMARY KEY      : Khóa chính
    // NVARCHAR(255)
    // MONEY    INTEGER     BOOLEAN     DATETIME       DATE     : Kiểu dữ liệu

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLENAME + " ("
                + COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_1 + " NVARCHAR(255),"
                + COL_2 + " NVARCHAR(255), "
                + COL_3 + " NVARCHAR(255), "
                + COL_4 + " BOOLEAN)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Truy vấn có trả kết quả: CREATE, INSERT, UPDATE, DELETE,...
    public void QueryData(String str) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(str);
    }

    // Truy vấn có trả kết quả: SELECT
    public Cursor GetData(String str) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(str, null);
    }

    public void Delete(Product_181203458 object) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLENAME + " WHERE " + COL_0 + " = " + object.getId() + ";");
    }

    public void Insert(Product_181203458 object) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO " + TABLENAME + " VALUES(NULL,?,?,?,?)";
        SQLiteStatement mSqLiteStatement = sqLiteDatabase.compileStatement(sql);
        mSqLiteStatement.clearBindings();
        mSqLiteStatement.bindString(1, object.getName());
        mSqLiteStatement.bindString(2, object.getDescription());
        mSqLiteStatement.bindString(3, object.getImage());
        mSqLiteStatement.bindString(4, String.valueOf(object.isStatus()));

        mSqLiteStatement.executeInsert();
    }

    public void Update(Product_181203458 object) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = " UPDATE " + TABLENAME + " SET "
                + COL_1 + " = ?, "
                + COL_2 + " = ?, "
                + COL_3 + " = ?, "
                + COL_4 + " = ?"
                + " WHERE " + COL_0 + " = " + object.getId() + ";";
        SQLiteStatement mSqLiteStatement = sqLiteDatabase.compileStatement(sql);
        mSqLiteStatement.clearBindings();
        mSqLiteStatement.bindString(1, object.getName());
        mSqLiteStatement.bindString(2, object.getDescription());
        mSqLiteStatement.bindString(3, object.getImage());
        mSqLiteStatement.bindString(4, String.valueOf(object.isStatus()));

        mSqLiteStatement.executeUpdateDelete();
    }

}
