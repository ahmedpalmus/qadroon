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

public class AddStory extends AppCompatActivity {
    EditText etitle, eperson, edetail;
    Info memo;
    ImageView image;
    Button save, cancel, img_btn, audio_dtn, del;
    String URL = Server.ip + "addstory.php";
    TextView l1,l2,l3, l4;
    String id, title, person = "",Image = "none",Audio = "none" , details, op_type, memo_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        id = getIntent().getStringExtra("id");
        op_type = getIntent().getStringExtra("op_type");

        etitle = findViewById(R.id.fm_title);
        edetail = findViewById(R.id.fm_details);
        eperson = findViewById(R.id.fm_place);
        audio_dtn = findViewById(R.id.fm_audio_btn);
        img_btn = findViewById(R.id.fm_image_btn);
        image = findViewById(R.id.fm_image);
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
            eperson.setText(memo.getPerson());
            edetail.setText(memo.getDetails());
            memo_id = memo.getInfo_id();

            del.setVisibility(View.VISIBLE);
            getImage(memo.getImage(), image);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(AddStory.this);
                alert.setTitle("Deleting a story");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        op_type = "del";
                        title="";
                        person="";
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

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(400);
            }

        });

        audio_dtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }



    private void showFileChooser(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedimg = data.getData();
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap univLogo = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                univLogo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void getImage(final String img, final ImageView viewHolder) {

        class packTask extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap image1 = null;
                java.net.URL url = null;
                try {
                    url = new URL(Server.ip + img);
                    image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image1;
            }

            protected void onPostExecute(Bitmap image) {

                viewHolder.setImageBitmap(image);
            }
        }
        packTask t = new packTask();
        t.execute();
    }

    public void Save() {

        title = etitle.getText().toString().trim();
        person = eperson.getText().toString().trim();
        Audio = l4.getText().toString().trim();

        details = edetail.getText().toString().trim();

        l1.setTextColor(Color.BLACK);
        l2.setTextColor(Color.BLACK);
        l3.setTextColor(Color.BLACK);

        boolean err = false;
        if (title.length() < 2) {
            l1.setTextColor(Color.RED);
            err = true;
        }
        if (person.length() < 2) {
            l2.setTextColor(Color.RED);
            err = true;
        }
        if (details.length() < 5) {
            l3.setTextColor(Color.RED);
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
                loadingDialog = ProgressDialog.show(AddStory.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", id);
                data.put("title", title);
                data.put("person", person);
                data.put("details", details);
                data.put("op_type", op_type);
                data.put("memo_id", memo_id);
                data.put("audio", Audio);
                data.put("image", Image);

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