package com.example.cspersonal_phone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ColorMatchActivity extends AppCompatActivity {

    String name;
    String personalColor;

    private DatabaseHelper dbHelper;        // DB의 사용
    private ImageView top_ImageView;        // 상의 이미지 뷰
    private ImageView bottom_ImageView;     // 하의 이미지 뷰
    private ArrayList<OUTFIT> topIcons;          // 상의 아이콘 리스트
    private ArrayList<OUTFIT> bottomIcons;       // 하의 아이콘 리스트
    private int topIndex = 0;               // 상의 배열 인덱스
    private int bottomIndex = 0;            // 하의 배열 인덱스

    private boolean isSelected = true;      // 상의 선택 여부 변수

    ImageButton left_change_button;         // 왼쪽 버튼
    ImageButton right_change_button;        // 오른쪽 버튼
    ImageButton recommend_color_button1;    // 추천 옷 버튼1
    ImageButton recommend_color_button2;    // 추천 옷 버튼2
    ImageButton recommend_color_button3;    // 추천 옷 버튼3
    ImageButton save_button;                // 옷 저장 버튼

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatch);

        dbHelper = new DatabaseHelper(this);

        top_ImageView = findViewById(R.id.top_ImageView);
        bottom_ImageView = findViewById(R.id.bottom_ImageView);
        left_change_button = findViewById(R.id.left_change_button);
        right_change_button = findViewById(R.id.right_change_button);
        recommend_color_button1 = findViewById(R.id.recommend_color_button1);
        recommend_color_button2 = findViewById(R.id.recommend_color_button2);
        recommend_color_button3 = findViewById(R.id.recommend_color_button3);
        save_button = findViewById(R.id.save_button);

        topIcons = new ArrayList<>();
        bottomIcons = new ArrayList<>();

        Intent intent = getIntent();                              // 그전 Intent()에서 가져온 내용을 가져옴
        name = intent.getStringExtra("name");
        personalColor = intent.getStringExtra("personalColor");

        // 리스너를 ImageView에 설정
        top_ImageView.setOnTouchListener(imageTouchListener);
        bottom_ImageView.setOnTouchListener(imageTouchListener);

        OUTFIT outfit = new OUTFIT();


        // OUTFIT테이블의 내용을 조회해 배열에 넣는 과정
        // cirsor.getColumnIndex에서 접근 시 -1의 반환으로 오류가 발생할 수 있음 이를 @SuppressLint("Range") 으로 차단함 인지 필요
        Cursor cursor = dbHelper.SELECT_ALL_OUTFIT();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                outfit.setOUTFIT_ID(cursor.getInt(cursor.getColumnIndex("OUTFIT_ID")));
                int outfitType = cursor.getInt(cursor.getColumnIndex("OUTFIT_TYPE"));
                outfit.setOUTFIT_TYPE(outfitType);
                outfit.setOUTFIT_CODE(cursor.getString(cursor.getColumnIndex("OUTFIT_CODE")));

                if (outfitType == OUTFIT.TYPE_TOP) {
                    topIcons.add(outfit);  // 상의 아이콘 추가
                } else if (outfitType == OUTFIT.TYPE_BOTTOM) {
                    bottomIcons.add(outfit);  // 하의 아이콘 추가
                }
            } while (cursor.moveToNext());
            cursor.close();
        }


        left_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIcon(1);
            }
        });

        right_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIcon(-1);
            }
        });

    }

    // METHOD
    // 상의와 하의 ImageView에 공통으로 사용할 터치 리스너 생성
    private View.OnTouchListener imageTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v.getId() == R.id.top_ImageView) {
                    isSelected = true;                                  // 상의가 선택됨
                } else if (v.getId() == R.id.bottom_ImageView) {
                    isSelected = false;                                 // 하의가 선택됨
                }
                return true;
            }
            return false;
        }
    };

    // OUTFIT의 개수에 따라(4) 돌아가며 반복선택을 하기위한 버퍼 형식 계산함수
    private void changeIcon(int direction) {
        if (isSelected) {                                               // 상의가 선택된 경우
            topIndex = (topIndex + direction + topIcons.size()) % topIcons.size();
            updateSelectedIcon(isSelected);
        } else {                                                        // 하의가 선택된 경우
            bottomIndex = (bottomIndex + direction + bottomIcons.size()) % bottomIcons.size();
            updateSelectedIcon(isSelected);
        }
    }

    // 현재 선택된 의상의 아이콘을 업데이트
    private void updateSelectedIcon(boolean isSelected) {              // isSelected의 bool형에 따라 상하의 선택을 판단 배열의 Index를 통해 옷의 아이콘 code를 iconCode 에 대입
        String iconCode;
        int resId;
        if (isSelected) {
            iconCode = topIcons.get(topIndex).getOUTFIT_CODE();
            resId = getResources().getIdentifier(iconCode, "drawable", getPackageName());    // iconCode의 이름으로 된 이미지를 drawable에서 객체로(getResources()) 가져
            top_ImageView.setImageResource(resId);                                                  // layout 에서 컴포넌트의 옷 이미지를 바꿈
        }
        else {
            iconCode = bottomIcons.get(bottomIndex).getOUTFIT_CODE();
            resId = getResources().getIdentifier(iconCode, "drawable", getPackageName());
            bottom_ImageView.setImageResource(resId);
        }
    }

}