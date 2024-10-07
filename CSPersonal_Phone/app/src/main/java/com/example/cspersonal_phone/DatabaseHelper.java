package com.example.cspersonal_phone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {           // database 사용을 위한 객체 파일
    public static final String NAME = "PersonalColor.db";        // DB의 이름
    public static final int VERSION = 1;                         // DB의 version

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + "User" + " (" +
                    "id" + "INTEGER PRIMARY KEY ," +
                    "date" + " TEXT, " +
                    "spring" + "INTEGER, " +
                    "summer" + "INTEGER, " +
                    "autumn" + "INTEGER, " +
                    "winter" + "INTEGER)";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS" + "User";
    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }
    public void onCreate(SQLiteDatabase db) {                    // DB 처음 생성 시 생성 시 호출 됨 즉 테이블을 생성, 구조 정의
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onOpen(SQLiteDatabase db) {                      // DB 가 열릴 때 마다 호출됨 즉 초기화 작업 같은 내용 실행 부분


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {      // DB의 스키마, 구조가 변경되었을 때 호출됨 테이블에 내용 추가 및 변경 등 쿼리문 실행
      //  db.execSQL();
    }
}
