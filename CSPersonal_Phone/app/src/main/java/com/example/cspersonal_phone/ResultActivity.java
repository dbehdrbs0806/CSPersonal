package com.example.cspersonal_phone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// 미완성
// 필요한 내용: DB 생성 및 사용 / 퍼스널컬러 결정 / 퍼스널컬러에 알맞는 레이아웃 선택
// 수정해야할 사항:
public class ResultActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    String name;                                      // 처리한 결과 데이터의 이름
    String timestamp;                                 // 처리한 결과 데이터의 시간
    int spring;                                       // 처리한 결과 봄 값
    int summer;                                       // 처리한 결과 여름 값
    int autumn;                                         // 처리한 결과 가을 값
    int winter;                                       // 처리한 결과 겨울 값

    String perosonalColor;
    int personalColorint;                             // 퍼스널컬러 int값

    ImageButton Match_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();                  // 그전 Intent()에서 가져온 내용을 가져옴
        dbHelper = new DatabaseHelper(this);

        name = intent.getStringExtra("name");
        timestamp = intent.getStringExtra("timestamp");
        spring = intent.getIntExtra("spring", 0);
        summer = intent.getIntExtra("summer", 0);
        autumn = intent.getIntExtra("autumn", 0);
        winter = intent.getIntExtra("winter", 0);


        Log.d("Color", "Spring value: " + spring);
        Log.d("Color", "Summer value: " + summer);
        Log.d("Color", "Autumn value: " + autumn);
        Log.d("Color", "Winter value: " + winter);

        perosonalColor = highest_color(spring, summer, autumn, winter);

        if (perosonalColor == "spring") {
            setContentView(R.layout.activity_spring);
            Match_button = findViewById(R.id.imageButton0);
            personalColorint = 0;
        }
        else if (perosonalColor == "summer") {
            setContentView(R.layout.activity_summer);
            Match_button = findViewById(R.id.imageButton1);
            personalColorint = 1;
        }
        else if (perosonalColor == "autumn") {
            setContentView(R.layout.activity_autumn);
            Match_button = findViewById(R.id.imageButton2);
            personalColorint = 2;
        }
        else if (perosonalColor == "winter") {
            setContentView(R.layout.activity_winter);
            Match_button = findViewById(R.id.imageButton3);
            personalColorint = 3;
        }
        // dbHelper에 있는 insert 함수를 사용해 DB에 데이터 삽입
        dbHelper.INSERT_User(dbHelper.getWritableDatabase(), name, timestamp, spring, summer, autumn, winter, perosonalColor);

        Match_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ResultActivity.this, ColorMatchActivity.class);
                intent2.putExtra("name", name);
                intent2.putExtra("personalColorint", personalColorint);
                startActivity(intent2);
                finish();
            }
        });
    }

    public String highest_color(int a, int b, int c, int d) {                   // Math.max 메소드를 사용해 가장 큰값을 알아냄
        int temp = Math.max(Math.max(a, b), Math.max(c, d));
        if (spring == temp) {
            return "spring";
        }
        else if(summer == temp) {
            return "summer";
        }
        else if(autumn == temp) {
            return "autumn";
        }
        else {
            return "winter";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();                                               // DatabaseHelper 닫기
        }
    }
}
