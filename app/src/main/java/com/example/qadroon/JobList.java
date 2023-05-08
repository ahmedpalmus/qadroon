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

public class JobList extends AppCompatActivity {
    Button new_member;
    String username;
    ListView simpleList;
    ArrayAdapter<String> adapter;
    private final String URL = Server.ip + "getjobs.php";


    public ArrayList<Memo> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        username = getIntent().getStringExtra("id");

        simpleList = findViewById(R.id.memo_list);
        infos = new ArrayList<>();

        new_member = findViewById(R.id.new_member);
        new_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobList.this, AddJob.class);
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
                loadingDialog = ProgressDialog.show(JobList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", username);

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
                            Memo temp=new Memo();

                            temp.setMemo_id(row.getString("job_id"));
                            temp.setAdd_date(row.getString("add_date"));
                            temp.setDetails(row.getString("details"));
                            temp.setTitle(row.getString("title"));
                            temp.setFullname(row.getString("fullname"));
                            temp.setSalary(row.getString("salary"));
                            temp.setAccepted(row.getString("accepted"));
                            temp.setQualification(row.getString("qualification"));
                            temp.setStatus(row.getString("status"));

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

                                Intent intent = new Intent(JobList.this, StoryDetails.class);
                                intent.putExtra("id",username);
                                intent.putExtra("info", infos.get(position));

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