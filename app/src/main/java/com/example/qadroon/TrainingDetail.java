package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class TrainingDetail extends AppCompatActivity {
    String username,type,lat,lon;
    Info Info;
    TextView title, fullname, add_date,address,accepted, detail, start_d,end_d;
    Button map,edit,apply,cancel;
    String URL = Server.ip + "apply.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        Info = (Info) getIntent().getSerializableExtra("info");

        title = findViewById(R.id.mem_title);
        address = findViewById(R.id.mem_address);
        add_date = findViewById(R.id.mem_add);
        detail = findViewById(R.id.mem_details);
        fullname = findViewById(R.id.mem_username);
        start_d = findViewById(R.id.mem_sdate);
        end_d = findViewById(R.id.mem_edate);
        accepted = findViewById(R.id.mem_accept);

        map = findViewById(R.id.po_map);
        edit = findViewById(R.id.fm_edit);
        apply = findViewById(R.id.fm_apply);
        cancel = findViewById(R.id.fm_cancel);

        if(!type.equals("user")){
            edit.setVisibility(View.VISIBLE);
            apply.setVisibility(View.GONE);
        }else {
            edit.setVisibility(View.GONE);
            apply.setVisibility(View.VISIBLE);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrainingDetail.this, AddTraining.class);
                intent.putExtra("id", username);
                intent.putExtra("op_type", "edit");
                intent.putExtra("info", Info);
                startActivity(intent);
                finish();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMap(Info.getLat(),Info.getLon());
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SendInfo();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setText(Info.getTitle());
        address.setText(Info.getAddress());
        accepted.setText(Info.getDisability());
        start_d.setText(Info.getStart_date());
        end_d.setText(Info.getEnd_date());
        detail.setText(Info.getDetails());
        fullname.setText("By: " + Info.getComp_title());
        add_date.setText(Info.getAdd_date());
        lat=Info.getLat();
        lon=Info.getLon();

    }
    public void OpenMap(String lat,String lon){

        double latitude = Double.parseDouble(lat); // The latitude value
        double longitude = Double.parseDouble(lon); // The longitude value
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
   /* // Create a Uri object with the coordinates and marker
    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Customer Location)");

    // Create an Intent with the action and Uri
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

    // Set the package to the Google Maps app
    mapIntent.setPackage("com.google.android.apps.maps");

    // Verify if there is an app available to handle the Intent
    if (mapIntent.resolveActivity(getPackageManager()) != null) {
        // Start the activity
        startActivity(mapIntent);
    }*/
    }

    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(TrainingDetail.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", username);
                data.put("job_id", Info.getInfo_id());
                data.put("type", "Training Request");


                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

}
