package com.example.qadroon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class StoryDetails extends AppCompatActivity {
    String username, type;
    Info Info;
    TextView title, fullname, add_date, detail, likecount, dislikecount;
    ImageView imageView;
    Button edit, cancel;
    RatingBar ratingBarshow, ratingBarsend;
    ImageButton like, dislike;
String Like;
float rate;
    private final String URL = Server.ip + "setlike.php";
    private final String URL2 = Server.ip + "setrate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
        username = getIntent().getStringExtra("id");
        Info = (Info) getIntent().getSerializableExtra("info");
        type = getIntent().getStringExtra("type");

        title = findViewById(R.id.mem_title);
        add_date = findViewById(R.id.mem_add);
        fullname = findViewById(R.id.mem_username);
        detail = findViewById(R.id.mem_details);
        imageView = findViewById(R.id.mem_image);
        edit = findViewById(R.id.fm_edit);
        cancel = findViewById(R.id.fm_cancel);

        likecount = findViewById(R.id.likeCount);
        dislikecount = findViewById(R.id.dislikeCount);
        ratingBarshow = findViewById(R.id.s_rate);
        ratingBarsend = findViewById(R.id.s_rating);
        like = findViewById(R.id.likeButton);
        dislike = findViewById(R.id.dislikeButton);


        if (type.equals("admin")) {
            edit.setVisibility(View.VISIBLE);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoryDetails.this, AddStory.class);
                intent.putExtra("id", username);
                intent.putExtra("op_type", "edit");
                intent.putExtra("info", Info);

                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setText(Info.getTitle());
        fullname.setText("About: " + Info.getPerson());
        add_date.setText(Info.getAdd_date());
        detail.setText(Info.getDetails());
        ratingBarshow.setRating(Float.parseFloat(Info.getRating()));
        likecount.setText(Info.getLikes());
        dislikecount.setText(Info.getDslikes());

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like="like";
                Sendlike();
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like="dislike";
                Sendlike();
            }
        });

        ratingBarsend.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate=ratingBarsend.getRating();
                SendRate();
            }
        });
        getImage(Info.getImage(), imageView);


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

    private void Sendlike() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(StoryDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("post_id", Info.getInfo_id());
                data.put("username", username);
                data.put("type",Like);

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
                }else if (result.equals("Exist")) {
                    Toast.makeText(getApplicationContext(), "Already Done", Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    if(Like.equals("like")){
                        int l=Integer.parseInt(Info.getLikes())+1;

                        likecount.setText(""+l);
                    }else{
                        int l=Integer.parseInt(Info.getDslikes())+1;

                        dislikecount.setText(l);

                    }

                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }
    private void SendRate() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(StoryDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("post_id", Info.getInfo_id());
                data.put("username", username);
                data.put("rate",rate+"");

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
                } else {

                    ratingBarshow.setRating((rate+Float.parseFloat(Info.getRating()))/2);

                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

}
