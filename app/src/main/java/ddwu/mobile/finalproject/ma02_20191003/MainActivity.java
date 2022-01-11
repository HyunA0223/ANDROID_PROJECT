package ddwu.mobile.finalproject.ma02_20191003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.goToHos:
                intent = new Intent(MainActivity.this, Hospital.class);
                startActivity(intent);
                break;
            case R.id.goToDrug:
                intent = new Intent(MainActivity.this, Pharmacy.class);
                startActivity(intent);
                break;
            case R.id.goToMy:
                intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
                break;
        }

    }
}