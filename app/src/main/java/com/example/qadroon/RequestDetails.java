package com.example.qadroon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RequestDetails extends AppCompatActivity {
    String username,type,response,action,phone_number;
    JobRequest Info;

    TextView  req_type,the_name,fullname,phone,disability,education,skills,experience,add_date,state,title;
EditText rep;
    Button del,accept,reject,call;
    String URL = Server.ip + "reply.php";
    String URL2 = Server.ip + "delrequest.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        Info = (JobRequest) getIntent().getSerializableExtra("info");

        req_type = findViewById(R.id.req_type);
        fullname = findViewById(R.id.fullname);
        the_name = findViewById(R.id.the_name);
        phone = findViewById(R.id.phone);
        disability = findViewById(R.id.disability);
        education = findViewById(R.id.education);
        skills = findViewById(R.id.skills);

        experience = findViewById(R.id.experience);
        add_date = findViewById(R.id.add_date);
        state = findViewById(R.id.state);
        title = findViewById(R.id.title);
        rep = findViewById(R.id.rep);
        del = findViewById(R.id.del);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        call = findViewById(R.id.call);

        if(type.equals("user")){
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
            del.setVisibility(View.VISIBLE);
            the_name.setText("Company");
            fullname.setText(Info.getCom_name());
            phone_number=Info.getComp_phone();

            rep.setEnabled(false);
        }else {
            del.setVisibility(View.GONE);
            fullname.setText(Info.getFullname());
            phone_number=Info.getPhone();
        }
        if(!Info.getState().equals("new")){
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
        }
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RequestDetails.this);
                alert.setTitle("Deleting your request");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SendDel();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response=rep.getText().toString().trim();
                action="Accepted";
                if(response.length()>3){
                    SendInfo();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Response", Toast.LENGTH_LONG).show();
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response=rep.getText().toString().trim();
                action="Rejected";
                if(response.length()>3){
                    SendInfo();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Response", Toast.LENGTH_LONG).show();
                }

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone_number));
                startActivity(intent);
            }
        });
        req_type.setText(Info.getReq_type());
        phone.setText(Info.getPhone());
        disability.setText(Info.getDisability());
        education.setText(Info.getEducation());
        skills.setText(Info.getSkills());

        experience.setText(Info.getExperience());
        add_date.setText(Info.getAdd_date());
        state.setText(Info.getState());
        title.setText(Info.getTitle());

    }
    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(RequestDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", username);
                data.put("req_id", Info.getReq_id());
                data.put("action", action);
                data.put("resp", response);
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
    private void SendDel() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(RequestDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("req_id", Info.getReq_id());

                String result = con.sendPostRequest(URL2, data);
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
