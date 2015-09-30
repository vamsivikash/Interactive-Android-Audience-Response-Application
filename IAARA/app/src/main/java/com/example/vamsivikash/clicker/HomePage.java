package com.example.vamsivikash.clicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// This page is used to retrieve the courses for that particular student
public class HomePage extends ActionBarActivity {

    GetIP IP = new GetIP();
    RadioGroup radioGroup;
    final Context context = this;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP()+"/retrieveCourses.php";
    com.beardedhen.androidbootstrap.BootstrapButton courseSelect;
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";
    final String cname = "courseName";
    final String cid = "course";
    String studId;
    int success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        radioGroup = (RadioGroup) findViewById(R.id.course);

        studId = getIntent().getExtras().getString("studentId");
        Log.e("Student Id","Home Page"+studId);
        // Call to the function to query the database to retrieve courses
        new retrieveCourses().execute();
    }

    public class retrieveCourses extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            String sId = studId;
            String courseValues="";
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("studentId", sId));
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    int rowCount = json.getInt("rowCount");

                    // Send the course names to the Post execute method to dynamically create the radio buttons
                    if(rowCount != 0){
                        for(int i=0;i<rowCount;i++){
                            String subjects = json.getString(cname+i);
                            String courseId = json.getString(cid+i);
                            subjects = (subjects).replaceAll("\\[", "").replaceAll("\\]","").replace("\"", "");
                            courseId = (courseId).replaceAll("\\[", "").replaceAll("\\]","").replace("\"", "");
                            courseValues = subjects + ";" + courseId + ";" + courseValues;
                            //courseValues = courseId + ";" + courseValues;
                        }
                        return courseValues;
                    }
                    else{
                        Log.e("Message","in HomePage - No Courses Available");
                    }

                }else{
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {

            Log.e("Message","in HomePage"+message);
            TextView msg = (TextView) findViewById(R.id.message);
            courseSelect = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.courseSelect);

            // Code to dynamically create radio buttons for the courses based on the student's enrollment

            if(message!= null && !message.isEmpty()){
                String[] courseId = message.split(";");
                final RadioButton[] rb = new RadioButton[5];
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                );

                // Separate the course Names from the reply message

                String courseName[] = new String[(courseId.length)/2];
                Log.e("Course Length","Course Id"+courseId.length);
                for(int j=0;j<=(courseId.length)/2;){
                    courseName[j/2]= courseId[j];
                            j=j+2;
                }

                // Separate the Course Id's from the reply message

                final String cId[] = new String[(courseId.length)/2];
                for(int j=0,k=1;j<(courseId.length)/2;j++){
                    cId[j]= courseId[k];
                    k=k+2;
                }

                // Create Radio buttons and add it to the radio group programmatically
                for (int i = 0; i < courseName.length;i++) {
                    rb[i] = new RadioButton(HomePage.this);
                    radioGroup.addView(rb[i]);
                    rb[i].setText(courseName[i]);
                    rb[i].setId(i);
                    rb[i].setTextColor(Color.WHITE);
                    params.setMargins(15, 30+(i*5),30 , 5);
                    rb[i].setLayoutParams(params);
                }

                // Get the selected course on click of the button
                courseSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        int flag = 5;
                        String val= null;
                        if (selectedId == 0 ){
                            flag = 0;
                            val = cId[flag];
                        }

                        else if(selectedId == 1){
                            flag = 1;
                            val = cId[flag];
                        }
                        else if(selectedId == 2){
                            flag = 2;
                            val = cId[flag];
                        }
                        else if(selectedId == 3){
                            flag = 3;
                            val = cId[flag];
                        }

                        else {
                            Toast toast = Toast.makeText(context, "Please select the course", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 25, 100);
                            toast.show();
                        }
                        if(flag != 5){
                            Log.e("value","check"+val);
                            Intent ii = new Intent(HomePage.this,CoursePage.class);
                            ii.putExtra("selectedCourse",val);
                            String ex = "course"+flag;
                            ii.putExtra("studentId",studId);
                            finish();
                            startActivity(ii);
                        }
                    }
                });
            }
            else{
                // Display no courses available message
                msg.setTextSize(20);
                msg.setTextColor(Color.WHITE);
                courseSelect.setEnabled(false);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Prevent the page from going to back page

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}

