package com.example.cspersonal_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    String name;                                      // 처리한 결과 데이터의 이름
    String timestamp;                                 // 처리한 결과 데이터의 시간
    int spring;                                       // 처리한 결과 봄 값
    int summer;                                       // 처리한 결과 여름 값
    int autumn;                                         // 처리한 결과 가을 값
    int winter;                                       // 처리한 결과 겨울 값

    String perosonalColor;

    Button Match_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();                  // 그전 Intent()에서 가져온 내용을 가져옴
        Match_button = findViewById(R.id.Match_button);

        dbHelper = new DatabaseHelper(this);

        name = intent.getStringExtra("name");
        timestamp = intent.getStringExtra("timestamp");
        spring = intent.getIntExtra("spring", 0);
        summer = intent.getIntExtra("summer", 0);
        autumn = intent.getIntExtra("autumn", 0);
        winter = intent.getIntExtra("winter", 0);

        perosonalColor = highest_color(spring, summer, autumn, winter);
        if (perosonalColor == "spring") {
            setContentView(R.layout.activity_spring);
        }
        else if (perosonalColor == "summer") {
            setContentView(R.layout.activity_summer);
        }
        else if (perosonalColor == "autumn") {
            setContentView(R.layout.activity_autumn);;
        }
        else if (perosonalColor == "winter") {
            setContentView(R.layout.activity_winter);
        }
        // dbHelper에 있는 insert 함수를 사용해 DB에 데이터 삽입
        dbHelper.INSERT_User(dbHelper.getWritableDatabase(), name, timestamp, spring, summer, autumn, winter, perosonalColor);

        Match_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ResultActivity.this, ColorMatchActivity.class);
                intent2.putExtra("name", name);
                intent2.putExtra("personalColor", perosonalColor);
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
