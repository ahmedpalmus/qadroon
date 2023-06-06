package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoList extends AppCompatActivity {
    String id,usertype;
    ListView simpleList;
    ArrayAdapter<String> adapter;

    public ArrayList<Vid> posts;
    private final String URL = Server.ip + "getvideos.php";

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        id=getIntent().getStringExtra("id");
        usertype=getIntent().getStringExtra("usertype");
        add = findViewById(R.id.new_member);

        if(!usertype.equals("admin")){
            add.setVisibility(View.GONE);
        }
        simpleList = findViewById(R.id.memo_list);
        posts = new ArrayList<>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoList.this, AddVideo.class);
                intent.putExtra("id",id);
                intent.putExtra("op_type","add");
                intent.putExtra("usertype",usertype);
                startActivity(intent);
            }
        });

        getInfos();
    }

    protected void onResume() {
        super.onResume();
        getInfos();
    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(VideoList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", id);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                posts.clear();
                String res1[] = new String[0];
                adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.vid_view, R.id.item_n, res1);
                simpleList.setAdapter(adapter);
                adapter.clear();
                adapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Infos", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        posts = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {
                            JSONObject row = allReq.getJSONObject(i);
                            Vid temp=new Vid();

                            temp.setVideo_id(row.getString("video_id"));
                            temp.setTitle(row.getString("title"));
                            temp.setDetails(row.getString("details"));
                            temp.setVid_url(row.getString("vid_url"));
                            temp.setAdd_time(row.getString("add_time"));

                            posts.add(temp);

                        }

                        String res[] = new String[posts.size()];
                        for (int j = 0; j < posts.size(); j++) {
                            res[j] =posts.get(j).getTitle()+"\n"+posts.get(j).getAdd_time();
                        }
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.vid_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(VideoList.this, VideoDetails.class);
                                intent.putExtra("id",id);
                                intent.putExtra("post", posts.get(position));
                                intent.putExtra("usertype",usertype);

                                startActivity(intent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }
}