package com.example.vamsivikash.clicker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Questions extends ActionBarActivity {

    GetIP IP = new GetIP();
    TextView question;
    RadioGroup options,trueFalseOptions;
    RadioButton option1, option2, option3, option4,trueFalseOption1,trueFalseOption2;
    CheckBox chkOption1, chkOption2,chkOption3,chkOption4;
    com.beardedhen.androidbootstrap.BootstrapButton selectAnswer, selectAnswerMCQ, btnNumberAnswer,btnTextAnswer,trueFalsebtn;
    EditText numberAnswer,txtAnswer;
    String selectedCourse;
    String testId,studentId;
    final Context context = this;
    public static int count =0;
    public static int qId = 0;
    public static int countLimit =0;
    public static int points = 0;
    public static int type = 1;
    public static String studId;
    String ANSWER;
    String score;

    int weight = 0;
    JSONParser jsonParser = new JSONParser();
    final String LOGIN_URL = "http://"+ IP.getIP() +"/getQuestion.php";
    final String TAG_SUCCESS = "success";
    final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        studentId = getIntent().getExtras().getString("studentId");
        new getQuestion().execute();

    }

    public class getQuestion extends AsyncTask<String, String, String> {

        // Every time you call the Questions page, this is the code that retrieves the question
        // and decides what to do - whether to display the question/ whether to display error message/
        // To check if it's the last question

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String questionData = null;
            String testType;
            selectedCourse = getIntent().getExtras().getString("selectedCourse");
            testId = getIntent().getExtras().getString("testId");

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("selectedCourse", selectedCourse));
                params.add(new BasicNameValuePair("testId", testId));

                // Get the question based on the test Id and the selected course

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                String questionId;

                // Get the question, no of questions, answer choices, weightage, status
                if (success == 1) {
                    String noOfQuestions = json.getString("noOfQuestions");
                    String questionType = json.getString("questionType");
                    String question = json.getString("question");
                    question = '"' + question + '"';
                    String correctAnswer = json.getString("correctAnswer");
                    String choice1 = json.getString("choice1");
                    String choice2 = json.getString("choice2");
                    String choice3 = json.getString("choice3");
                    String choice4 = json.getString("choice4");
                    String weightage = json.getString("weightage");
                    String status = json.getString("state");
                    questionId = json.getString("questionId");
                    testType = json.getString("testType");
                    questionData = noOfQuestions + ";" + questionType + ";" + question + ";" + correctAnswer + ";" + choice1 + ";" + choice2 + ";" + choice3 + ";" + choice4 + ";" + weightage +  ";" + "1" + ";" + status + ";" + questionId + ";" + testType;

                    return questionData;
                } else {
                    // If there is no question return a message with no. 10 appended at the 10th position
                    return questionData = null + ";" + null + ";" + null + ";" + null + ";" + null + ";" + null + ";" + null + ";" + null + ";" + json.getString(TAG_MESSAGE) +  ";" + "0" +  ";" + "0" + ";" + 5+  ";" + json.getInt("questionId");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
            Log.e("onPostExecute", "Vamsi Vikash " + message);

            if (!(message == null)) {
                String[] data = message.split(";");
                int status;
                final int questionId = Integer.parseInt(data[11]);

                // Check if the 10th parameter is 1 which means there is a question available

                if (Integer.parseInt(data[9]) == 1) {
                    final String testType = data[12];
                    Log.e("RESULT", "HAS SOME QUESTION");
                    countLimit = Integer.parseInt(data[0]);
                    countLimit = Integer.parseInt(data[0]);

                    // TYPE = 1 - Single Choice, 2 - Multiple Choice Questions, 3 - number answer, 4 - text answer 5 - True or False
                    type = Integer.parseInt(data[1]);
                    Log.e("Student Id", "Get Question Method"+studentId);
                    studId = studentId;
                    Log.e("Student Id", "Get Question Method"+studId);

                    // Count is a static variable which is incremented only based on the student's first response.
                    if (count < countLimit) {
                        if (type == 1) {

                            // Radio type questions
                            setContentView(R.layout.activity_questions);

                            options = (RadioGroup) findViewById(R.id.options);
                            option1 = (RadioButton) findViewById(R.id.option1);
                            option2 = (RadioButton) findViewById(R.id.option2);
                            option3 = (RadioButton) findViewById(R.id.option3);
                            option4 = (RadioButton) findViewById(R.id.option4);
                            question = (TextView) findViewById(R.id.question);
                            selectAnswer = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.answerSelect);

                            final int answer1;

                            answer1 = Integer.parseInt(data[3]);
                            question.setText(data[2]);
                            option1.setText(data[4]);
                            option2.setText(data[5]);
                            option3.setText(data[6]);
                            option4.setText(data[7]);
                            weight = Integer.parseInt(data[8]);

                            selectAnswer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int selectedId = options.getCheckedRadioButtonId();
                                    int flag = 0;

                                    if (selectedId == option1.getId())
                                        flag = 1;

                                    else if (selectedId == option2.getId())
                                        flag = 2;

                                    else if (selectedId == option3.getId())
                                        flag = 3;

                                    else if (selectedId == option4.getId())
                                        flag = 4;

                                    else {
                                        Toast toast = Toast.makeText(context, "Please select the correct option", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.TOP, 25, 100);
                                        toast.show();
                                        flag = 0;
                                    }

                                    // Only if the question type is graded points have to be added
                                    if(testType.equals("graded")){
                                        if (flag != 0 && answer1 == flag) {
                                            if (qId != questionId) {
                                                points += weight;
                                                count++;
                                            }
                                            qId = questionId;

                                            Log.e("Graded - correct","Type 1");
                                            Log.e("Points","Type 1"+points);
                                            Log.e("count","Type 1"+count);
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);

                                        } else if (flag != 0) {
                                            // The below condition is used to check when the same question repeats multiple time,
                                            // the student's score shouldn't add up

                                            if (qId != questionId ){
                                                points += 0;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - Not correct","Type 1");
                                            Log.e("Points","Type 1"+points);
                                            Log.e("count","Type 1"+count);
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                        }
                                    }

                                    else if(testType.equals("anonymous")){
                                        if (flag != 0 && qId != questionId) {
                                            Log.e("Graded - Anonymous","Type 1");
                                            Log.e("Points","Type 1");
                                            Log.e("count","Type 1");
                                            Log.e("Flag", "Type 1"+flag);
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                            ANSWER = Integer.toString(flag);
                                            qId = questionId;
                                            count++;
                                            new saveScoresAnonymous().execute();
                                        }
                                        // You should not add the count if the answer is not selected or
                                        // if the question is repeating
                                        else{
                                            Log.e("Graded - Anonymous","Type 1");
                                            Log.e("Points","Type 1");
                                            Log.e("count","Type 1");
                                            Log.e("Flag", "Type 1"+flag);
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                            ANSWER = Integer.toString(flag);
                                            qId = questionId;
                                            new saveScoresAnonymous().execute();
                                        }
                                    }

                                    Intent intent = new Intent(Questions.this, Questions.class);
                                    intent.putExtra("selectedCourse", selectedCourse);
                                    intent.putExtra("testId", testId);
                                    intent.putExtra("studentId", studentId);
                                    startActivity(intent);
                                }
                            });
                        }

                        // MULTIPLE CHOICE QUESTIONS

                        else if (type == 2) {
                            setContentView(R.layout.activity_questions_mcq);
                            chkOption1 = (CheckBox) findViewById(R.id.chkOption1);
                            chkOption2 = (CheckBox) findViewById(R.id.chkOption2);
                            chkOption3 = (CheckBox) findViewById(R.id.chkOption3);
                            chkOption4 = (CheckBox) findViewById(R.id.chkOption4);
                            question = (TextView) findViewById(R.id.questionMCQ);
                            selectAnswerMCQ = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.answerSelectMCQ);

                            final String answer2;

                            answer2 = data[3];
                            //String[] answerChoices = answer2.split(",");
                            question.setText(data[2]);
                            chkOption1.setText(data[4]);
                            chkOption2.setText(data[5]);
                            chkOption3.setText(data[6]);
                            chkOption4.setText(data[7]);
                            weight = Integer.parseInt(data[8]);

                            selectAnswerMCQ.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // Append the answer solution so that you can compare the solutions at the end
                                    String selectedAnswer = "";
                                    int[] ans = new int[4];
                                    if (chkOption1.isChecked()){
                                        ans[0]=1;
                                        selectedAnswer += "1,";
                                    }

                                    if (chkOption2.isChecked())
                                    {
                                        ans[1]=2;
                                        selectedAnswer += "2,";
                                    }
                                    if (chkOption3.isChecked()){
                                        ans[2]=3;
                                        selectedAnswer += "3,";
                                    }
                                    if (chkOption4.isChecked()){
                                        ans[3]=4;
                                        selectedAnswer += "4,";
                                    }

                                    Log.e("Selected Answer","Vamsi Vikash"+selectedAnswer);
                                    Log.e("Correct Answer","Vamsi Vikash"+answer2);
                                    //String[] answers = selectedAnswer.split(",");

                                    // Credits given only if the options match
                                    if(testType.equals("graded") && selectedAnswer != ""){

                                        if (selectedAnswer.equals(answer2)) {
                                            if (qId != questionId) {
                                                points += weight;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - correct","Type 2");
                                            Log.e("Points","Type 2"+points);
                                            Log.e("count","Type 2"+count);
                                            Log.e("QId","Type 2"+qId);
                                            Log.e("Question Id", "Type 2"+questionId);
                                            Intent intent = new Intent(context, Questions.class);
                                            intent.putExtra("selectedCourse", selectedCourse);
                                            intent.putExtra("testId", testId);
                                            intent.putExtra("studentId", studentId);
                                            startActivity(intent);
                                        } else {
                                            if (qId != questionId) {
                                                points += 0;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - Not correct","Type 2");
                                            Log.e("Points","Type 2"+points);
                                            Log.e("count","Type 2"+count);
                                            Log.e("QId","Type 2"+qId);
                                            Log.e("Question Id", "Type 2"+questionId);
                                            Intent intent = new Intent(context, Questions.class);
                                            intent.putExtra("selectedCourse", selectedCourse);
                                            intent.putExtra("testId", testId);
                                            intent.putExtra("studentId", studentId);
                                            startActivity(intent);
                                        }
                                    }

                                    else if(testType.equals("anonymous")){
                                        if (selectedAnswer != "" && qId != questionId) {
                                            Log.e("Anonymous - correct","Type 2");
                                            Log.e("count","Type 2"+count);
                                            Log.e("QId","Type 2"+qId);
                                            Log.e("Question Id", "Type 2"+questionId);
                                            ANSWER = selectedAnswer;
                                            qId = questionId;
                                            count++;
                                            new saveScoresAnonymous().execute();
                                        }
                                        else{
                                            Log.e("Graded - Anonymous","Type 1");
                                            Log.e("Points","Type 1");
                                            Log.e("count","Type 1");
                                            Log.e("Flag", "Type 1");
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                            ANSWER = selectedAnswer;
                                            qId = questionId;
                                            new saveScoresAnonymous().execute();
                                        }
                                    }
                                    else{
                                        Toast.makeText(context, "Please select some answers!!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        // Number based question
                        else if (type == 3) {
                            setContentView(R.layout.activity_questions_number);

                            question = (TextView) findViewById(R.id.question);
                            numberAnswer = (EditText) findViewById(R.id.numberAnswer);
                            btnNumberAnswer = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnNumberAnswer);

                            final String answer3;
                            answer3 = data[3];
                            weight = Integer.parseInt(data[8]);

                            question.setText(data[2]);

                            btnNumberAnswer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                     if(testType.equals("graded") && numberAnswer.getText().toString().length() != 0){
                                        if (numberAnswer.getText().toString().equals(answer3)) {
                                            if (qId != questionId) {
                                                points += weight;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - correct","Type 3");
                                            Log.e("Points","Type 3"+points);
                                            Log.e("count","Type 3"+count);
                                            Log.e("QId","Type 3"+qId);
                                            Log.e("Question Id", "Type 3"+questionId);
                                        }

                                        else {
                                            if (qId != questionId) {
                                                points += 0;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - Not correct","Type 3");
                                            Log.e("Points","Type 3"+points);
                                            Log.e("count","Type 3"+count);
                                            Log.e("QId","Type 3"+qId);
                                            Log.e("Question Id", "Type 3"+questionId);
                                        }

                                        Intent intent = new Intent(context, Questions.class);
                                        intent.putExtra("selectedCourse", selectedCourse);
                                        intent.putExtra("testId", testId);
                                        intent.putExtra("studentId", studentId);
                                        startActivity(intent);
                                    }

                                    else if(testType.equals("anonymous")){
                                        if (numberAnswer.getText().toString() != "" && qId != questionId) {
                                            Log.e("Anonymous ","Type 3");
                                            Log.e("count","Type 3"+count);
                                            Log.e("QId","Type 3"+qId);
                                            Log.e("Question Id", "Type 3"+questionId);
                                            ANSWER = numberAnswer.getText().toString();
                                            qId = questionId;
                                            count++;
                                            new saveScoresAnonymous().execute();
                                        }
                                        else{
                                            Log.e("Graded - Anonymous","Type 1");
                                            Log.e("Points","Type 1");
                                            Log.e("count","Type 1");
                                            Log.e("Flag", "Type 1");
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                            ANSWER = numberAnswer.getText().toString();
                                            qId = questionId;
                                            new saveScoresAnonymous().execute();
                                        }
                                    }

                                    else{
                                        Toast.makeText(context, "Please enter some value", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        // Text based question
                        else if (type == 4) {
                            setContentView(R.layout.activity_questions_text);

                            question = (TextView) findViewById(R.id.question);
                            txtAnswer = (EditText) findViewById(R.id.txtAnswer);
                            btnTextAnswer = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnTextAnswer);

                            final String answer4;
                            answer4 = data[3];
                            weight = Integer.parseInt(data[8]);

                            question.setText(data[2]);

                            btnTextAnswer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (txtAnswer.getText().toString().length() == 0) {
                                        Toast.makeText(context, "Please enter some value", Toast.LENGTH_LONG).show();
                                    }

                                    if(testType.equals("graded")){
                                        if (txtAnswer.getText().toString().equalsIgnoreCase(answer4)) {
                                            if (qId != questionId) {
                                                points += weight;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - correct","Type 4");
                                            Log.e("Points","Type 4"+points);
                                            Log.e("count","Type 4"+count);
                                            Log.e("QId","Type 4"+qId);
                                            Log.e("Question Id", "Type 4"+questionId);

                                        } else {
                                            if (qId != questionId) {
                                                points += 0;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - Not correct","Type 4");
                                            Log.e("Points","Type 4"+points);
                                            Log.e("count","Type 4"+count);
                                            Log.e("QId","Type 4"+qId);
                                            Log.e("Question Id", "Type 4"+questionId);
                                        }

                                        Intent intent = new Intent(context, Questions.class);
                                        intent.putExtra("selectedCourse", selectedCourse);
                                        intent.putExtra("testId", testId);
                                        intent.putExtra("studentId", studentId);
                                        startActivity(intent);

                                    }

                                    else if(testType.equals("anonymous")){
                                        if (txtAnswer.getText().toString() != "" && qId != questionId) {
                                            Log.e("Anonymous","Type 4");
                                            Log.e("count","Type 4"+count);
                                            Log.e("QId","Type 4"+qId);
                                            Log.e("Question Id", "Type 4"+questionId);
                                            ANSWER = txtAnswer.getText().toString();
                                            qId = questionId;
                                            count++;
                                            new saveScoresAnonymous().execute();
                                        }
                                        else{
                                            Log.e("Graded - Anonymous","Type 1");
                                            Log.e("Points","Type 1");
                                            Log.e("count","Type 1");
                                            Log.e("Flag", "Type 1");
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                            ANSWER = txtAnswer.getText().toString();
                                            qId = questionId;
                                            new saveScoresAnonymous().execute();
                                        }
                                    }
                                }
                            });
                        }

                        // True or false based question

                        else if (type == 5) {

                            setContentView(R.layout.activity_questions_truefalse);

                            question = (TextView) findViewById(R.id.question);
                            trueFalseOption1 = (RadioButton) findViewById(R.id.trueFalseOption1);
                            trueFalseOption2 = (RadioButton) findViewById(R.id.trueFalseOption2);
                            trueFalsebtn = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.trueFalsebtn);
                            trueFalseOptions = (RadioGroup) findViewById(R.id.trueFalseOptions);

                            final String answer5;
                            answer5 = data[3];

                            question.setText(data[2]);
                            trueFalseOption1.setText(data[4]);
                            trueFalseOption2.setText(data[5]);
                            weight = Integer.parseInt(data[8]);

                            trueFalsebtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    int selectedId = trueFalseOptions.getCheckedRadioButtonId();
                                    String flag1 = "null";

                                    if (selectedId == trueFalseOption1.getId())
                                        flag1 = "True";

                                    else if (selectedId == trueFalseOption2.getId())
                                        flag1 = "False";

                                    else {
                                        Toast toast = Toast.makeText(context, "Please choose your answer", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.TOP, 25, 100);
                                        toast.show();
                                        flag1 = "null";
                                    }
                                    Log.e("Vamsi Vikash ", "1");

                                    if(testType.equals("graded")){
                                        if (answer5.equals(flag1)) {
                                            if (qId != questionId) {
                                                points += weight;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - correct","Type 5");
                                            Log.e("Points","Type 5"+points);
                                            Log.e("count","Type 5"+count);
                                            Log.e("QId","Type 5"+qId);
                                            Log.e("Question Id", "Type 5"+questionId);
                                        }
                                        else  {

                                            if (qId != questionId) {
                                                points += 0;
                                                count++;
                                            }
                                            qId = questionId;
                                            Log.e("Graded - Not correct","Type 5");
                                            Log.e("Points","Type 5"+points);
                                            Log.e("count","Type 5"+count);
                                            Log.e("QId","Type 5"+qId);
                                            Log.e("Question Id", "Type 5"+questionId);
                                        }

                                        Intent intent = new Intent(Questions.this, Questions.class);
                                        intent.putExtra("selectedCourse", selectedCourse);
                                        intent.putExtra("testId", testId);
                                        intent.putExtra("studentId", studentId);
                                        startActivity(intent);
                                    }

                                    else if(testType.equals("anonymous")){
                                        if ( qId != questionId) {
                                            ANSWER = flag1;
                                            qId = questionId;
                                            count++;
                                            new saveScoresAnonymous().execute();
                                            Log.e("Anonymous","Type 5");
                                            Log.e("count","Type 5"+count);
                                            Log.e("QId","Type 5"+qId);
                                            Log.e("Question Id", "Type 5"+questionId);
                                        }
                                        else{
                                            Log.e("Graded - Anonymous","Type 1");
                                            Log.e("Points","Type 1");
                                            Log.e("count","Type 1");
                                            Log.e("Flag", "Type 1"+flag1);
                                            Log.e("QId","Type 1"+qId);
                                            Log.e("Question Id", "Type 1"+questionId);
                                            ANSWER = flag1;
                                            qId = questionId;
                                            new saveScoresAnonymous().execute();
                                        }
                                    }
                                }
                            });
                        }

                        else {
                            setContentView(R.layout.activity_questions);
                        }

                    } else {
                        Log.e("Questions", "HAS NO QUESTION");
                        // There are no available questions. So take the user to a refresh page!

                            Log.e("Test Ended", "Last Question Reached");
                            score = Integer.toString(points);
                            // Store the score in the database
                            new saveScoresGraded().execute();
                    }
                } else {

                    Log.e("No tests available!", "Vamsi Vikash");
                    Toast.makeText(Questions.this, message, Toast.LENGTH_LONG).show();
                }
            }
            // If there are no active questions call the NoQuestions method which again calls the Questions method
            // if there is any active question
            else{
                    Intent intent = new Intent(Questions.this, NoQuestions.class);
                    intent.putExtra("selectedCourse", selectedCourse);
                    intent.putExtra("testId", testId);
                    intent.putExtra("studentId", studentId);
                    startActivity(intent);
            }
        }
    }

    // Saving the scores to the database

    public class saveScoresGraded extends AsyncTask<String, String, String> {
        final String SaveScores = "http://"+ IP.getIP()+"/results.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            //final String studentId = getIntent().getExtras().getString("studentId");
            String score = Integer.toString(points);
            String tId = testId;
            Log.e("Student ID!!","Vamsi Vikash"+studId);
            Log.e("Test ID!!","Vamsi Vikash"+tId);
            Log.e("Score!!","Vamsi Vikash"+score);


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("studentId", studentId));
                params.add(new BasicNameValuePair("score", score));
                params.add(new BasicNameValuePair("testId", tId));

                JSONObject json = jsonParser.makeHttpRequest(SaveScores, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.e("Marks Saved!!","Vamsi Vikash");
                    Intent intent = new Intent(Questions.this, Questions.class);
                    intent.putExtra("selectedCourse", selectedCourse);
                    intent.putExtra("testId", testId);
                    intent.putExtra("studentId", studentId);
                    startActivity(intent);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.e("Marks not Saved!!","Vamsi Vikash");
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {

            if (message != null){
                count = 0;
                qId = 0;
                countLimit = 0;
                points = 0;
                type = 0;

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Questions.this, Results.class);
                intent.putExtra("studentId",studentId);
                intent.putExtra("Points", score);
                intent.putExtra("testId",testId);
                startActivity(intent);
                finish();
            }
        }
    }


    // TO STORE THE ANONYMOUS RESULTS

    public class saveScoresAnonymous extends AsyncTask<String, String, String> {
        final String SaveScores = "http://"+ IP.getIP()+"/anonymousResults.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;

            String tId = testId;
            String answer = ANSWER;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("testId", tId));
                params.add(new BasicNameValuePair("questionId", Integer.toString(qId)));
                params.add(new BasicNameValuePair("answer", answer));

                JSONObject json = jsonParser.makeHttpRequest(SaveScores, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.e("Answers Saved!!","Vamsi Vikash");
                    Intent intent = new Intent(Questions.this, Questions.class);
                    intent.putExtra("selectedCourse", selectedCourse);
                    intent.putExtra("testId", testId);
                    intent.putExtra("studentId", studentId);
                    startActivity(intent);
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.e("Marks not Saved!!","Vamsi Vikash");
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}
