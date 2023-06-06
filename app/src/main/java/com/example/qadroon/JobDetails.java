package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class JobDetails extends AppCompatActivity {
    String username,type;
    Memo Info;
    TextView title, fullname, add_date,qual,accepted, detail, salary;
    Button edit,apply,cancel;
    String URL = Server.ip + "apply.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        Info = (Memo) getIntent().getSerializableExtra("info");

        title = findViewById(R.id.mem_title);
        salary = findViewById(R.id.mem_salary);
        add_date = findViewById(R.id.mem_add);
        detail = findViewById(R.id.mem_details);
        fullname = findViewById(R.id.mem_username);
        qual = findViewById(R.id.mem_qual);
        accepted = findViewById(R.id.mem_accept);

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
                Intent intent = new Intent(JobDetails.this, AddJob.class);
                intent.putExtra("id", username);
                intent.putExtra("op_type", "edit");
                intent.putExtra("info", Info);

                startActivity(intent);
                finish();
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
        salary.setText(Info.getSalary());
        accepted.setText(Info.getAccepted());
        qual.setText(Info.getQualification());
        detail.setText(Info.getDetails());
        fullname.setText("By: " + Info.getUsername());
        add_date.setText(Info.getAdd_date());

    }
    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(JobDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", username);
                data.put("job_id", Info.getMemo_id());
                data.put("type", "Job Request");


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
