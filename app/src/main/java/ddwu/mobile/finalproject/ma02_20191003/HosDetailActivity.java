package ddwu.mobile.finalproject.ma02_20191003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HosDetailActivity extends AppCompatActivity {

    HosInfoDto hid;

    String hosCode;
    String detailInfoUrl;

    TextView dHosName; // 병원 이름 => hospInfoService1/getHospBasisList1
    TextView dHosAddress; // 병원 주소 => hospInfoService1/getHospBasisList1
    TextView dRcvSat; // 병원 운영 시간 => medicInsttDetailInfoService/getDetailInfo
    TextView dRcvWeek;
    TextView dNoTrSun; // 병원 휴진 안내 => medicInsttDetailInfoService/getDetailInfo
    TextView dNoTrHol;
    CheckBox hosSave;

    HosDetailParser detailParser;
    NetworkManager networkManager;

    HosDBManager hosDBManager;
    HosInfoDto hosDetail;

    private GoogleMap mGoogleMap;
    private Marker centerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_detail);

        hid = (HosInfoDto) getIntent().getSerializableExtra("hid");
        hosCode = hid.getCode(); // 병원 ykiho => 사용하여 정보 검색

        dHosName = findViewById(R.id.dHosName);
        dHosAddress = findViewById(R.id.dHosAddress);
        dRcvSat = findViewById(R.id.dRcvSat);
        dRcvWeek = findViewById(R.id.dRcvWeek);
        dNoTrSun = findViewById(R.id.dNoTrSun);
        dNoTrHol = findViewById(R.id.dNoTrHol);
        hosSave = findViewById(R.id.hosSave);

        detailParser = new HosDetailParser();
        networkManager = new NetworkManager(this);

        hosDBManager = new HosDBManager(HosDetailActivity.this);

        // parsing
        detailInfoUrl = getResources().getString(R.string.getDetailInfo); // 병원 운영시간, 휴진 안내
        new DetailAsyncTask().execute(detailInfoUrl + hosCode);

        // 데이터 출력
        dHosName.setText(hid.getName());
        dHosAddress.setText(hid.getAddress());
        if (hosDBManager.btnChecked(hid.getAddress()))  hosSave.setChecked(true);

        // Google Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        // 관심 병원 표시
        hosSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hosSave.isChecked()) {
                    boolean result = hosDBManager.addHos(hid);
                    if (result) Toast.makeText(HosDetailActivity.this, "병원 저장에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(HosDetailActivity.this, "병원 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = hosDBManager.removeHos(hid.getAddress());
                    if (result) Toast.makeText(HosDetailActivity.this, "병원 저장이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    else    Toast.makeText(HosDetailActivity.this, "병원 저장 취소에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    class DetailAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(HosDetailActivity.this, "Wait", "Finding...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            String address = strings[0];
            // networking
            result = networkManager.downloadContents(address);
            if (result == null) return "Error!";

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // parsing
            hosDetail = detailParser.parse(result);
            dNoTrHol.setText(hosDetail.getNoTrHol());
            dNoTrSun.setText(hosDetail.getNoTrSun());
            dRcvSat.setText(hosDetail.getRcvSat());
            dRcvWeek.setText(hosDetail.getRcvWeek());
            progressDlg.dismiss();

        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;

            if (hid.getXPos() != null) {
                Double lontitude = Double.valueOf(hid.getXPos());
                Double latitude = Double.valueOf(hid.getYPos());

                LatLng currentLoc = new LatLng(latitude, lontitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

                MarkerOptions options = new MarkerOptions();
                options.position(currentLoc);
                options.title(hid.getName());
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                centerMarker = mGoogleMap.addMarker(options);
                centerMarker.showInfoWindow(); // 생략할 경우 마커를 터치해야 윈도우 나타남
            } else {
                Toast.makeText(HosDetailActivity.this, "지도 정보 없음", Toast.LENGTH_LONG).show();
            }
        }
    };

}