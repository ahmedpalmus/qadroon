package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageUsers extends AppCompatActivity {
    String id,usertype;
    Button ad_update,ad_del;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        id=getIntent().getStringExtra("id");
        usertype=getIntent().getStringExtra("type");

        ad_update=findViewById(R.id.ad_update);
        ad_del=findViewById(R.id.ad_del);


        ad_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageUsers.this, UserList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", usertype);
                intent.putExtra("op_type", "update");
                startActivity(intent);
            }
        });
        ad_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageUsers.this, UserList.class);
                intent.putExtra("id", id);
                intent.putExtra("usertype", usertype);

                intent.putExtra("op_type", "del");
                startActivity(intent);
            }
        });

    }
}