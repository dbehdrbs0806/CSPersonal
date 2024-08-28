package com.example.cspersonal_phone;

import android.content.Context;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ProgressWorker extends Worker {

    static RequestQueue requestQueue;        // volley 사용을 위한 rest api 요청을 담고 뱉을 요청 큐

    public ProgressWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String imageFilePath = // 이미지 경로를 저장


        // null일 때 Queue 초기화
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        });

        Gson gson = new Gson();
        Image image = gson.fromJson()





        for (int i = 0; i <= 100; i++) {   // 작업 로직: 0부터 100까지 Progress를 증가
            try {
                TimeUnit.MILLISECONDS.sleep(100);  // 100ms마다 진행률 증가
                setProgressAsync(new Data.Builder().putInt("PROGRESS", i).build());  // setProgressAsync(): 진행 상태를 MainActivity에 전달
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }
        return Result.success();
    }


    private String encodeImageToBase64(String imagePath) throws IOException {
        File file = new File(imagePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // 파일을 -1 끝까지 읽어 바이트 배열로 변환
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        fis.close();

        // Base64로 인코딩 후 문자열로 반환
        return Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
    }

    private void sendJson() {
        Image Jsonimage =
    }

}
