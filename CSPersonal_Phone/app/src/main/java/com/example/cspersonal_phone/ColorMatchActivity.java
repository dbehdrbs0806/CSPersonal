package com.example.cspersonal_phone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.Manifest;



public class ColorMatchActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 101;  // 권환 요청 시 사용할 식별자 상수

    private String name;
    private int personalColor;

    private Intent before_intent;

    private DatabaseHelper dbHelper;        // DB의 사용
    private ImageView top_ImageView;        // 상의 이미지 뷰
    private ImageView bottom_ImageView;     // 하의 이미지 뷰
    private ArrayList<OUTFIT> topIcons;          // 상의 아이콘 리스트
    private ArrayList<OUTFIT> bottomIcons;       // 하의 아이콘 리스트

    private HashMap<Integer, String[][]> colorMapping;   // 퍼스널컬러에 맞는 색상 해쉬맵
    private int topIndex = 0;               // 상의 배열 인덱스
    private int bottomIndex = 0;            // 하의 배열 인덱스

    private boolean isSelected;            // 상의 선택 여부 변수

    ImageView top_left_change_button;         // 상의 왼쪽 버튼
    ImageView top_right_change_button;        // 상의 오른쪽 버튼

    ImageView bottom_left_change_button;         // 하의 왼쪽 버튼
    ImageView bottom_right_change_button;        // 하의 오른쪽 버튼
    Button top_recommend_color_button1;    // 상의 추천 옷 버튼1
    Button top_recommend_color_button2;    // 상의 추천 옷 버튼2
    Button top_recommend_color_button3;    // 상의 추천 옷 버튼3
    Button bottom_recommend_color_button1;    // 하의 추천 옷 버튼1
    Button bottom_recommend_color_button2;    // 하의 추천 옷 버튼2
    Button bottom_recommend_color_button3;    // 하의 추천 옷 버튼3
    ImageView save_button;                 // 옷 저장 버튼

    ImageView goback_button;               // 메인 돌아가기 버튼


    // 스크린샷을 위해 넣어놓은것들
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;

    private int screenDensity;
    private int screenWidth;
    private int screenHeight;

    private ActivityResultLauncher<Intent> captureLauncher;

    private ImageReader imageReader;


    // @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatch);

        dbHelper = new DatabaseHelper(this);

        // 그전 Intent()에서 가져온 내용을 가져옴
        before_intent = getIntent();
        name = before_intent.getStringExtra("name");
        personalColor = before_intent.getIntExtra("personalColorint", -1);

        // 상의 이미지뷰
        top_ImageView = findViewById(R.id.top_ImageView);
        // 하의 이미지뷰
        bottom_ImageView = findViewById(R.id.bottom_ImageView);

        // 상의 프레임 < > 버튼
        top_left_change_button = findViewById(R.id.top_ImageView_left_button);
        top_right_change_button = findViewById(R.id.top_ImageView_right_button);

        // 하의 프레임 < > 버튼
        bottom_left_change_button = findViewById(R.id.bottom_ImageView_left_button);
        bottom_right_change_button = findViewById(R.id.bottom_ImageView_right_button);

        // 상의 옷 색상 추천 버튼
        top_recommend_color_button1 = findViewById(R.id.top_recommend_color_button1);
        top_recommend_color_button2 = findViewById(R.id.top_recommend_color_button2);
        top_recommend_color_button3 = findViewById(R.id.top_recommend_color_button3);

        // 하의 옷 색상 추천 버튼
        bottom_recommend_color_button1 = findViewById(R.id.bottom_recommend_color_button1);
        bottom_recommend_color_button2 = findViewById(R.id.bottom_recommend_color_button2);
        bottom_recommend_color_button3 = findViewById(R.id.bottom_recommend_color_button3);

        // 저장 버튼
        save_button = findViewById(R.id.save_button);

        goback_button = findViewById(R.id.imageButton4);

        // 상의 담을 배열 생성
        topIcons = new ArrayList<>();
        // 하의 담을 배열 생성
        bottomIcons = new ArrayList<>();

        colorMapping = new HashMap<>();

        // 사진촬영기능을 위한 객체생성
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.densityDpi;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        initializeColorMapping();  // 색상 매핑 초기화
        updateColorButtons();      // 버튼 색상 업데이트
        setupColorButtonListeners(); // 버튼 이벤트 설정



        /*// OUTFIT테이블의 내용을 조회해 배열에 넣는 과정
        // cirsor.getColumnIndex에서 접근 시 -1의 반환으로 오류가 발생할 수 있음 이를 @SuppressLint("Range") 으로 차단함 인지 필요

        Cursor outfit_cursor = dbHelper.SELECT_ALL_OUTFIT();
        if (outfit_cursor != null && outfit_cursor.moveToFirst()) {
            do {
                OUTFIT outfit = new OUTFIT();
                outfit.setOUTFIT_ID(outfit_cursor.getInt(outfit_cursor.getColumnIndex("OUTFIT_ID")));
                outfit.setOUTFIT_TYPE(outfit_cursor.getInt(outfit_cursor.getColumnIndex("OUTFIT_TYPE")));
                outfit.setCLOTHES(outfit_cursor.getInt(outfit_cursor.getColumnIndex("CLOTHES")));
                outfit.setCOLOR(outfit_cursor.getString(outfit_cursor.getColumnIndex("COLOR")));
                outfit.setSEASON(outfit_cursor.getInt(outfit_cursor.getColumnIndex("SEASON")));
                outfit.setOUTFIT_CODE(outfit_cursor.getString(outfit_cursor.getColumnIndex("OUTFIT_CODE")));

                // Add to the appropriate list based on the type
                if (outfit.getOUTFIT_TYPE() == OUTFIT.TYPE_TOP) {
                    topIcons.add(outfit);
                } else if (outfit.getOUTFIT_TYPE() == OUTFIT.TYPE_BOTTOM) {
                    bottomIcons.add(outfit);
                }
            } while (outfit_cursor.moveToNext());
            outfit_cursor.close();
        }*/
/*

        // OUTFIT테이블의 내용을 조회해 배열에 넣는 과정
        // 조건: personalColor와 같은 season, 그리고 type별로 나누기


        Cursor topCursor = dbHelper.getOutfitsByTypeAndSeason(personalColor, 0);
        if (topCursor != null && topCursor.moveToFirst()) {
            do {
                OUTFIT outfit = new OUTFIT();
                outfit.setOUTFIT_ID(topCursor.getInt(topCursor.getColumnIndex("OUTFIT_ID")));
                outfit.setOUTFIT_TYPE(topCursor.getInt(topCursor.getColumnIndex("OUTFIT_TYPE")));
                outfit.setCLOTHES(topCursor.getInt(topCursor.getColumnIndex("CLOTHES")));
                outfit.setCOLOR(topCursor.getString(topCursor.getColumnIndex("COLOR")));
                outfit.setSEASON(topCursor.getInt(topCursor.getColumnIndex("SEASON")));
                outfit.setOUTFIT_CODE(topCursor.getString(topCursor.getColumnIndex("OUTFIT_CODE")));

                // Add to the topIcons list
                topIcons.add(outfit);
            } while (topCursor.moveToNext());
            topCursor.close();
        }

        Cursor bottomCursor = dbHelper.getOutfitsByTypeAndSeason(personalColor, 1);
        if (bottomCursor != null && bottomCursor.moveToFirst()) {
            do {
                OUTFIT outfit = new OUTFIT();
                outfit.setOUTFIT_ID(bottomCursor.getInt(bottomCursor.getColumnIndex("OUTFIT_ID")));
                outfit.setOUTFIT_TYPE(bottomCursor.getInt(bottomCursor.getColumnIndex("OUTFIT_TYPE")));
                outfit.setCLOTHES(bottomCursor.getInt(bottomCursor.getColumnIndex("CLOTHES")));
                outfit.setCOLOR(bottomCursor.getString(bottomCursor.getColumnIndex("COLOR")));
                outfit.setSEASON(bottomCursor.getInt(bottomCursor.getColumnIndex("SEASON")));
                outfit.setOUTFIT_CODE(bottomCursor.getString(bottomCursor.getColumnIndex("OUTFIT_CODE")));

                // Add to the bottomIcons list
                bottomIcons.add(outfit);
            } while (bottomCursor.moveToNext());
            bottomCursor.close();
        }
*/
        top_left_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIcon(1, isSelected = true);
            }
        });

        top_right_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIcon(1, isSelected = true);
            }
        });

        bottom_left_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIcon(-1, false);
            }
        });
        bottom_right_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIcon(1, false);
            }
        });


        // 안드로이드 이미지 캡쳐 저장 메서드
        save_button.setOnClickListener(view -> takeScreenshot());

        goback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(ColorMatchActivity.this, MainActivity.class);
                startActivity(goback);
                finish();
            }
        });
    }


    // 퍼스널컬러에 맞는 색깔 모음
    private void initializeColorMapping() {

        // 봄: 상의 / 하의
        colorMapping.put(0, new String[][]{
                {"#f98d9a", "#fff0b9", "#fbdcbd"},  // 상의
                {"#512d13", "#12559c", "#d2b482"}   // 하의
        });

        // 여름: 상의 / 하의
        colorMapping.put(1, new String[][]{
                {"#fffffb", "#b1c9e1", "#adb4bc"},  // 상의
                {"#325889", "#8e877f", "#6fa5d4"}   // 하의
        });

        // 가을: 상의 / 하의
        colorMapping.put(2, new String[][]{
                {"#b58c60", "#faf8e1", "#8a171a"},  // 상의
                {"#0e3963", "#a3bfcd", "#fcf8df"}   // 하의
        });

        // 겨울: 상의 / 하의
        colorMapping.put(3, new String[][]{
                {"#ffffff", "#050608", "#d9dadf"},  // 상의
                {"#140c45", "#050606", "#d9d0b1"}   // 하의
        });
    }

    private void updateColorButtons() {
        // 퍼스널 컬러에 맞는 상의/하의 색상 배열 가져오기
        String[][] colors = colorMapping.get(personalColor);

        if (colors != null) {
            // 상의 색상 버튼 업데이트
            top_recommend_color_button1.setBackgroundColor(Color.parseColor(colors[0][0]));
            top_recommend_color_button2.setBackgroundColor(Color.parseColor(colors[0][1]));
            top_recommend_color_button3.setBackgroundColor(Color.parseColor(colors[0][2]));

            // 하의 색상 버튼 업데이트
            bottom_recommend_color_button1.setBackgroundColor(Color.parseColor(colors[1][0]));
            bottom_recommend_color_button2.setBackgroundColor(Color.parseColor(colors[1][1]));
            bottom_recommend_color_button3.setBackgroundColor(Color.parseColor(colors[1][2]));
        }
    }


    private void setupColorButtonListeners() {
        String[][] colors = colorMapping.get(personalColor);

        if (colors != null) {
            // 상의 버튼 이벤트
            top_recommend_color_button1.setOnClickListener(v -> updateOutfitListByColor(colors[0][0], 0));
            top_recommend_color_button2.setOnClickListener(v -> updateOutfitListByColor(colors[0][1], 0));
            top_recommend_color_button3.setOnClickListener(v -> updateOutfitListByColor(colors[0][2], 0));

            // 하의 버튼 이벤트
            bottom_recommend_color_button1.setOnClickListener(v -> updateOutfitListByColor(colors[1][0], 1));
            bottom_recommend_color_button2.setOnClickListener(v -> updateOutfitListByColor(colors[1][1], 1));
            bottom_recommend_color_button3.setOnClickListener(v -> updateOutfitListByColor(colors[1][2], 1));
        }
    }


    @SuppressLint("Range") // Android Lint에서 Cursor 사용 시 발생하는 경고를 억제합니다.
    private void updateOutfitListByColor(String colorHex, int outfitType) {
        // 1. 데이터베이스에서 조건(colorHex와 outfitType)에 맞는 데이터를 가져옵니다.
        Cursor cursor = dbHelper.getOutfitsByColorAndType(colorHex, outfitType);

        // 2. targetList는 상의(topIcons) 또는 하의(bottomIcons) 배열을 참조합니다.
        // outfitType이 OUTFIT.TYPE_TOP(0)이라면 topIcons를 참조,
        // 그렇지 않다면 bottomIcons를 참조합니다.
        ArrayList<OUTFIT> targetList = (outfitType == 0) ? topIcons : bottomIcons;

        // 3. 기존 데이터를 모두 삭제하여 새로운 데이터로 갱신합니다.
        targetList.clear();                                 // 상의인지 하의인지 판단해서 targetList를 상의 배열 혹은 하의 배열로
        // 설정 그 다음 targetList를 삭제해야 새로운 데이터를 넣어 띄우기 때문

        // 4. 쿼리 결과가 있는 경우 데이터를 읽기 시작합니다.
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // 5. OUTFIT 객체를 생성하여 쿼리 결과의 각 행 데이터를 저장합니다.
                OUTFIT outfit = new OUTFIT();

                // 6. 각 컬럼 값을 가져와 OUTFIT 객체에 저장합니다.
                outfit.setOUTFIT_ID(cursor.getInt(cursor.getColumnIndex("OUTFIT_ID"))); // OUTFIT ID
                outfit.setOUTFIT_TYPE(cursor.getInt(cursor.getColumnIndex("OUTFIT_TYPE"))); // 상의 또는 하의 타입
                outfit.setCLOTHES(cursor.getInt(cursor.getColumnIndex("CLOTHES"))); // 옷 ID
                outfit.setCOLOR(cursor.getString(cursor.getColumnIndex("COLOR"))); // 색상
                outfit.setSEASON(cursor.getInt(cursor.getColumnIndex("SEASON"))); // 계절
                outfit.setOUTFIT_CODE(cursor.getString(cursor.getColumnIndex("OUTFIT_CODE"))); // 이미지 코드

                // 7. 완성된 OUTFIT 객체를 targetList에 추가합니다.
                targetList.add(outfit);
            } while (cursor.moveToNext()); // 다음 행으로 이동하며 데이터를 계속 읽습니다.

            // 8. Cursor를 닫아 리소스를 반환합니다.
            cursor.close();
        }

        Log.d("changeIcon", "topIcons size: " + topIcons.size() + ", bottomIcons size: " + bottomIcons.size());


        if (!targetList.isEmpty()) {
            String iconCode = targetList.get(0).getOUTFIT_CODE(); // 첫 번째 아이템의 이미지 코드 가져오기
            int resId = getResources().getIdentifier(iconCode, "drawable", getPackageName()); // 이미지 리소스 ID 가져오기

            if (outfitType == OUTFIT.TYPE_TOP) {
                topIcons = targetList; // 상의 리스트 갱신
                topIndex = 0; // 인덱스를 0으로 초기화
                top_ImageView.setImageResource(resId); // 상의 이미지뷰 업데이트
            } else {
                bottomIcons = targetList; // 하의 리스트 갱신
                bottomIndex = 0; // 인덱스를 0으로 초기화
                bottom_ImageView.setImageResource(resId); // 하의 이미지뷰 업데이트
            }
        } else {
            // 데이터가 없을 경우 기본 이미지를 설정
            if (outfitType == OUTFIT.TYPE_TOP) {
                top_ImageView.setImageResource(R.drawable.default_top); // 기본 상의 이미지
            } else {
                bottom_ImageView.setImageResource(R.drawable.default_bottom); // 기본 하의 이미지
            }
        }
    }

    // 좌우 버튼을 눌렀을 때 인덱스를 반복적으로 변경하는 메서드
    private void changeIcon(int direction, boolean isSelected) {
        if (isSelected) { // 상의가 선택된 경우
            if (!topIcons.isEmpty()) { // 리스트가 비어있지 않은 경우만 처리
                topIndex = (topIndex + direction + topIcons.size()) % topIcons.size(); // 반복 인덱스 계산
                updateSelectedIcon(true); // 상의 이미지뷰 업데이트
            }
        } else { // 하의가 선택된 경우
            if (!bottomIcons.isEmpty()) { // 리스트가 비어있지 않은 경우만 처리
                bottomIndex = (bottomIndex + direction + bottomIcons.size()) % bottomIcons.size(); // 반복 인덱스 계산
                updateSelectedIcon(false); // 하의 이미지뷰 업데이트
            }
        }
    }


    // 현재 선택된 의상의 아이콘을 업데이트

    private void updateSelectedIcon(boolean isSelected) {
        String iconCode;
        int resId;

        if (isSelected) { // 상의가 선택된 경우
            if (!topIcons.isEmpty()) { // 리스트가 비어있지 않은 경우만 처리
                iconCode = topIcons.get(topIndex).getOUTFIT_CODE();
                resId = getResources().getIdentifier(iconCode, "drawable", getPackageName()); // 이미지 리소스 ID 가져오기
                top_ImageView.setImageResource(resId); // 상의 이미지뷰 업데이트
            }
        } else { // 하의가 선택된 경우
            if (!bottomIcons.isEmpty()) { // 리스트가 비어있지 않은 경우만 처리
                iconCode = bottomIcons.get(bottomIndex).getOUTFIT_CODE();
                resId = getResources().getIdentifier(iconCode, "drawable", getPackageName()); // 이미지 리소스 ID 가져오기
                bottom_ImageView.setImageResource(resId); // 하의 이미지뷰 업데이트
            }
        }
    }


    // 이미지 저장 메서드
    private void takeScreenshot() {
        // 1. 현재 화면 캡처
        View rootView = getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        // 2. 파일 저장
        File screenshotFile = saveBitmap(bitmap);

        // 3. 공유를 위한 FileProvider URI 가져오기
        if (screenshotFile != null) {
            Uri screenshotUri = FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName(),
                    screenshotFile
            );

            // 4. 공유 Intent 실행
            shareScreenshot(screenshotUri);
        }
    }

    private File saveBitmap(Bitmap bitmap) {
        File screenshotFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Capture");
        if (!screenshotFolder.exists()) {
            screenshotFolder.mkdirs();
        }

        String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
        File screenshotFile = new File(screenshotFolder, fileName);

        try (FileOutputStream fos = new FileOutputStream(screenshotFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            // 갤러리에 이미지 추가
            addImageToGallery(screenshotFile);


            return screenshotFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void shareScreenshot(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }

    private void addImageToGallery(File screenshotFile) {
        // 미디어 스캐닝을 통해 갤러리에 추가
        MediaScannerConnection.scanFile(
                this,
                new String[]{screenshotFile.getAbsolutePath()},
                new String[]{"image/png"},
                (path, uri) -> {
                    // 갤러리 등록 완료 콜백
                    System.out.println("Image added to gallery: " + path);
                });
    }
}