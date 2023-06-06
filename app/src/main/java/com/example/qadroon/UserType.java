package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserType extends AppCompatActivity {
    String id;
    Button prov,user,comp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        id=getIntent().getStringExtra("id");

        prov=findViewById(R.id.provs);
        user=findViewById(R.id.users);
        comp=findViewById(R.id.comps);


        prov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserType.this,SupportList.class);
                intent.putExtra("id",id);
                intent.putExtra("type","admin");
                startActivity(intent);

            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserType.this, ManageUsers.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "company");
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserType.this, ManageUsers.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
    }
}