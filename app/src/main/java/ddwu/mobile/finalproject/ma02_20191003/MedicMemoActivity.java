package ddwu.mobile.finalproject.ma02_20191003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MedicMemoActivity extends AppCompatActivity {
    final static String TAG = "MedicMemoActivity";

    Boolean thisIsForUpdate = false;

    private final static int REQUEST_TAKE_PHOTO = 200;
    private String mCurrentPhotoPath;

    TextView tvDate;
    TextView tvTime;
    EditText etMedicName;
    ImageView ivPhoto;
    TextView etMedicMemo;

    long memoId;

    MedicDto updateMd;
    MedicDto md;
    MedicDBManager medicDBManager;

    PendingIntent sender = null;
    AlarmManager alarmManager = null;
    int alarmHour;
    int alarmMinute;

    boolean[] mSelectedItems;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_memo);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        ivPhoto = findViewById(R.id.ivPhoto);
        etMedicName = findViewById(R.id.etMedicName);
        etMedicMemo = findViewById(R.id.etMedicMemo);

        md = new MedicDto();
        medicDBManager = new MedicDBManager(this);

        // memo 수정
        if (getIntent().getSerializableExtra("id") != null) {
            thisIsForUpdate = true;
            memoId = (long) getIntent().getSerializableExtra("id");
//        Log.d(TAG, String.valueOf(memoId));
            updateMd = medicDBManager.searchMedic(memoId);

            tvDate.setText(updateMd.getDate()); // 날짜 불러오기
            tvTime.setText(updateMd.getTime()); // 시간 불러오기
            etMedicName.setText(updateMd.getName());    // 약 이름 불러오기
            etMedicMemo.setText(updateMd.getMemo());    // 약 메모 불러오기
//            사진 불러오기
            mCurrentPhotoPath = updateMd.getImage();
            Log.d(TAG, "Detail Memo = " + mCurrentPhotoPath);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            bmOptions.inJustDecodeBounds = false;
            
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            ivPhoto.setImageBitmap(bitmap);
        }

//        알람 등록 준비
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]); }
    }

    public void onClick(View v) {
        Intent intent = null;

        switch(v.getId()) {
            case R.id.btnDate:
                String[] itemList = {"일요일마다", "월요일마다", "화요일마다", "수요일마다", "목요일마다", "금요일마다", "토요일마다"};
                mSelectedItems = new boolean[]{false, false, false, false, false, false, false};
                builder = new AlertDialog.Builder(MedicMemoActivity.this);
                builder.setTitle("알람 반복을 설정하세요.")
                        .setMultiChoiceItems(itemList, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems[which] = true;
                                } else {
                                    mSelectedItems[which] = false;
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedItems = "";
                                for (int i = 0; i < mSelectedItems.length; i++) {
                                    if (mSelectedItems[i]) {
                                        selectedItems += itemList[i] + "\n";
                                    }
                                }
                                tvDate.setText(selectedItems);

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
            case R.id.btnTime:
                TimePickerDialog.OnTimeSetListener mTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tvTime.setText(hourOfDay + " : " + minute);
                                alarmHour = hourOfDay;
                                alarmMinute = minute;
                            }
                        };

                TimePickerDialog tDialog = new TimePickerDialog(this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mTimeSetListener, 0, 0, false);
                tDialog.show();

                break;
            case R.id.btnInsertPicture:
                dispatchTakePictureIntent();
                break;
            case R.id.btnSaveAlarm:
                if (mCurrentPhotoPath == null) {
                    Toast.makeText(MedicMemoActivity.this, "사진을 삽입해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etMedicName.getText().toString().equals("")) {
                    Toast.makeText(MedicMemoActivity.this, "복용 중인 약 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tvTime.getText().toString().equals("약 복용 시간을 설정해주세요")) {
                    Toast.makeText(MedicMemoActivity.this, "약 복용 시간을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                md.setName(etMedicName.getText().toString());
                md.setDate(tvDate.getText().toString());
                md.setTime(tvTime.getText().toString());
                Log.d(TAG, mCurrentPhotoPath);
                md.setImage(mCurrentPhotoPath);
                md.setMemo(etMedicMemo.getText().toString());

                boolean result = false;

                if (thisIsForUpdate) {
                    mSelectedItems = new boolean[]{false, false, false, false, false, false, false};
                    String[] date = tvDate.getText().toString().split("요일마다\n");
                    for (int i = 0; i < date.length; i++) {
                        if (date[i].equals("일")) mSelectedItems[0] = true;
                        else if (date[i].equals("월")) mSelectedItems[1] = true;
                        else if (date[i].equals("화")) mSelectedItems[2] = true;
                        else if (date[i].equals("수")) mSelectedItems[3] = true;
                        else if (date[i].equals("목")) mSelectedItems[4] = true;
                        else if (date[i].equals("금")) mSelectedItems[5] = true;
                        else if (date[i].equals("토")) mSelectedItems[6] = true;
                    }
                    md.set_id(memoId);
                    result = medicDBManager.updateMedic(md);
                }
                else result = medicDBManager.addMedic(md);

//              알람 설정
                Boolean once = false;
                if (tvDate.getText().toString().equals("약 복용 날짜를 설정해주세요") || tvDate.getText().toString().equals(""))
                    once = true;
                String[] time = tvTime.getText().toString().split(" : ");
                alarmHour = Integer.parseInt(time[0]);
                alarmMinute = Integer.parseInt(time[1]);

                Log.d(TAG, "once = " + once);

                Calendar alarmTime = Calendar.getInstance();    // btnDate로 설정한 날짜
                alarmTime.set(Calendar.HOUR_OF_DAY, alarmHour);
                alarmTime.set(Calendar.MINUTE, alarmMinute);
                alarmTime.set(Calendar.SECOND, 0);
                alarmTime.set(Calendar.MILLISECOND, 0);

                Log.d(TAG, alarmHour + " : " + alarmMinute);

                long oneday = 24 * 60 * 60 * 1000;// 24시간

                if (!once) {  // 반복 설정 했을 때
                    memoId = medicDBManager.searchId(md);
//                    Log.d(TAG, String.valueOf((int)memoId));
                    intent = new Intent(this, BroadcastRepeatReceiver.class);
                    intent.putExtra("weekday", mSelectedItems);
                    intent.putExtra("name", etMedicName.getText().toString());
                    intent.putExtra("memoId", memoId);

                    sender = PendingIntent.getBroadcast(this, (int)memoId, intent, PendingIntent.FLAG_UPDATE_CURRENT);    // 해당 db의 _id를 RequestCode로
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), oneday, sender);
                } else {  // 반복 설정 안했을 때
                    intent = new Intent(this, BroadcastOnceReceiver.class);
                    intent.putExtra("name", etMedicName.getText().toString());
                    intent.putExtra("memoId", memoId);

                    sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), sender);
                }

                if (result)
                    finish();
                else
                    Toast.makeText(this, "알람 생성 실패", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);       // strings.xml 에 채널명 기록
            String description = getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
            int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
            notificationManager.createNotificationChannel(channel);
        }
    }


    /* 여기부터 사진 저장 부분 */

    /*원본 사진 파일 저장*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photoFile != null) {
            Uri photoUri = FileProvider.getUriForFile(this,
                        "ddwu.mobile.finalproject.ma02_20191003.fileProvider",
                        photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    /*사진의 크기를 ImageView에서 표시할 수 있는 크기로 변경*/
    private void setPic() {
        // Get the dimensions of the View
        int targetW = ivPhoto.getWidth();
        int targetH = ivPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivPhoto.setImageBitmap(bitmap);
    }


    /*현재 시간 정보를 사용하여 파일 정보 생성*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "create Image file = " + mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }
}
