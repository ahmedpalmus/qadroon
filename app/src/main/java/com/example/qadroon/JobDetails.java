package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class JobDetails extends AppCompatActivity {
    String username,type;
    Memo Info;
    TextView title, fullname, add_date,qual,accepted, detail, salary;
    Button edit,apply,cancel;

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

}
