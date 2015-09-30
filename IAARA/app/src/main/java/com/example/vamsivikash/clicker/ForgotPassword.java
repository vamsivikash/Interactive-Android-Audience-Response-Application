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

import java.util.ArrayList;
import java.util.List;


public class ForgotPassword extends ActionBarActivity {

    GetIP IP = new GetIP();
    com.beardedhen.androidbootstrap.BootstrapButton retrieve_Question;
    EditText username;
    final Context context = this;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP()+"/recoverPassword.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username = (EditText)findViewById(R.id.username);
        retrieve_Question = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.retrieve_Question);

        final Toast toast = Toast.makeText(context, "I am toast", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 25, 100);

        // Retrieve the security question
        retrieve_Question.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(username.getText().length() == 0){
                    Log.e("Forgot Password","No text error");
                    toast.setText("Please enter the username");
                    toast.show();
                }
                // If some Id is entered check whether that unity Id exists
                else {
                    new recoverPassword().execute();
                }
            }
        });
    }

    // Retrieving the security question
    public class recoverPassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String name = username.getText().toString();

            try {
                // You pass the username to the URL
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", name));

                final JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Retrieve the security Question and the Answer from the response
                    String DBsecurityQuestion = json.getString("securityQuestion");
                    String DBsecurityAnswer = json.getString("securityAnswer");
                    String studentId = json.getString("studId");

                    // Pass the response to the next page to get the security answer
                    Intent ii = new Intent(ForgotPassword.this, SecurityQuestion.class);
                    ii.putExtra("DBsecurityQuestion",DBsecurityQuestion);
                    ii.putExtra("DBsecurityAnswer",DBsecurityAnswer);
                    ii.putExtra("unity",username.getText().toString());
                    ii.putExtra("studentId",studentId);
                    finish();
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }
                else{
                    return json.getString(TAG_MESSAGE);
                }
            }
            catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
            if (message != null){
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
