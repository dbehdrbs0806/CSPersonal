package com.example.cspersonal_phone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/*
public class test {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 먼저 핸드폰에 사진 촬영 기능이 있는지 확인
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))  {
                    // 카메라 하드웨어가 있는지 확인하는 조건
                    if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { // 권한이 없으면 요청
                        ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                    } else {
                        // 권한이 이미 부여되어 있으면 카메라 인텐트 실행
                        openCamera();
                    }
                } else {
                    // 디바이스에 카메라 기능이 없을 경우 처리
                    Toast.makeText(CameraActivity.this, "이 기기에는 카메라 기능이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

      권환 요청 후 사용자가 허용 및 거부 했을 때 자동으로 호출되는 method
     ActivityCompat.requsetPermissions() 메소드 실행 후 권환 확인 후 onRequestPermissionsResult() 가 실행됨
     @NonNull String[] permissions 요청한 권한의 목록 여러 권한을 요청할 수 있으므로 배열 형태로 제공
     @NonNull int[] grantResults: 각 권한 요청에 대한 결과를 나타내는 배열입니다. 배열의 각 요소는 해당 권한의 허용 여부를 나타냅니다. PackageManager.PERMISSION_GRANTED는 권한이 부여된 경우, PackageManager.PERMISSION_DENIED는 권한이 거부된 경우를 나타앰
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  요청한 코드가 즉 카메라 권환의 결과를 확인해 동일한 경우
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 카메라 인텐트를 실행
                openCamera();
            } else {
                // 권한이 거부된 경우 처리
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openCamera() {
        try {
            file = createImageFile();
            Intent intent_picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent_picture.resolveActivity(getPackageManager()) != null) {
                photoURI = FileProvider.getUriForFile(CameraActivity.this, "com.example.cspersonal_phone", file);
                intent_picture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("CameraActivity", "사진 촬영 인텐트 시작");
                startActivityResult.launch(intent_picture);
            }
            file.createNewFile();
        } catch (Exception e) {
            Log.e("CameraActivity", "사진 촬영 준비 중 오류 발생", e);
            e.printStackTrace();
        }
    }

    //이미지저장 메소드
    private void saveImg() {

        try {
            //저장할 파일 경로
            File storageDir = new File(getFilesDir() + "/capture");
            if (!storageDir.exists()) //폴더가 없으면 생성.
                storageDir.mkdirs();

            String filename = "캡쳐파일" + ".jpg";

            // 기존에 있다면 삭제
            File file = new File(storageDir, filename);
            boolean deleted = file.delete();
            Log.w(TAG, "Delete Dup Check : " + deleted);
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output); //해상도에 맞추어 Compress
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert output != null;
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "Captured Saved");
            Toast.makeText(this, "Capture Saved ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w(TAG, "Capture Saving Error!", e);
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();

        }
    }
    public class MainActivity extends AppCompatActivity {
    Button selecBtn;  // 선택 버튼
    Button Check;  // 확인 버튼
    String time = get_now_time();  // 현재 시간을 가져옴
    ImageView imageView;  // 이미지를 보여줄 ImageView
    private final String FileName = time + ".png";  // 현재 시간을 이용한 파일명 생성
    String ImageName;  // 이미지 파일명을 저장할 변수

    // 액티비티 생성 시 호출되는 메서드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);  // ImageView 초기화
        selecBtn = findViewById(R.id.selecBtn);  // 선택 버튼 초기화
        Check = findViewById(R.id.Check);  // 확인 버튼 초기화

        // "Check" 버튼에 대한 클릭 이벤트 리스너 설정
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MainActivity2로 이동하는 Intent 생성 및 이미지 파일명 전달
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("ImgName", ImageName);
                startActivity(intent);
            }
        });

        // "selecBtn" 버튼에 대한 클릭 이벤트 리스너 설정
        selecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);  // 갤러리에서 이미지를 선택하는 Intent 생성
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);  // 선택한 이미지를 처리하는 ActivityResultLauncher 실행
            }
        });
    }

    // ActivityResultLauncher를 초기화하고 결과를 처리하는 콜백 메서드 설정
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Intent intent = result.getData();
                            Uri fileUri = intent.getData();  // 선택한 이미지의 URI 가져오기
                            ContentResolver resolver = getContentResolver();  // ContentResolver로 이미지 데이터 읽기
                            try {
                                InputStream inputStream = resolver.openInputStream(fileUri);  // InputStream을 이용하여 이미지 읽기
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);  // InputStream을 Bitmap으로 변환
                                inputStream.close();  // InputStream 닫기
                                imageView.setImageURI(fileUri);  // ImageView에 선택한 이미지 설정
                                saveImage(bitmap);  // 선택한 이미지를 저장하는 메서드 호출
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );

    // 이미지를 저장하는 메서드
    private void saveImage(Bitmap bitmap) {
        ImageName = FileName;  // 이미지 파일명을 현재 시간으로 설정
        File file = new File(getCacheDir(), ImageName);  // 파일 경로 설정

        try {
            file.createNewFile();  // 새 파일 생성
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);  // 비트맵 이미지를 파일로 압축하여 저장
            out.close();  // 출력 스트림 닫기
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 현재 시간을 문자열 형태로 반환하는 메서드
    private String get_now_time() {
        long now = System.currentTimeMillis();  // 현재 시간을 가져온다.
        Date mDate = new Date(now);  // Date형식으로 고친다.
        // 날짜, 시간을 가져오고 싶은 형태로 가져올 수 있다.
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddhhmmss", Locale.KOREA);
        String now_time = simpleDate.format(mDate);  // 포맷에 맞게 변환한 현재 시간을 반환
        return now_time;
    }


// 촬영한 사진을 가지고 provider 해서 내부로 이동할 임시 사진파일 생성 함수
    private File createImageFile() throws IOException {
        // String TempFileName = new SimpleDateFormat("yyyyMMdd_HHMMSS").format(new Date());   // 현재 날짜와 시간을 형식에 맞게 저장
        // File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);                  // 외부 저장소의 사진 디렉토리 내용 가져옴
        File storageDir = new File(getFilesDir(), "Capture");
        // File image = File.createTempFile(TempFileName, ".jpg", storageDir);              // 찍은 사진을 임시로 저장할 객체 생성
        File image = new File(storageDir, "image_" + System.currentTimeMillis() + ".jpg");
        imageFilePath = image.getAbsolutePath();                                                // image 사진 파일의 경로를 저장
        return image;                                                                           // 각각의 정보를 가진 image 반환
    }
}



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
*/