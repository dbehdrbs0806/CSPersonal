package com.example.cspersonal_phone;

import android.content.Intent;
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

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        progressBar = findViewById(R.id.progressBar);                      // ProgressBar 객체 생성
        progressBar.setIndeterminate(true);                                // progressbar를 비확정적 상태표시로 설정
        progressBar.setProgress(0);                                        // progress 시작을 0으로 설정
        progressBar.setMax(100);

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ProgressWorker.class).build(); // WorkManager 작업 생성
        WorkManager.getInstance(this).enqueue(workRequest);                                     // WorkManager 작업 큐에 추가

        // WorkManager의 진행 상태 관찰
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            // 진행 중 상태일 때
                            if (workInfo.getState() == WorkInfo.State.RUNNING) {
                                // 진행 상태 데이터 수신
                                Data progressData = workInfo.getProgress();
                                int progress = progressData.getInt("PROGRESS", 0);

                                // ProgressBar 업데이트
                                progressBar.setProgress(progress);
                            }
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {                    // 완료되면 결과를 받음
                                Data outputData = workInfo.getOutputData();                           // Data 객체가 받은 결과
                                String name = outputData.getString("name");
                                String timestamp = outputData.getString("timestamp");
                                int spring = outputData.getInt("spring", 0);
                                int summer = outputData.getInt("summer", 0);
                                int autumn = outputData.getInt("autumn", 0);
                                int winter = outputData.getInt("winter", 0);
                                // 결과 데이터를 다음 Activity에 전달
                                Intent intent = new Intent(LoadingActivity.this, ResultActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("timestamp", timestamp);
                                intent.putExtra("spring", spring);
                                intent.putExtra("summer", summer);
                                intent.putExtra("autumn", autumn);
                                intent.putExtra("winter", winter);

                                startActivity(intent);                                                // 새로운 Activity로 이동
                                finish();                                                             // 현재 Activity 종료
                            }
                        }
                    }
                });
    }
}