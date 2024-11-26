package com.example.cspersonal_phone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String NAME = "PersonalColor.db";        // DB의 이름
    public static final int VERSION = 1;                         // DB의 version

    private final Context context;
    private final String databasePath;

    // SQL 명령문
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

    private static final String SQL_CREATE_TABLE_OUTFIT =
            "CREATE TABLE " + "Outfit" + " (" +
                    "OUTFIT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "OUTFIT_TYPE INTEGER, " +
                    "CLOTHES INTEGER, " +
                    "COLOR CHAR, " +
                    "SEASON INTEGER, " +
                    "OUTFIT_CODE TEXT)";

    private static final String SQL_DELETE_TABLE_USER =
            "DROP TABLE IF EXISTS USER";

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

    // 생성자
    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
        this.databasePath = context.getDatabasePath(NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // DB가 처음 생성될 때 호출
        db.execSQL(SQL_CREATE_TABLE_USER);
      //  db.execSQL(SQL_CREATE_TABLE_OUTFIT);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        // DB가 열릴 때 호출
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DB 스키마가 변경되었을 때 호출
    }

    // 사용자 데이터 삽입 메서드
    public void INSERT_User(SQLiteDatabase db, String name, String timestamp, int spring, int summer, int autumn, int winter, String personalColor) {
        db.execSQL(SQL_INSERT_TABLE_USER, new Object[]{name, timestamp, spring, summer, autumn, winter, personalColor});
    }

    public Cursor SELECT_ALL_OUTFIT() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SQL_SELECT_TABLE_OUTFIT, null);
    }

    public Cursor getOutfitsByTypeAndSeason(int season, int outfitType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Outfit WHERE SEASON = ? AND OUTFIT_TYPE = ?";
        return db.rawQuery(query, new String[]{String.valueOf(season), String.valueOf(outfitType)});
    }

    public Cursor getOutfitsByColorAndType(String colorHex, int outfitType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Outfit WHERE COLOR = ? AND OUTFIT_TYPE = ?";
        return db.rawQuery(query, new String[]{colorHex, String.valueOf(outfitType)});
    }

    // 데이터베이스 복사 관련 메서드 추가
    public void createDatabase() {
        // 데이터베이스가 없는 경우 assets에서 복사
        if (!checkDatabaseExists()) {
            try {
                copyDatabase();
                Log.d("DatabaseHelper", "Database copied successfully");
            } catch (IOException e) {
                Log.e("DatabaseHelper", "Error copying database", e);
            }
        } else {
            Log.d("DatabaseHelper", "Database already exists");
        }
    }

    private boolean checkDatabaseExists() {
        // 내부 저장소에 데이터베이스 파일이 존재하는지 확인
        File dbFile = new File(databasePath);
        return dbFile.exists();
    }

    private void copyDatabase() throws IOException {
        // assets 폴더에서 데이터베이스 파일 복사
        InputStream inputStream = context.getAssets().open(NAME);
        OutputStream outputStream = new FileOutputStream(databasePath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
        // 디버깅용 로그
        Log.d("DatabaseHelper", "Database copied to: " + databasePath);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
