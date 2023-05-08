package com.example.qadroon;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddJob extends AppCompatActivity {
    EditText etitle, esalary,eaccept, equal, edetail;
    Memo memo;
    Button save, cancel, del;
    String URL = Server.ip + "addjob.php";
    TextView l1,l2,l3, l4,l5;
    String id, title, salary = "", accept = "", qualification = "",details, op_type, memo_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        id = getIntent().getStringExtra("id");
        op_type = getIntent().getStringExtra("op_type");
        etitle = findViewById(R.id.fm_title);
        edetail = findViewById(R.id.fm_desc);
        esalary = findViewById(R.id.fm_salary);
        eaccept = findViewById(R.id.fm_accepted);
        equal = findViewById(R.id.fm_qual);

        del = findViewById(R.id.fm_del);
        save = findViewById(R.id.fm_save);
        cancel = findViewById(R.id.fm_cancel);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        l5 = findViewById(R.id.l5);

        if (op_type.equals("edit")) {
            memo = (Memo) getIntent().getSerializableExtra("info");
            etitle.setText(memo.getTitle());
            esalary.setText(memo.getSalary());
            equal.setText(memo.getQualification());
            eaccept.setText(memo.getAccepted());
            edetail.setText(memo.getDetails());
            memo_id = memo.getMemo_id();
            del.setVisibility(View.VISIBLE);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddJob.this);
                alert.setTitle("Deleting a job add");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        op_type = "del";
                        title="";
                        details="";
                        salary="";
                        qualification="";
                        accept="";
                        SendInfo();
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void Save() {

        title = etitle.getText().toString().trim();
        salary = esalary.getText().toString().trim();
        accept = eaccept.getText().toString().trim();
        qualification = equal.getText().toString().trim();
        details = edetail.getText().toString().trim();

        l1.setTextColor(Color.BLACK);
        l2.setTextColor(Color.BLACK);
        l3.setTextColor(Color.BLACK);
        l4.setTextColor(Color.BLACK);
        l5.setTextColor(Color.BLACK);

        boolean err = false;
        if (title.length() < 2) {
            l1.setTextColor(Color.RED);
            err = true;
        }
        if (salary.length() < 2) {
            l2.setTextColor(Color.RED);
            err = true;
        }
        if (accept.length() < 5) {
            l3.setTextColor(Color.RED);
            err = true;
        }
        if (qualification.length() < 5) {
            l4.setTextColor(Color.RED);
            err = true;
        }
        if (details.length() < 5) {
            l4.setTextColor(Color.RED);
            err = true;
        }
        if (!err) {
            SendInfo();
        } else {
            Toast.makeText(getApplicationContext(), "Enter all the required fields", Toast.LENGTH_LONG).show();
        }


    }

    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(AddJob.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", id);
                data.put("title", title);
                data.put("salary", salary);
                data.put("details", details);
                data.put("op_type", op_type);
                data.put("memo_id", memo_id);
                data.put("qualification", qualification);
                data.put("accepted", accept);


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