package com.example.cspersonal_phone;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.android.volley.RequestQueue;

public class LoadingActivity extends AppCompatActivity {

    static RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ProgressBar progressBar = findViewById(R.id.progressBar);          // ProgressBar 객체 생성
        progressBar.setIndeterminate(true);                                // progressbar를 비확정적 상태표시로 설정
        progressBar.setProgress(0);                                        // progress 시작을 0으로 설정

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ProgressWorker.class).build(); // WorkManager 작업 생성
        WorkManager.getInstance(this).enqueue(workRequest);                                     // WorkManager 작업 큐에 추가

        // WorkManager의 진행 상태 관찰
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                            // 진행 상태 데이터 수신
                            Data progress = workInfo.getProgress();
                            int progressStatus = progress.getInt("PROGRESS", 0);

                            // ProgressBar 업데이트
                            progressBar.setProgress(progressStatus);
                        }
                    }
                });





    }
}
