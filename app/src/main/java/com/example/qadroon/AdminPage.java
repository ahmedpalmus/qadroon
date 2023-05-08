package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {
    String id;
    Button my_jobs,user_content,user_manage,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        id=getIntent().getStringExtra("id");

        my_jobs=findViewById(R.id.user_jobs);
        user_content=findViewById(R.id.user_content);
        user_manage=findViewById(R.id.user_manage);

        profile=findViewById(R.id.user_prof);

        my_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, JobList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "admin");
                startActivity(intent);
            }
        });
        user_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, Content.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        user_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}