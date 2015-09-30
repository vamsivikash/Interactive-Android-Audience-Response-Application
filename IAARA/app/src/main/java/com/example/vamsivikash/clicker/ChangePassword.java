package com.example.vamsivikash.clicker;

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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// This page displays the security question that was retrieved from the Forgot Password Activity
// Only if the correct answer is entered your password gets changed

public class ChangePassword extends ActionBarActivity {

    GetIP IP = new GetIP();
    EditText password, confirmPassword;
    com.beardedhen.androidbootstrap.BootstrapButton change_password;
    JSONParser jsonParser = new JSONParser();

    final String LOGIN_URL = "http://"+ IP.getIP() +"/changePassword.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        change_password = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.change_password);

        // Check whether the two passwords are equal on click of submit else display a message

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    if(password.getText().length() < 8){
                        Toast.makeText(ChangePassword.this, "Passwords should be min.8 characters", Toast.LENGTH_LONG).show();
                    }
                    else{
                        new insertNewPassword().execute(); // Call to insert the new password
                    }
                }
                else{
                    Toast toast = Toast.makeText(ChangePassword.this, "Password's doesn't match!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 25, 100);
                    toast.show();
                }
            }
        });
    }

    // Insert the new password when both the password match
    public class insertNewPassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String unityId = getIntent().getExtras().getString("unity");
            String studentId = getIntent().getExtras().getString("studentId");
            String pass = password.getText().toString();
            Log.e("Student ID", "Vamsi VIkash"+studentId);

            try {
                // Change the password for that particular Student ID

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("password", pass));
                params.add(new BasicNameValuePair("studentId", studentId));

                final JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.e("Password changed", "Vamsi Vikash");
                    Intent ii = new Intent(ChangePassword.this, Login.class);
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
                Toast.makeText(ChangePassword.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
