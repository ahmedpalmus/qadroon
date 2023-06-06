package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RequestType extends AppCompatActivity {
    String id,usertype;
    Button job,train;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_type);

        id=getIntent().getStringExtra("id");
        usertype=getIntent().getStringExtra("usertype");

        job=findViewById(R.id.job);
        train=findViewById(R.id.train);


        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestType.this, RequestList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", usertype);
                intent.putExtra("op_type", "Job Request");
                startActivity(intent);
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestType.this, RequestList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", usertype);

                intent.putExtra("op_type", "Training Request");
                startActivity(intent);
            }
        });

    }
}