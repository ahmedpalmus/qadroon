package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Content extends AppCompatActivity {
    String id;
    Button user_videos,user_stories,user_train;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        id=getIntent().getStringExtra("id");

        user_videos=findViewById(R.id.user_videos);
        user_stories=findViewById(R.id.user_stories);
        user_train=findViewById(R.id.user_train);


        user_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Content.this, StoryList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", "admin");
                startActivity(intent);
            }
        });

        user_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Content.this, TrainingMaterialList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", "admin");
                startActivity(intent);
            }
        });
        user_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Content.this, VideoList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", "admin");
                startActivity(intent);
            }
        });
    }
}