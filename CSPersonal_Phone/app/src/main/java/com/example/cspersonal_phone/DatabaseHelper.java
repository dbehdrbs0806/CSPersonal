package com.example.cspersonal_phone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {           // database 사용을 위한 객체 파일
    public static final String NAME = "PersonalColor.db";        // DB의 이름
    public static final int VERSION = 1;                         // DB의 version

    private static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE USER (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "TIMESTAMP TEXT, " +
                    "SPRING INTEGER, " +
                    "SUMMER INTEGER, " +
                    "AUTUMN INTEGER, " +
                    "WINTER INTEGER, " +
                    "PERSONAL_COLOR TEXT DEFAULT NULL)";  // NULL 값 허용
    // OUTFIT의 INTEGER 상의 0 하의 1
    private static final String SQL_CREATE_TABLE_OUTFIT =
            "CREATE TABLE " + "Outfit" + " (" +
                    "OUTFIT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OUTFIT_TYPE INTEGER, " +
                    "OUTFIT_CODE TEXT)";

    //
    private static final String SQL_CREATE_TABLE_COLOR =
            "CREATE TABLE " + "COLOR" + " (" +
                    "COLOR_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "COLOR_SEASON INTEGER, " +
                    "COLOR_TYPE INTEGER," +
                    "COLUMN_CODE TEXT)";


    private static final String SQL_DELETE_TABLE_USER =
            "DROP TABLE IF EXISTS" + "USER";

    private static final String SQL_INSERT_TABLE_USER =
            "INSERT INTO USER (" +
                    "NAME, " +
                    "TIMESTAMP, " +
                    "SPRING, " +
                    "SUMMER, " +
                    "AUTUMN, " +
                    "WINTER, " +
                    "PERSONAL_COLOR) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";


    private static final String SQL_SELECT_TABLE_OUTFIT =
            "SELECT * FROM Outfit";

    private static final String SQL_SELECT_TABLE_COLOR =
            "SELECT * FROM COLOR";

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }
    public void onCreate(SQLiteDatabase db) {                    // DB 처음 생성 시 생성 시 호출 됨 즉 테이블을 생성, 구조 정의
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_COLOR);
        db.execSQL(SQL_CREATE_TABLE_OUTFIT);
    }

    public void onOpen(SQLiteDatabase db) {                      // DB 가 열릴 때 마다 호출됨 즉 초기화 작업 같은 내용 실행 부분
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {      // DB의 스키마, 구조가 변경되었을 때 호출됨 테이블에 내용 추가 및 변경 DML 쿼리문 실행
      //  db.execSQL();
    }
    public void INSERT_User(SQLiteDatabase db, String name, String timestamp, int spring, int summer, int autumn, int winter, String personalColor) {
        db.execSQL(SQL_INSERT_TABLE_USER, new Object[]{name, timestamp, spring, summer, autumn, winter, personalColor});
    }
    public Cursor SELECT_ALL_OUTFIT() {                             // OUTFIT테이블의 모든 정보를 조회
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SQL_SELECT_TABLE_OUTFIT, null);
    }

    /*public Cursor SELECT_ALL_COLOR(SQLiteDatabase db) {
        db = this.getReadableDatabase();
        return db.rawQuery(SQL_SELECT_TABLE_COLOR);
    }*/
}
