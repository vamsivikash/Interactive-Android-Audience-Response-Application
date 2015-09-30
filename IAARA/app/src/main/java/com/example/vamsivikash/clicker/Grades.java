package com.example.vamsivikash.clicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.LinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Grades extends ActionBarActivity {

    GetIP IP = new GetIP();
    public static String studentId;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP()+"/getGrades.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";
    LinearLayout layout,lLayoutScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        // Get the grades of the student in his previous tests
        studentId = getIntent().getExtras().getString("studentId");
        Log.e("Student ID ", "ID" +studentId);
        layout = (LinearLayout) findViewById(R.id.lLayout);
        new getGrades().execute();
    }


    public class getGrades extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            Log.e("IP ", "ADDRESS" + IP.getIP());
            int success;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                Log.e("Student ID","GRADES"+studentId);
                params.add(new BasicNameValuePair("studentId", studentId));

                // Passing the username and password as a POST which is secured.
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // If the executed Query is correct you get a reply of 1 else 0
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    int rowCount = json.getInt("rowcount");
                    if(rowCount != 0){

                        // Dynamically create text views at run time and assign the scores and the test ID's

                        final TextView[] rb = new TextView[15];
                        final TextView[] score = new TextView[15];

                        for (int i = 0; i < rowCount;i++) {
                            String tmptestId = json.getString("test"+i);
                            String tmpScore = json.getString("score"+i);
                            Log.e("Test ID","temp"+tmptestId);
                            Log.e("Score","temp"+tmpScore);
                            rb[i] = new TextView(Grades.this);
                            score[i] = new TextView(Grades.this);
                            rb[i].setText(tmptestId);
                            rb[i].setTextSize(22);
                            score[i].setText(tmpScore);
                            score[i].setTextSize(22);

                            rb[i].setTextColor(Color.WHITE);
                            score[i].setTextColor(Color.WHITE);


                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            p.setMarginStart(200);
                            p.setMargins(50,0,0,0);
                            score[i].setLayoutParams(p);

                            layout.addView(rb[i]);
                            layout.addView(score[i]);

                        }
                    }
                    else{
                        Log.e("Message","Grades not available");
                    }
                    return "Success";
                }
                else{
                    // Else you return a message stating that there was an error retrieving grades
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {

            if (message != null){
                Toast.makeText(Grades.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
