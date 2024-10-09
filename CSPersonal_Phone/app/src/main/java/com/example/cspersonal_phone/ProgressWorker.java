package com.example.cspersonal_phone;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ProgressWorker extends Worker {

    static RequestQueue requestQueue;        // volley 사용을 위한 rest api 요청을 담고 뱉을 요청 큐
    String imageFilePath;                    // 사용할 이미지의 경로
    // String base64Image;                   // 이미지를 base64인코딩 처리한 문자열 값
    String jsonImage;

    static final String url = "http://220.69.209.126:5070/personalcolor";

    // 서버 응답 받은 데이터
    private String name;                     // 받은 데이터의 name
    private String timestamp;                // 받은 데이터의 timestamp
    private int spring;                      // 받은 데이터의 spring 값
    private int summer;                      // 받은 데이터의 summer 값
    private int fall;                        // 받은 데이터의 fall 값
    private int winter;                      // 받은 데이터의 winter 값



    public ProgressWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        if (requestQueue == null) {                                                     // requestQueue 사용을 위해 Queue를 초기화
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    @NonNull
    @Override
    public Result doWork() {                                                            // 백그라운드 처리될 내용의 함수
        imageFilePath = CameraActivity.imageFilePath;                                   // 이미지 경로를 static 변수를 사용해 가져옴
        try {
            String base64Image = encodeImageToBase64();                                 // try-catch를 통해 base64를 통해 이미지 인코딩
            jsonImage = saveTojson(base64Image);

            // 3. 서버와 통신 및 응답 처리 (기존 handleResult 메서드 사용)
            handleResult();

            // 4. 응답 대기 중 진행률 업데이트
            for (int i = 0; i <= 99; i++) {
                Thread.sleep(100);  // 대기 시간 동안 진행률 증가
                setProgressAsync(new Data.Builder().putInt("PROGRESS", i).build());
            }

            // 5. 응답이 올 때까지 대기
            while (name == null) {
                Thread.sleep(100);
            }
            // 응답 완료 후 진행률 100% 업데이트
            setProgressAsync(new Data.Builder().putInt("PROGRESS", 100).build());

            // 6. 성공 결과 반환

            Data outputData = new Data.Builder()
                    .putString("name", name)
                    .putString("timestamp", timestamp)
                    .putInt("spring", spring)
                    .putInt("summer", summer)
                    .putInt("fall", fall)
                    .putInt("winter", winter)
                    .build();

            return Result.success(outputData);  // 결과를 WorkManager로 전달

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Result.failure();
        }

        /*for (int i = 0; i <= 100; i++) {                                               // 작업 로직: 0부터 100까지 Progress를 증가
            try {
                TimeUnit.MILLISECONDS.sleep(100);                               // 100ms마다 진행률 증가
                setProgressAsync(new Data.Builder().putInt("PROGRESS", i).build());    // setProgressAsync(): 진행 상태를 MainActivity에 전달
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }*/
    }

    private String encodeImageToBase64() throws IOException {                        // base64를 사용한 이미지 인코딩 함수
        int bytesRead;
        File file = new File(imageFilePath);                                         // 매개변수의 경로를 통해 File 객체 생성
        FileInputStream fis = new FileInputStream(file);                             // File을 읽기위해 FileInputStream 생성
        ByteArrayOutputStream bos = new ByteArrayOutputStream();                     // 데이터를 바이트배열로 저장해 변환 함수를 위한 객체 ByteArrayOutputStream 생성
        byte[] buffer = new byte[1024];                                              // FileInputStream으로 읽은 파일을 임시 저장할 배열

        // 파일을 -1 끝까지 읽어 바이트 배열로 변환
        while ((bytesRead = fis.read(buffer)) != -1) {                               // FileInputStream 으로 파일을 -1까지 읽어
            bos.write(buffer, 0, bytesRead);                                     // ByteArrayOutputStream 객체의 write() 통해 buffer에 저장
        }
        fis.close();                                                                 // Base64로 인코딩 후 문자열로 반환
        return Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
    }

    private String saveTojson(String base64Image) {
        Gson gson = new Gson(); ;                                                    // Gson 사용을 위한 객체 생성
        String fileName = new File(imageFilePath).getName();                         // 이미지 파일로부터 이름 얻기
        BitmapFactory.Options options = new BitmapFactory.Options();                 // BitmapFactory를 사용하여 이미지의 가로와 세로를 얻기
        options.inJustDecodeBounds = true;                                           // 이미지를 실제로 로드하지 않고 크기만 얻음
        BitmapFactory.decodeFile(imageFilePath, options);
        int width = options.outWidth;
        int height = options.outHeight;

        Image gsonImage = new Image(fileName,base64Image, width, height);            // Image 객체 생성
        // ImageData 객체를 JSON 문자열로 변환
        String json = gson.toJson(gsonImage);
        // JSON 문자열 반환
        return json;
    }

    private void handleResult() {                                                    // 결과 전송 함수
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,    // Post 형식으로 rest api 보낼 내용 작성
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {                        // 서버로부터의 응답(response)을 JSON 형태로 처리
                        try {
                            JSONObject jsonResponse = new JSONObject(response);      // jsonResponse 가 받은

                            // 서버에서 전달된 데이터를 추출
                            name = jsonResponse.getString("name");
                            timestamp = jsonResponse.getString("timestamp");
                            spring = jsonResponse.getInt("spring");
                            summer = jsonResponse.getInt("summer");
                            fall = jsonResponse.getInt("fall");
                            winter = jsonResponse.getInt("winter");
                            // jsonResponse.get()  // 내용 가져와서



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {                                       // 오류 발생 시 실행 될 부분
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.getMessage()); // 오류 처리
                        Toast.makeText(getApplicationContext(), "Error: " +error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonImage.getBytes();                                         // JSON 데이터를 서버로 보냄
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";                            // Content-Type 설정
            }
        };

        requestQueue.add(stringRequest);                                             // 요청 큐에 추가하여 비동기 요청 수행
    }

}