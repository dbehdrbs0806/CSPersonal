package com.example.cspersonal_phone;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.os.Bundle;

// 완성
// 필요한 내용: 시작 Main activity / 화면 touch 다음 layout으로 넘어가는 이벤트 필요 /
// 수정해야할 사항: X

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                  // activity_main layout 불러옴
        View view = findViewById(R.id.main_view);                // 화면 터치 인식을 위한 View 객체 찾아와 생성
        view.setOnTouchListener(new View.OnTouchListener() {     // view 객체의 callback TouchListener 생성
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {      // boolean형 onTouch() 생성 MotionEvent의 다양한 모션 이벤트 사용
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {     // Touch action에 대해서 motion 생성 시
                    Intent camera_intent = new Intent(MainActivity.this, CameraActivity.class);   // 다음 layout으로 넘어감
                    startActivity(camera_intent);                                                              // 다음 layout 시작
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}