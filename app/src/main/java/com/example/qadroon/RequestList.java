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

public class RequestList extends AppCompatActivity {
    String username,type,op_type;
    ListView simpleList;
    ArrayAdapter<String> adapter;
    private final String URL = Server.ip + "getreqs.php";

    public ArrayList<JobRequest> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("usertype");
        op_type = getIntent().getStringExtra("op_type");


        simpleList = findViewById(R.id.memo_list);
        infos = new ArrayList<>();



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
                loadingDialog = ProgressDialog.show(RequestList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", username);
                data.put("type", type);
                data.put("op_type", op_type);

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
                            JobRequest temp=new JobRequest();

                            temp.setUsername(row.getString("username"));
                            temp.setFullname(row.getString("fullname"));
                            temp.setPhone(row.getString("phone"));
                            temp.setDisability(row.getString("disability"));
                            temp.setEducation(row.getString("education"));
                            temp.setSkills(row.getString("skills"));
                            temp.setExperience(row.getString("experience"));
                            temp.setReq_id(row.getString("req_id"));
                            temp.setAdd_date(row.getString("add_date"));
                            temp.setState(row.getString("state"));
                            temp.setTitle(row.getString("title"));
                            temp.setReq_type(row.getString("req_type"));
                            temp.setCom_name(row.getString("com_name"));
                            temp.setComp_phone(row.getString("comp_phone"));

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

                                Intent intent = new Intent(RequestList.this, RequestDetails.class);
                                intent.putExtra("id",username);
                                intent.putExtra("info", infos.get(position));
                                intent.putExtra("type", type);

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