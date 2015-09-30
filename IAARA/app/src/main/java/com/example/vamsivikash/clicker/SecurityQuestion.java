package com.example.vamsivikash.clicker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SecurityQuestion extends ActionBarActivity {

    TextView full_question;
    EditText secAnswer;
    com.beardedhen.androidbootstrap.BootstrapButton reset_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);
        full_question = (TextView)findViewById(R.id.full_question);
        secAnswer = (EditText)findViewById(R.id.securityAnswer);

        // Retrieve the security question and answer from the database
        String Question = getIntent().getExtras().getString("DBsecurityQuestion");
        final String Answer = getIntent().getExtras().getString("DBsecurityAnswer");
        final String unity = getIntent().getExtras().getString("unity");
        final String studentId = getIntent().getExtras().getString("studentId");
        full_question.setText(Question);

        // Only if the entered answer is correct, it goes to the next page.
        reset_password = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.reset_password);
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(secAnswer.getText().length() == 0){
                    Toast toast = Toast.makeText(SecurityQuestion.this, "Please enter the security answer", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 25, 100);
                    toast.show();
                }
                else if(secAnswer.getText().toString().equals(Answer)){
                    Intent ii = new Intent(SecurityQuestion.this, ChangePassword.class);
                    ii.putExtra("unityId",unity);
                    ii.putExtra("studentId",studentId);
                    startActivity(ii);
                }
                else{
                    Toast toast = Toast.makeText(SecurityQuestion.this, "Answer doesn't match!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 25, 100);
                    toast.show();
                }

            }
        });
    }

}
