package com.example.vamsivikash.clicker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CoursePage extends ActionBarActivity {

    com.beardedhen.androidbootstrap.BootstrapButton quiz, btnHomePage, grades;
    EditText quizId;

    GetIP IP = new GetIP();
    Intent intent = getIntent();
    final Context context = this;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP() +"/testIdCheck.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";
    String selectedCourse;
    static String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        studentId = getIntent().getExtras().getString("studentId");
        quizId = (EditText)findViewById(R.id.quizId);
        grades = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.grades);
        quiz = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.quiz);

        // Home button redirection

        btnHomePage = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.btnHomePage);
        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoursePage.this, HomePage.class);
                intent.putExtra("studentId",studentId);
                startActivity(intent);
            }
        });

        // Testing whether only 5 digits are entered for the test Id and if that is failing it will through up an error

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizId.getText().length() == 5){
                    new testIdCheck().execute();
                }
                else{
                    Toast toast = Toast.makeText(context, "Please enter a valid test Id", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 25, 100);
                    toast.show();
                }
            }
        });

        // Goto Grades page

        grades.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Grades.class);
                Log.e("Stud ID","Course Page"+studentId);
                intent.putExtra("studentId",studentId);
                startActivity(intent);
            }
        });

    }

    // Call the Questions page only if there is a test available or if it is not activated!

    public class testIdCheck extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            selectedCourse = getIntent().getExtras().getString("selectedCourse");
            String testId = quizId.getText().toString();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("testId", testId));
                params.add(new BasicNameValuePair("selectedCourse", selectedCourse));
                params.add(new BasicNameValuePair("studentId", studentId));
                Log.e("Test id", "COurse Page"+testId);
                Log.e("selected course", "COurse Page"+selectedCourse);
                Log.e("Student Id", "COurse Page"+studentId);
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);

                // Only if there is question you will redirect to questions page which retrieves the question
                if (success == 1) {
                    Intent intent = new Intent(context, Questions.class);
                    intent.putExtra("selectedCourse",selectedCourse);
                    intent.putExtra("studentId",studentId);
                    intent.putExtra("testId",testId);
                    startActivity(intent);
                    return json.getString(TAG_MESSAGE);
                }else{
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {

            if (message != null){
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onBackPressed() {

    }
}
