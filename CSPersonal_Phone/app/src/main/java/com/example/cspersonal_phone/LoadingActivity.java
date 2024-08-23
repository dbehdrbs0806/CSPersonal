package com.example.cspersonal_phone;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

public class LoadingActivity extends AppCompatActivity {

    static RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ProgressBar progressBar = findViewById(R.id.progressBar);          // ProgressBar 객체 생성
        progressBar.setIndeterminate(false);                               // progressbar를 확정적 상태표시로 설정
        progressBar.setProgress(0);                                        // progress 시작을 0으로 설정





    }

}
