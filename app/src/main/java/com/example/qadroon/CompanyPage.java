package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompanyPage extends AppCompatActivity {
    String id;
    Button my_jobs,my_train,reqs,support,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);

        id=getIntent().getStringExtra("id");

        my_jobs=findViewById(R.id.user_jobs);
        my_train=findViewById(R.id.user_train);
        reqs=findViewById(R.id.user_reqs);
        support=findViewById(R.id.user_supp);
        profile=findViewById(R.id.user_prof);

        my_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyPage.this, JobList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "company");
                startActivity(intent);
            }
        });
        my_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyPage.this, TrainList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", "company");
                startActivity(intent);
            }
        });
        reqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyPage.this, RequestType.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", "company");
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyPage.this, Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "company");
                startActivity(intent);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CompanyPage.this,SupportList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","user");
                startActivity(intent);
            }
        });
    }
}