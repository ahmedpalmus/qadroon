package com.example.qadroon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ReportPage extends AppCompatActivity {
    private final String URL = Server.ip + "getusers.php";
    private final String URL3 = Server.ip + "getjobsdate.php";
    private final String URL2 = Server.ip + "gettraining.php";

    public ArrayList<UserProfile> memos;
    public ArrayList<Memo> infos;
    public ArrayList<Info> infos2;
    RadioButton jobs, train, comps, users;
    Button s_date, e_date, search;
    Calendar myCalendar;
    TextView r;
    LinearLayout l1, l2;
    String username = "admin", ddate1 = "", ddate2 = "", type;
    String usertype;
    ListView simpleList;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        jobs = findViewById(R.id.jobs);
        train = findViewById(R.id.training);
        comps = findViewById(R.id.companies);
        users = findViewById(R.id.users);
        s_date = findViewById(R.id.s_date);
        e_date = findViewById(R.id.e_date);
        search = findViewById(R.id.search);
        simpleList = findViewById(R.id.l3);
        r = findViewById(R.id.result);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        memos = new ArrayList<>();
        infos = new ArrayList<>();
        infos2 = new ArrayList<>();

        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (jobs.isChecked()) {
                    train.setChecked(false);
                    comps.setChecked(false);
                    users.setChecked(false);

                    type = "job";
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);

                }
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (train.isChecked()) {
                    jobs.setChecked(false);
                    comps.setChecked(false);
                    users.setChecked(false);
                    type = "train";
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                }
            }
        });
        comps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comps.isChecked()) {
                    jobs.setChecked(false);
                    train.setChecked(false);
                    users.setChecked(false);
                    type = "company";
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                }
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users.isChecked()) {
                    jobs.setChecked(false);
                    train.setChecked(false);
                    comps.setChecked(false);
                    type = "user";
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                }
            }
        });
        s_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(s_date);
            }
        });

        e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(e_date);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ddate1 = s_date.getText().toString();

                ddate2 = e_date.getText().toString();
                r.setText("");
                if (type.equals("user") || type.equals("company")) {
                    getUsers();
                } else if (type.equals("job")) {
                    if (ddate1.length() < 5 || ddate2.length() < 5) {
                        Toast.makeText(getApplicationContext(), "Enter valid dates", Toast.LENGTH_LONG).show();
                    } else if(isDateBefore(ddate2,ddate1)){
                        Toast.makeText(getApplicationContext(), "Start Date must be before End Date", Toast.LENGTH_LONG).show();
                    }else {
                        getJobs();
                    }

                } else if (type.equals("train")) {
                    if (ddate1.length() < 5 || ddate2.length() < 5) {
                        Toast.makeText(getApplicationContext(), "Enter valid dates", Toast.LENGTH_LONG).show();
                    } else if(isDateBefore(ddate2,ddate1)){
                        Toast.makeText(getApplicationContext(), "Start Date must be before End Date", Toast.LENGTH_LONG).show();
                    }else {
                        getTrain();
                    }
                }
            }
        });


    }

    public boolean isDateBefore(String date1String, String date2String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date1 = format.parse(date1String);
            Date date2 = format.parse(date2String);

            return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false; // Return a default value if an exception occurs
    }
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
        new DatePickerDialog(ReportPage.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void getUsers() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ReportPage.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("usertype", type);

                String result = con.sendPostRequest(URL, data);
                return result.trim();
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                memos.clear();
                String res1[] = new String[0];
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.user_view, R.id.item_n, res1);
                simpleList.setAdapter(adapter);
                adapter.clear();
                adapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Infos", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        memos = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {
                            JSONObject row = allReq.getJSONObject(i);
                            UserProfile temp = new UserProfile();

                            temp.setUsername(row.getString("username"));
                            temp.setFullname(row.getString("fullname"));
                            memos.add(temp);

                        }

                        String res[] = new String[memos.size()];
                        for (int j = 0; j < memos.size(); j++) {
                            res[j] = memos.get(j).getFullname();
                        }
                        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.user_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        r.setText("Total Users: " + memos.size());
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(ReportPage.this, Profile.class);
                                intent.putExtra("id", memos.get(position).getUsername());
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

    private void getJobs() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ReportPage.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();

                data.put("date1", ddate1);
                data.put("date2", ddate2);

                String result = con.sendPostRequest(URL3, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                infos.clear();
                String res1[] = new String[0];
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_view, R.id.item_n, res1);
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
                            Memo temp = new Memo();

                            temp.setMemo_id(row.getString("job_id"));
                            temp.setAdd_date(row.getString("add_date"));
                            temp.setDetails(row.getString("description"));
                            temp.setTitle(row.getString("title"));
                            temp.setFullname(row.getString("comp_name"));
                            temp.setSalary(row.getString("salary"));
                            temp.setAccepted(row.getString("accepted_disability"));
                            temp.setQualification(row.getString("qualification"));
                            temp.setStatus(row.getString("status"));
                            temp.setUsername(row.getString("company_id"));

                            infos.add(temp);
                        }

                        String res[] = new String[infos.size()];
                        for (int j = 0; j < infos.size(); j++) {
                            res[j] = infos.get(j).getTitle() + "\nAdded on : " + infos.get(j).getAdd_date();
                        }
                        r.setText("Total Jobs: " + infos.size());

                        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(ReportPage.this, JobDetails.class);
                                intent.putExtra("id", username);
                                intent.putExtra("info", infos.get(position));
                                intent.putExtra("type", "admin");

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

    private void getTrain() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ReportPage.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", "admin");
                data.put("type", "admin");

                String result = con.sendPostRequest(URL2, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                infos2.clear();
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
                        infos2 = new ArrayList<>();
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


                            infos2.add(temp);
                        }

                        String res[] = new String[infos2.size()];
                        for (int j = 0; j < infos2.size(); j++) {
                            res[j] =infos2.get(j).getTitle()+"\nAdded on : "+infos2.get(j).getAdd_date();
                        }
                        r.setText("Training Count: "+infos2.size());
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.item_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(ReportPage.this, TrainingDetail.class);
                                intent.putExtra("id","admin");
                                intent.putExtra("info", infos2.get(position));
                                intent.putExtra("type", "admin");

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