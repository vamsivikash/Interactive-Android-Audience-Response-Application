package com.example.vamsivikash.clicker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Login extends ActionBarActivity {

    GetIP IP = new GetIP();
    RelativeLayout background;
    EditText username, password;
    TextView forgot_password,register,connect;
    com.beardedhen.androidbootstrap.BootstrapButton login;
    final Context context = this;
    JSONParser jsonParser = new JSONParser();
    // URL for login page!
    final String LOGIN_URL = "http://"+ IP.getIP()+"/login_new.php";

    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("URL", " NEW URL " +LOGIN_URL);

        background = (RelativeLayout)findViewById(R.id.background);
        username = (EditText)findViewById(R.id.txtOne);
        password = (EditText)findViewById(R.id.txttwo);
        login = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.submit);

        // Handle the login button - Check the Username and Password from the database

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If the username/ password is not entered he will get a message
                if(username.getText().length() == 0 || password.getText().length() == 0){
                    Toast toast = Toast.makeText(context, "Please enter the Username/Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 25, 100);
                    toast.show();
                }
                // Else check the uername and Password
                else{
                    new loginCheck().execute();
                }
            }
        });

        // Button to handle forgot password - Takes you to the ForgotPassword Activity
        forgot_password = (TextView)findViewById(R.id.forgot);

        forgot_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Button for registering as a new user - Takes you to the Signup Activity
        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignUp.class);
                startActivity(intent);
            }
        });

        connect = (TextView)findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Connect.class);
                startActivity(intent);
            }
        });
    }

    // Create a new thread for checking whether the student has a registered or not

    public class loginCheck extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            Log.e("IP ", "ADDRESS" +IP.getIP());
            int success;
            String unityId = username.getText().toString();
            String pass = password.getText().toString();

            try {
                // Pass the Unity Id and the password to the login.php which checks with the database whether the student is
                // registered with the system or not.

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("unityId", unityId));
                params.add(new BasicNameValuePair("password", pass));

                // Passing the username and password as a POST which is secured.
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // If the executed Query is correct you get a reply of 1 else 0
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // If it's a success we are going to the Home Page where we pass the Student Id to the Homepage activity
                    // This one returns the success message
                    Intent ii = new Intent(Login.this,HomePage.class);
                    String studId = json.getString("studentId");
                    ii.putExtra("studentId", studId);
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }else{
                    // Else you return a message stating that your credentials are wrong.
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
}
