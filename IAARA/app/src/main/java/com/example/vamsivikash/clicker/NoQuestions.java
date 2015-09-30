package com.example.vamsivikash.clicker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// This page handles if there are no active questions - it redirects you to the Questions page again
// which checks whether there are any active questions or not
public class NoQuestions extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_questions);
        com.beardedhen.androidbootstrap.BootstrapCircleThumbnail reload;

        final String selectedCourse = getIntent().getExtras().getString("selectedCourse");
        final String testId = getIntent().getExtras().getString("testId");
        final String studentId = getIntent().getExtras().getString("studentId");

        reload = (com.beardedhen.androidbootstrap.BootstrapCircleThumbnail) findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoQuestions.this, Questions.class);
                intent.putExtra("selectedCourse",selectedCourse);
                intent.putExtra("testId",testId);
                intent.putExtra("studentId",studentId);
                startActivity(intent);
            }
        });

    }

}
