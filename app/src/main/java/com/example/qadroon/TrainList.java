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

public class TrainList extends AppCompatActivity {
    Button new_member;
    String username,usertype;
    ListView simpleList;
    ArrayAdapter<String> adapter;
    private final String URL = Server.ip + "gettraining.php";

    public ArrayList<Info> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);
        username = getIntent().getStringExtra("id");
        usertype = getIntent().getStringExtra("usertype");

        simpleList = findViewById(R.id.memo_list);
        infos = new ArrayList<>();

        new_member = findViewById(R.id.new_member);

        if(!usertype.equals("company")){
            new_member.setVisibility(View.GONE);
        }

        new_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrainList.this, AddTraining.class);
                intent.putExtra("id",username);
                intent.putExtra("op_type","add");
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
                loadingDialog = ProgressDialog.show(TrainList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", username);
                data.put("type", usertype);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                infos.clear();
                String res1[] = new String[0];
                adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.item_view, R.id.item_n, res1);
                simpleList.setAdapter(adapter);
                adapter.clear();
                adapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Infos", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        infos = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {
                            JSONObject row = allReq.getJSONObject(i);
                            Info temp=new Info();


                            temp.setComp_title(row.getString("comp_name"));
                            temp.setInfo_id(row.getString("training_id"));
                            temp.setComp_id(row.getString("company_id"));
                            temp.setTitle(row.getString("title"));
                            temp.setAddress(row.getString("location"));
                            temp.setAdd_date(row.getString("add_date"));
                            temp.setDetails(row.getString("description"));
                            temp.setLat(row.getString("lat"));
                            temp.setLon(row.getString("lon"));
                            temp.setDisability(row.getString("accepted_disability"));
                            temp.setStart_date(row.getString("start_date"));
                            temp.setEnd_date(row.getString("end_date"));


                            infos.add(temp);
                        }

                        String res[] = new String[infos.size()];
                        for (int j = 0; j < infos.size(); j++) {
                            res[j] =infos.get(j).getTitle()+"\nAdded on : "+infos.get(j).getAdd_date();
                        }
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.item_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(TrainList.this, TrainingDetail.class);
                                intent.putExtra("id",username);
                                intent.putExtra("info", infos.get(position));
                                intent.putExtra("type", usertype);

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