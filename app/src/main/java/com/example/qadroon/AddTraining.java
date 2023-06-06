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

public class AddTraining extends AppCompatActivity {
    EditText etitle, elocation, edetail,eaccepted;
    Info memo;
    Button save, cancel, sdate_btn, edate_dtn,map, del;
    String URL = Server.ip + "addtraining.php";
    TextView l1,l2,l3, l4;
    String id,location="",lat="",lon="", s_date="",e_date="",title="",accepted_d="", details="", op_type, memo_id = "0";
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        id = getIntent().getStringExtra("id");
        op_type = getIntent().getStringExtra("op_type");

        etitle = findViewById(R.id.po_title);
        edetail = findViewById(R.id.po_details);
        elocation = findViewById(R.id.po_place);
        eaccepted = findViewById(R.id.fm_accepted);
        sdate_btn = findViewById(R.id.po_start);
        edate_dtn = findViewById(R.id.po_end);
        map = findViewById(R.id.po_map);
        del = findViewById(R.id.fm_del);
        save = findViewById(R.id.fm_save);
        cancel = findViewById(R.id.fm_cancel);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);

        if (op_type.equals("edit")) {
            memo = (Info) getIntent().getSerializableExtra("info");
            etitle.setText(memo.getTitle());
            elocation.setText(memo.getPerson());
            eaccepted.setText(memo.getPerson());
            edetail.setText(memo.getDetails());
            memo_id = memo.getInfo_id();
            lat=memo.getLat();
            lon=memo.getLon();
            s_date=memo.getStart_date();
            e_date=memo.getEnd_date();

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
                AlertDialog.Builder alert = new AlertDialog.Builder(AddTraining.this);
                alert.setTitle("Deleting a story");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        op_type = "del";
                        title="";
                        location="";
                        lat="";
                        lon="";
                        s_date="";
                        e_date="";
                        details="";
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

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddTraining.this, MapActivity.class);
                launchMap.launch(intent);
            }

        });

        sdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(sdate_btn);

            }
        });

        edate_dtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(edate_dtn);


            }
        });
    }
    // Create lanucher variable inside onAttach or onCreate or global
    ActivityResultLauncher<Intent> launchMap = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    lat = data.getStringExtra("lat");
                    lon = data.getStringExtra("lon");
                }
            }
        }
    });
    public void setDate(Button vdate) {
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                vdate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(AddTraining.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void Save() {

        title = etitle.getText().toString().trim();
        location = elocation.getText().toString().trim();
        accepted_d = eaccepted.getText().toString().trim();
        s_date = sdate_btn.getText().toString().trim();
        e_date = edate_dtn.getText().toString().trim();
        details = edetail.getText().toString().trim();

        l1.setTextColor(Color.BLACK);
        l2.setTextColor(Color.BLACK);
        l3.setTextColor(Color.BLACK);
        l4.setTextColor(Color.BLACK);

        boolean err = false;
        if (title.length() < 2) {
            l1.setTextColor(Color.RED);
            err = true;
        }
        if (location.length() < 2) {
            l2.setTextColor(Color.RED);
            err = true;
        }
        if (details.length() < 5) {
            l3.setTextColor(Color.RED);
            err = true;
        } if (lon.length() < 5) {
            map.setTextColor(Color.RED);
            err = true;
        } if (s_date.equalsIgnoreCase("Set Start Date")) {
            sdate_btn.setTextColor(Color.RED);
            err = true;
        } if (e_date.equalsIgnoreCase("Set End Date")) {
            edate_dtn.setTextColor(Color.RED);
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
                loadingDialog = ProgressDialog.show(AddTraining.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", id);
                data.put("title", title);
                data.put("location", location);
                data.put("details", details);
                data.put("op_type", op_type);
                data.put("memo_id", memo_id);
                data.put("s_date", s_date);
                data.put("e_date", e_date);
                data.put("lat", lat);
                data.put("lon", lon);
                data.put("accepted_d", accepted_d);

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