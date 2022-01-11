package ddwu.mobile.finalproject.ma02_20191003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MyActivity extends AppCompatActivity {

    ListView listView;
    RadioButton myPhaBtn;
    RadioButton myHosBtn;
    ImageView img;

    ArrayList<PhaInfoDto> phaList;
    ArrayList<HosInfoDto> hosList;
    PhaAdapter phaAdapter;
    HosAdapter hosAdapter;
    PhaDBManager phaDBManager;
    HosDBManager hosDBManager;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        listView = findViewById(R.id.medicListView);
        myPhaBtn = findViewById(R.id.myPhaBtn);
        myHosBtn = findViewById(R.id.myHosBtn);
        img = findViewById(R.id.img);

        phaList = new ArrayList();
        hosList = new ArrayList();
        phaDBManager = new PhaDBManager(MyActivity.this);
        hosDBManager = new HosDBManager(MyActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myHosBtn.isChecked()) {
                    HosInfoDto hid = hosList.get(position);
                    Intent intent = new Intent(MyActivity.this, HosDetailActivity.class);
                    intent.putExtra("hid", hid);
                    startActivity(intent);
                } else {
                    PhaInfoDto pid = phaList.get(position);
                    Intent intent = new Intent(MyActivity.this, PhaDetailActivity.class);
                    intent.putExtra("pid", pid);
                    startActivity(intent);
                }
            }
        });
        
        // MY HOS 라디오 버튼 클릭 시 저장한 병원 표시
        myHosBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (myHosBtn.isChecked()) {
                    img.setVisibility(img.GONE);
                    hosAdapter = new HosAdapter(MyActivity.this, R.layout.listview_layout, hosList);
                    listView.setAdapter(hosAdapter);

                    hosList.clear();
                    hosList.addAll(hosDBManager.getAllHos());
                    hosAdapter.notifyDataSetChanged();
                } else {
                    hosList.clear();
                    hosAdapter.notifyDataSetChanged();
                }
            }
        });

        // MY PHA 라디오 버튼 클릭 시 저장한 약국 표시
        myPhaBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (myPhaBtn.isChecked()) {
                    img.setVisibility(img.GONE);
                    phaAdapter = new PhaAdapter(MyActivity.this, R.layout.listview_layout, phaList);
                    listView.setAdapter(phaAdapter);

                    phaList.clear();
                    phaList.addAll(phaDBManager.getAllPha());
                    phaAdapter.notifyDataSetChanged();
                } else {
                    phaList.clear();
                    phaAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.medicBtn:
                intent = new Intent(MyActivity.this, MedicActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myHosBtn.isChecked()) {
            hosList.clear();
            hosList.addAll(hosDBManager.getAllHos());
            hosAdapter.notifyDataSetChanged();
        } else if (myPhaBtn.isChecked()){
            phaList.clear();
            phaList.addAll(phaDBManager.getAllPha());
            phaAdapter.notifyDataSetChanged();
        }
    }
}