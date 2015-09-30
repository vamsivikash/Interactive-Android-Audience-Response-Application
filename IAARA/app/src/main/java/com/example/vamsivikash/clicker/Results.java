package com.example.vamsivikash.clicker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Results extends ActionBarActivity {

    GetIP IP = new GetIP();
    final Context context = this;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP()+"/results.php";

    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        com.beardedhen.androidbootstrap.BootstrapButton btnHome;
        EditText txtScore;

        final String studentId = getIntent().getExtras().getString("studentId");
        String score = getIntent().getExtras().getString("Points");
        String testId = getIntent().getExtras().getString("testId");

        btnHome = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Results.this, HomePage.class);
                intent.putExtra("studentId",studentId);
                startActivity(intent);
            }
        });

        txtScore = (EditText)findViewById(R.id.txtScore);
        txtScore.setText(score);
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
