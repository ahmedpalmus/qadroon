package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserPage extends AppCompatActivity {
    String id;
    Button my_jobs,user_stories,user_videos,user_train,user_reqs,support,profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        id=getIntent().getStringExtra("id");

        my_jobs=findViewById(R.id.user_jobs);
        user_stories=findViewById(R.id.user_stories);
        user_videos=findViewById(R.id.user_videos);
        user_train=findViewById(R.id.user_train);
        user_reqs=findViewById(R.id.user_reqs);
        support=findViewById(R.id.user_supp);
        profile=findViewById(R.id.user_prof);

        my_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, JobList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        user_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, StoryList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", "user");
                startActivity(intent);
            }
        });
        user_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        user_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        user_reqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}