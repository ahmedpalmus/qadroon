package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StoryDetails extends AppCompatActivity {
    String username,type;
    Info Info;
    TextView title, fullname, add_date, detail;
    ImageView imageView;
    Button edit,cancel;

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

if(type.equals("admin")){
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
}
