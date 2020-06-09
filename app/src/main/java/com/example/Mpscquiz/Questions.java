package com.example.Mpscquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Questions extends AppCompatActivity {
    String subjectName;
    TextView  question,tv,quesno;
    Button nextQuestion, quitTest;
    RadioGroup radioGroup;
    RadioButton op1, op2, op3, op4;
    public static int correct=0;
    int cnt=1;
    int variable =0;
    Button play_button;
    CardView card;
    public int visibility = 0;
    ProgressBar pb;
    int progress = 1;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toast = new Toast(this);
        play_button = findViewById(R.id.play_button);//Play button to start the game
        card=findViewById(R.id.cardview1);
        question = findViewById(R.id.question);
        pb=findViewById(R.id.progressBar);
        tv=findViewById(R.id.textViewTime);
        final CounterClass timer = new CounterClass(1800000, 1000);
        timer.start();

        //Question no
        quesno=findViewById(R.id.qstnNo) ;
        // Buttons
        nextQuestion =  findViewById(R.id.next_button);
        quitTest =  findViewById(R.id.quit_button);

        // Options
        radioGroup =  findViewById(R.id.radio_group);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 =  findViewById(R.id.op3);
        op4 =  findViewById(R.id.op4);

        pb = findViewById(R.id.progressBar);
        pb.setMax(30);
        pb.setProgress(1);

        Intent intent = getIntent();//receiving the intent send by the Navigation activity
        subjectName = intent.getStringExtra("subject_name");//converting that intent message to string using the getStringExtra() method

        play_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (visibility == 0) {
                    //showing the buttons which were previously invisible
                    pb.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    quesno.setVisibility(View.VISIBLE);
                    question.setVisibility(View.VISIBLE);
                    nextQuestion.setVisibility(View.VISIBLE);
                    quitTest.setVisibility(View.VISIBLE);
                    op1.setVisibility(View.VISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op3.setVisibility(View.VISIBLE);
                    op4.setVisibility(View.VISIBLE);
                    play_button.setVisibility(View.GONE);
                    card.setVisibility(View.GONE);
                    visibility = 1;
                }
            }
        });

        // get Quiz Data including answers
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this, subjectName+".db");
        databaseAccess.open();
        radioGroup.clearCheck();
        final int in = replaceQuestion(databaseAccess);

        quitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.cancel();
                getScore();
                Intent intent = new Intent(Questions.this, activity_result.class);
                intent.putExtra("attempt",cnt);
                intent.putExtra("result", correct+"");
                intent.putExtra("subject_name", subjectName);
                startActivity(intent);
                Questions.this.finish();
            }
        });
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            int flag = 1;
            int index = 0;

            @Override
            public void onClick(View v) {
                cnt++;
                progress = progress+1;

                if ( flag == 1) {
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String cFlag = databaseAccess.getSubjectDetails(6, subjectName, in);
                    int correctOption = cFlag.charAt(0) - 65 + 2;
                    RadioButton uans = findViewById(radioGroup.getCheckedRadioButtonId());

                    String userAnswer = uans.getText().toString();

                    if (databaseAccess.getSubjectDetails(correctOption, subjectName, in).equals(userAnswer)) {
                        Snackbar.make(v, "         Correct ☺", Snackbar.LENGTH_SHORT).show();
                        correct++;
                    }
                    else
                        Snackbar.make(v, "Incorrect\tAnswer :" + userAnswer+ "", Snackbar.LENGTH_SHORT).show();
                    radioGroup.clearCheck();
                    flag = 0;
                    index = replaceQuestion(databaseAccess);
                }

                // for remaining questions
                else {
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        cnt--;
                        progress--;
                        correct--;
                        Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int res = checkAnswer(databaseAccess, index);
                    index = replaceQuestion(databaseAccess);

                }
                if(cnt<10)
                    quesno.setText("0" + cnt + "/30");
                else
                    quesno.setText("" + cnt+ "/30");

                if(cnt > 30) {
                    timer.onFinish();
                    timer.cancel();
                }
                pb.setProgress(progress);
            }
        });
    }
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            tv.setText(hms);
        }

        @Override
        public void onFinish() {
            toast.cancel();
            getScore();
            Intent intent = new Intent(Questions.this, activity_result.class);
            intent.putExtra("attempt",cnt);
            intent.putExtra("result", correct+"");
            intent.putExtra("subject_name", subjectName);
            startActivity(intent);
            Questions.this.finish();
        }
    }
    public int checkAnswer(DatabaseAccess databaseAccess, int index) {
        View v=findViewById(android.R.id.content);
        String cFlag = databaseAccess.getSubjectDetails(6, subjectName, index);
        int correctOption = cFlag.charAt(0) - 65 + 2;

        RadioButton uans =  findViewById(radioGroup.getCheckedRadioButtonId());
        String userAnswer = uans.getText().toString();

        if (databaseAccess.getSubjectDetails(correctOption, subjectName, index).equals(userAnswer)) {
            Snackbar.make(v, "         Correct ☺", Snackbar.LENGTH_SHORT).show();
            correct++;

        }
        else
            Snackbar.make(v, "Incorrect\tAnswer :" + userAnswer+ "", Snackbar.LENGTH_SHORT).show();

        radioGroup.clearCheck();
        return 0;
    }
    public int replaceQuestion(DatabaseAccess databaseAccess) {

        int index = new Random().nextInt(50);
        question.setText(databaseAccess.getSubjectDetails(1, subjectName, index));
        op1.setText(databaseAccess.getSubjectDetails(2,subjectName, index));
        op2.setText(databaseAccess.getSubjectDetails(3,subjectName, index));
        op3.setText(databaseAccess.getSubjectDetails(4,subjectName, index));
        op4.setText(databaseAccess.getSubjectDetails(5,subjectName, index));
        return index;
    }

     public void getScore(){
         if (subjectName.equals("logical") )
             score.logicalm = correct * 2;
         else if (subjectName.equals("english"))
             score.englishm = correct * 2;
         if (subjectName.equals("History") )
             score.historym = correct * 2;
         else if (subjectName.equals("geography") )
             score.geographym = correct * 2;
         if (subjectName.equals("agriculture") )
             score.agriculturem = correct * 2;
         else if (subjectName.equals("politics") )
             score.politicm = correct * 2;
         if (subjectName.equals("human_res") )
             score.human_resm = correct * 2;
         else if (subjectName.equals("science") )
             score.sciencem = correct * 2;
         else if (subjectName.equals("economics") )
             score.economicsm = correct * 2;
         else if (subjectName.equals("current_affair") )
             score.current_affairm = correct * 2;
     }

    @Override
    protected void onPause() {
        super.onPause();
        variable =1;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        variable =1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        variable = 1;
        finish();
    }
}
