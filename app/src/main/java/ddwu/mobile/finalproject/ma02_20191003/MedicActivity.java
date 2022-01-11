package ddwu.mobile.finalproject.ma02_20191003;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MedicActivity extends AppCompatActivity {

    final static String TAG = "MedicActivity";
    final static int REQ_CODE = 100;

    Intent intent;
    Intent alarmIntent;
    PendingIntent alarmPIntent;
    AlarmManager alarmManager;

    private ListView alarmListView;
    private ArrayList<MedicDto> resultList;
    MedicAdapter medicAdapter;
    MedicDBManager medicDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic);

        alarmListView = findViewById(R.id.alarmListView);
        resultList = new ArrayList();
        medicDBManager = new MedicDBManager(MedicActivity.this);

        medicAdapter = new MedicAdapter(MedicActivity.this, R.layout.medic_listview_layout, resultList);
        alarmListView.setAdapter(medicAdapter);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

//        항목 클릭 시 수정
        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MedicDto pid = resultList.get(position);
                Intent intent = new Intent(MedicActivity.this, MedicMemoActivity.class);
                Log.d(TAG, "id : " + id);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });

//        항목 롱클릭 시 삭제 (알람 중지 & 사진 삭제)
        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MedicActivity.this);
                builder.setTitle("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Boolean removeSuccess = medicDBManager.removeMedic(id);

                                // 알람 삭제
                                alarmIntent = new Intent(MedicActivity.this, BroadcastRepeatReceiver.class);
                                alarmPIntent = PendingIntent.getBroadcast(MedicActivity.this, (int)id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                if (alarmPIntent != null) alarmManager.cancel(alarmPIntent);

                                if (removeSuccess) {
                                    Toast.makeText(MedicActivity.this, "삭제에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    resultList.clear();
                                    resultList.addAll(medicDBManager.getAllMedic());
                                    medicAdapter.notifyDataSetChanged();
                                } else
                                    Toast.makeText(MedicActivity.this, "삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MedicActivity.this, "삭제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnMedicAlarm:
                intent = new Intent(MedicActivity.this, MedicMemoActivity.class);
                startActivity(intent);
                break;
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQ_CODE) {
//            switch (resultCode) {
//                case RESULT_OK:
//                    break;
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        resultList.clear();
        resultList.addAll(medicDBManager.getAllMedic());
        medicAdapter.notifyDataSetChanged();
    }
}