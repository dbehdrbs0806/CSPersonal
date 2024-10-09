package com.example.cspersonal_phone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.resultTextView);

        // Intent로부터 데이터 수신
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String timestamp = intent.getStringExtra("timestamp");
        int spring = intent.getIntExtra("spring", 0);
        int summer = intent.getIntExtra("summer", 0);
        int fall = intent.getIntExtra("fall", 0);
        int winter = intent.getIntExtra("winter", 0);

        // 데이터를 TextView에 출력
        String resultText = "Name: " + name + "\nTimestamp: " + timestamp +
                "\nSpring: " + spring + "\nSummer: " + summer +
                "\nFall: " + fall + "\nWinter: " + winter;
        resultTextView.setText(resultText);
    }
}
