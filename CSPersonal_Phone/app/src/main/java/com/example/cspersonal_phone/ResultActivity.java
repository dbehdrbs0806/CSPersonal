package com.example.cspersonal_phone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    String name;                                      // 처리한 결과 데이터의 이름
    String timestamp;                                 // 처리한 결과 데이터의 시간
    int spring;                                       // 처리한 결과 봄 값
    int summer;                                       // 처리한 결과 여름 값
    int fall;                                         // 처리한 결과 가을 값
    int winter;                                       // 처리한 결과 겨울 값


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.resultTextView);                  // TextView 생성

        // Intent로부터 데이터 수신
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        timestamp = intent.getStringExtra("timestamp");
        spring = intent.getIntExtra("spring", 0);
        summer = intent.getIntExtra("summer", 0);
        fall = intent.getIntExtra("fall", 0);
        winter = intent.getIntExtra("winter", 0);

        /*// 데이터를 TextView에 출력 */
        resultTextView.setText();


        String personal_color = highest_color(spring, summer, fall, winter);



    }

    public String highest_color(int a, int b, int c, int d) {                   // Math.max 메소드를 사용해 가장 큰값을 알아냄
        int temp = Math.max(Math.max(a, b), Math.max(c, d));
        if (spring == temp) {
            return "spring";
        }
        else if(summer == temp) {
            return "summer";
        }
        else if(fall == temp) {
            return "fall";
        }
        else {
            return "winter";
        }
    }
}
