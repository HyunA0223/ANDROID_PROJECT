package ddwu.mobile.finalproject.ma02_20191003;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Pharmacy extends AppCompatActivity {
    EditText etPhaName;
    ListView phaListView;
    ImageView pImg;

    String phaUrl;

    ArrayList<PhaInfoDto> resultList;
    PhaAdapter phaAdapter;

    PhaInfoParser parser;
    NetworkManager networkManager;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy);

        etPhaName = findViewById(R.id.etPhaName);
        phaListView = findViewById(R.id.medicListView);
        pImg = findViewById(R.id.pImg);

        phaUrl = getResources().getString(R.string.pha_url);

        resultList = new ArrayList();
        phaAdapter = new PhaAdapter(this, R.layout.listview_layout, resultList);
        phaListView.setAdapter(phaAdapter);

        parser = new PhaInfoParser();
        networkManager = new NetworkManager(this);

        // 항목 클릭 시 상세보기
        phaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhaInfoDto pid = resultList.get(position);
                Intent intent = new Intent(Pharmacy.this, PhaDetailActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnFindPha:
                pImg.setVisibility(pImg.GONE);
                query = etPhaName.getText().toString();
                try {
                    new BasicAsyncTask().execute(phaUrl + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    class BasicAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(Pharmacy.this, "Wait", "Finding...");
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
            resultList = parser.parse(result);
            phaAdapter.setList(resultList); // Adapter에 결과 List를 설정 후 notify
            progressDlg.dismiss();

        }
    }

}
