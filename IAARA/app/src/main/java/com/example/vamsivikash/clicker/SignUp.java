package com.example.vamsivikash.clicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignUp extends ActionBarActivity {

    GetIP IP = new GetIP();
    EditText firstName, lastName, unity, password, confirmPassword, studentId, securityQuestion, securityAnswer;
    com.beardedhen.androidbootstrap.BootstrapButton register1;
    final Context context = this;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP()+"/signup.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Toast toast = Toast.makeText(context, "I am toast", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 25, 100);

        final Toast toast1 = Toast.makeText(context, "I am toast", Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.TOP, 25, 100);

        final Toast toast2 = Toast.makeText(context, "I am toast", Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.TOP, 25, 100);


        firstName = (EditText)findViewById(R.id.firstName1);
        lastName = (EditText)findViewById(R.id.lastName1);
        unity = (EditText)findViewById(R.id.unity1);
        password = (EditText)findViewById(R.id.password1);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword1);
        studentId = (EditText)findViewById(R.id.studentId1);
        securityQuestion = (EditText)findViewById(R.id.securityQuestion1);
        securityAnswer = (EditText)findViewById(R.id.securityAnswer1);
        register1 = (com.beardedhen.androidbootstrap.BootstrapButton)findViewById(R.id.register1);

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NULL VALIDATION
                if(firstName.getText().length() == 0 || unity.getText().length() == 0 || password.getText().length() == 0 || confirmPassword.getText().length() == 0 || studentId.getText().length() == 0 || securityQuestion.getText().length() == 0 || securityAnswer.getText().length() == 0 ){

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please enter all the fields");
                    alertDialog.show();
                    return;
                }

                //PASSWORDS VALIDATION

                if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                    if(password.getText().length() < 8){
                        toast2.setText("Passwords should be min.8 characters");
                        toast2.show();
                        return;
                    }
                }

                else{
                    toast2.setText("Password's doesn't match!!");
                    toast2.show();
                    return;
                }

                // STUDENT ID VALIDATION

                if(studentId.getText().length() != 9){
                    toast.setText("Student Id must be 9 digits");
                    toast.show();
                    return;
                }
                new insertDetails().execute();
            }
        });
    }

    public class insertDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String fName = firstName.getText().toString();
            String lName = lastName.getText().toString();
            String unityId = unity.getText().toString();
            String pass = password.getText().toString();
            String studId = studentId.getText().toString();
            String secQuestion = securityQuestion.getText().toString();
            String secAnswer = securityAnswer.getText().toString();
            secAnswer = secAnswer.trim();
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("firstName", fName));
                params.add(new BasicNameValuePair("lastName", lName));
                params.add(new BasicNameValuePair("unityId", unityId));
                params.add(new BasicNameValuePair("password", pass));
                params.add(new BasicNameValuePair("studId", studId));
                params.add(new BasicNameValuePair("secQuestion", secQuestion));
                params.add(new BasicNameValuePair("secAnswer", secAnswer));

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent ii = new Intent(context,Login.class);
                    startActivity(ii);
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
}
