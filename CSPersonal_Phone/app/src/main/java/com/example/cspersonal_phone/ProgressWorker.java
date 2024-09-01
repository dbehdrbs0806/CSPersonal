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
    String imageFilePath;                    // 사용할 이미지의 경로
    String base64Image;                      // 이미지를 base64인코딩 처리한 문자열 값
    Gson gson;                               // Gson 사용을 위한 객체 생성

    public ProgressWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (requestQueue == null) {                                       // requestQueue 사용을 위해 Queue를 초기화
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        gson = new Gson();
        imageFilePath = CameraActivity.imageFilePath;                     // 이미지 경로를 static 변수를 사용해 가져옴
        try {
            base64Image = encodeImageToBase64(imageFilePath);                           // try-catch를 통해 base64를 통해 이미지 인코딩



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


       /* StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        });

        Gson gson = new Gson();*/
        // Image image = gson.fromJson()



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


    private String encodeImageToBase64(String imagePath) throws IOException {        // base64를 사용한 이미지 인코딩 함수
        int bytesRead;
        File file = new File(imagePath);                                             // 매개변수의 경로를 통해 File 객체 생성
        FileInputStream fis = new FileInputStream(file);                             // File을 읽기위해 FileInputStream 생성
        ByteArrayOutputStream bos = new ByteArrayOutputStream();                     // 데이터를 바이트배열로 저장해 변환 함수를 위한 객체 ByteArrayOutputStream 생성
        byte[] buffer = new byte[1024];                                              // FileInputStream으로 읽은 파일을 임시 저장할 배열

        // 파일을 -1 끝까지 읽어 바이트 배열로 변환
        while ((bytesRead = fis.read(buffer)) != -1) {                               // FileInputStream 으로 파일을 -1까지 읽어
            bos.write(buffer, 0, bytesRead);                                     // ByteArrayOutputStream 객체의 write() 통해 buffer에 저장
        }
        fis.close();

        // Base64로 인코딩 후 문자열로 반환
        return Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
    }

    private void saveTojson() {

    }

}
