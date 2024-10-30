package com.example.cspersonal_phone;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.main_view);             // 화면의 터치를 인식하기위해 layout 자체의 view 생성
        view.setOnTouchListener(new View.OnTouchListener() {  // view 객체의 callback TouchListener 생성
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {  // Touch action에 대해서 motion 생성 시
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });



    }

}