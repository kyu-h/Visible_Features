package com.example.b10715.final_pj;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PetEditActivity extends AppCompatActivity {
    String EDIT_URL = Config.URL + "pet_edit.php";
    String DELETE_URL = Config.URL + "pet_edit_delete.php";

    Button edit_pet_birth, btn_choose, btn_species_1, btn_species_2, btn_species_3, btn_pet_breed, btn_sex_female, btn_sex_male, btn_sex_etc, btn_enroll, btn_delete, btn_cancel;
    String[] arr;
    boolean[] mBoolean;

    EditText edit_pet_name, edit_pet_weight, edit_pet_number;

    private static String user_email, pet_species, pet_name_str, pet_birth_str, pet_breed, pet_sex, pet_weight_str, pet_number_str, pet_id, pet_image;

    private ImageView pet_img;
    private File image, cropFile;
    private String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}; //권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101;
    private Uri photoUri;
    private int mYear;
    private int mMonth;
    private int mDay;

    private static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수
    private static final int DIALOG_BREED = 4;
    private static final int DIALOG_BIRTH = 5;
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_edit);
        user_email = LoginActivity.call_email;
        checkPermissions(); // 권한불가

        pet_img = (ImageView) this.findViewById(R.id.pet_img);
        edit_pet_name = (EditText) findViewById(R.id.edit_pet_name);
        edit_pet_birth = (Button) findViewById(R.id.edit_pet_birth);
        edit_pet_weight = (EditText) findViewById(R.id.edit_pet_weight);
        edit_pet_number = (EditText) findViewById(R.id.edit_pet_number);
        btn_choose = (Button) this.findViewById(R.id.btn_choose);
        btn_species_1 = (Button) findViewById(R.id.btn_species_1);
        btn_species_2 = (Button) findViewById(R.id.btn_species_2);
        btn_species_3 = (Button) findViewById(R.id.btn_species_3);
        btn_pet_breed = (Button) findViewById(R.id.btn_pet_breed);
        btn_sex_female = (Button) findViewById(R.id.btn_sex_female);
        btn_sex_male = (Button) findViewById(R.id.btn_sex_male);
        btn_sex_etc = (Button) findViewById(R.id.btn_sex_etc);
        btn_enroll = (Button) findViewById(R.id.btn_enroll);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);


        Intent intent = getIntent();
        pet_id = intent.getExtras().getString("pet_id");
        if (intent.getExtras().getString("pet_image") != null) {
            pet_img.setImageURI(Uri.parse(intent.getExtras().getString("pet_image")));
        }
        edit_pet_name.setText(intent.getExtras().getString("pet_name"));
        edit_pet_birth.setText(intent.getExtras().getString("pet_birth"));
        btn_pet_breed.setText(intent.getExtras().getString("pet_breed"));
        edit_pet_weight.setText(intent.getExtras().getString("pet_weight"));
        edit_pet_number.setText(intent.getExtras().getString("pet_number"));

        arr = getResources().getStringArray(R.array.pet_breed);
        btn_species_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_species = btn_species_1.getText().toString();
            }
        });
        btn_species_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_species = btn_species_2.getText().toString();
            }
        });
        btn_species_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_species = btn_species_3.getText().toString();
            }
        });

        btn_sex_female.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_sex = btn_sex_female.getText().toString();
            }
        });
        btn_sex_male.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_sex = btn_sex_male.getText().toString();
            }
        });
        btn_sex_etc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_sex = btn_sex_etc.getText().toString();
            }
        });

        btn_enroll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pet_name_str = edit_pet_name.getText().toString();
                pet_weight_str = edit_pet_weight.getText().toString();
                pet_number_str = edit_pet_number.getText().toString();

/*

                if (pet_name_str.length() == 0 || pet_birth_str.length() == 0 || pet_species.length() == 0 || pet_breed.length() == 0 || pet_sex.length() == 0 || pet_weight_str.length() == 0 || pet_number_str.length() == 0) {

                    new AlertDialog.Builder(PetEditActivity.this).setTitle("알림").setMessage("필수정보가 입력되지 않았습니다").setPositiveButton("확인", null).show();

                } else {
*/

                insertToDatabase(pet_id, user_email, pet_name_str, pet_birth_str, pet_species, pet_breed, pet_sex, pet_weight_str, pet_number_str, pet_image);

                Intent enrollintent = new Intent(PetEditActivity.this, PetActivity.class);
                startActivity(enrollintent);

                Log.i("ddd", pet_name_str + pet_birth_str + pet_species + pet_breed + pet_sex + pet_weight_str + pet_number_str + pet_id + pet_image);

                //   }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Deletedatabase(pet_id);
                Intent deleteintent = new Intent(PetEditActivity.this, PetActivity.class);
                startActivity(deleteintent);
            }
        });


        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // updateDisplay();


        mBoolean = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            mBoolean[i] = false;
        }

    }

    /**
     * 카메라에서 사진 촬영
     */
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(PetEditActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(PetEditActivity.this,
                    "com.example.b10715.final_pj.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "KeePeT" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/KeePeT/"); //KeePeT이라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        galleryRefresh(String.valueOf(image));

        return image;

    }

    // 갤러리 새로고침, ACTION_MEDIA_MOUNTED는 하나의 폴더, FILE은 하나의 파일을 새로 고침할 때 사용함
    private void galleryRefresh(String imgFullPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imgFullPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {

            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
/*            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);*/
            intent.putExtra("scale", true);
            File croppedFileName = null;

            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/KeePeT/");
            cropFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(PetEditActivity.this,
                    "com.example.b10715.final_pj.provider", cropFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

            startActivityForResult(i, CROP_FROM_CAMERA);


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {

            Toast.makeText(PetEditActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == PICK_FROM_ALBUM) {

            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            cropImage();
            MediaScannerConnection.scanFile(PetEditActivity.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

        } else if (requestCode == CROP_FROM_CAMERA) {

            try {
                // bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출\.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

                //여기서는 ImageView에 setImageBitmap을 활용하여 그림을 띄우기.
                pet_img.setImageBitmap(thumbImage);

                //  cropFile.delete();
                pet_image = cropFile.toString();

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }

        }
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose:
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum();
                    }
                };

                new android.app.AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setNegativeButton("취소", cancelListener)
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .show();
                break;

            case R.id.edit_pet_birth:
                showDialog(DIALOG_BIRTH);
                break;

            case R.id.btn_pet_breed:
                showDialog(DIALOG_BREED);
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_BREED:
                return new AlertDialog.Builder(PetEditActivity.this).setTitle("품종 선택").setItems(R.array.pet_breed, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        btn_pet_breed.setText(arr[which]);
                        pet_breed = arr[which];
                        Log.i("pet_breed", pet_breed);
                    }
                }).setNegativeButton("닫기", null).create();

            case DIALOG_BIRTH:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            Log.i("DatePickerDialog", "DatePickerDialog");

            updateDisplay();

        }
    };

    // 설정된 날짜를 TextView에 출력
    private void updateDisplay() {
       /* int num1 = Integer.parseInt(String.valueOf(mMonth));
        int age = Calendar.MONTH - num1;*/

        edit_pet_birth.setText(
                new StringBuilder()
                        //월은 시스템에서 0~11로 인식하기 때문에 1을 더해줌
                        .append(mYear).append("년 ")
                        .append(mMonth + 1).append("월 ")
                        .append(mDay).append("일 ")
        );

        pet_birth_str = edit_pet_birth.getText().toString();

        Log.i("입력", edit_pet_birth.getText().toString());
    }


    private void insertToDatabase(String pet_id, String user_name, String pet_name_str, String pet_birth_str, String pet_species, String pet_breed, String pet_sex, String pet_weight_str, String pet_number_str, String pet_image) {

        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PetEditActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String pet_id = (String) params[0];
                    String user_email = (String) params[1];
                    String pet_name_str = (String) params[2];
                    String pet_birth_str = (String) params[3];
                    String pet_species = (String) params[4];
                    String pet_breed = (String) params[5];
                    String pet_sex = (String) params[6];
                    String pet_weight_str = (String) params[7];
                    String pet_number_str = (String) params[8];
                    String pet_image = (String) params[9];


                    String data = URLEncoder.encode("pet_id", "UTF-8") + "=" + URLEncoder.encode(pet_id, "UTF-8");
                    data += "&" + URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_name", "UTF-8") + "=" + URLEncoder.encode(pet_name_str, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_birth", "UTF-8") + "=" + URLEncoder.encode(pet_birth_str, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_species", "UTF-8") + "=" + URLEncoder.encode(pet_species, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_breed", "UTF-8") + "=" + URLEncoder.encode(pet_breed, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_sex", "UTF-8") + "=" + URLEncoder.encode(pet_sex, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_weight", "UTF-8") + "=" + URLEncoder.encode(pet_weight_str, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_number", "UTF-8") + "=" + URLEncoder.encode(pet_number_str, "UTF-8");
                    data += "&" + URLEncoder.encode("pet_image", "UTF-8") + "=" + URLEncoder.encode(pet_image, "UTF-8");

                    URL url = new URL(EDIT_URL);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(pet_id, user_name, pet_name_str, pet_birth_str, pet_species, pet_breed, pet_sex, pet_weight_str, pet_number_str, pet_image);
    }

    private void Deletedatabase(String pet_id) {

        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PetEditActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String pet_id = (String) params[0];

                    String data = URLEncoder.encode("pet_id", "UTF-8") + "=" + URLEncoder.encode(pet_id, "UTF-8");

                    Log.i("delete", pet_id);

                    URL url = new URL(DELETE_URL);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(pet_id);
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(
                    new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

    //권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다." +
                " 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }


}