package ddwu.mobile.finalproject.ma02_20191003;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class PhaDetailActivity extends AppCompatActivity {
    public static final String TAG = "PhaDetailActivity";
    PhaInfoDto pid;
    private ArrayList<String> check;

    TextView dPhaName;
    TextView dPhaAddress;
    TextView dPhaPhone;
    CheckBox rbSave;

    PhaDBManager manager;

    // Google Map
    private GoogleMap mGoogleMap;
    private Marker centerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pha_detail);

        manager = new PhaDBManager(PhaDetailActivity.this);

        pid = (PhaInfoDto) getIntent().getSerializableExtra("pid");

        dPhaName = findViewById(R.id.dPhaName);
        dPhaAddress = findViewById(R.id.dPhaAddress);
        dPhaPhone = findViewById(R.id.dPhaPhone);
        rbSave = findViewById(R.id.rbSave);

        dPhaName.setText(pid.getName());
        dPhaAddress.setText(pid.getAddress());
        dPhaPhone.setText(pid.getPhone());
        if (manager.btnChecked(pid.getAddress())) rbSave.setChecked(true);

        // Google Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        // 관심 약국 표시
        rbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbSave.isChecked()) {
                    boolean result = manager.addPha(pid);
                    if (result) Toast.makeText(PhaDetailActivity.this, "약국 저장에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(PhaDetailActivity.this, "약국 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = manager.removePha(pid.getAddress());
                    if (result) Toast.makeText(PhaDetailActivity.this, "약국이 저장이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    else    Toast.makeText(PhaDetailActivity.this, "약국이 저장 취소에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;

            if (pid.getXpos() != null) {
                Double lontitude = Double.valueOf(pid.getXpos());
                Double latitude = Double.valueOf(pid.getYpos());

                LatLng currentLoc = new LatLng(latitude, lontitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

                MarkerOptions options = new MarkerOptions();
                options.position(currentLoc);
                options.title(pid.getName());
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                centerMarker = mGoogleMap.addMarker(options);
                centerMarker.showInfoWindow(); // 생략할 경우 마커를 터치해야 윈도우 나타남
            } else {
                Toast.makeText(PhaDetailActivity.this, "지도 정보 없음", Toast.LENGTH_LONG).show();
            }
        }
    };

}