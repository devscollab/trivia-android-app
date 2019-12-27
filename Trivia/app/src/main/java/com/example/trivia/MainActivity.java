package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.example.trivia.data.AnswerListAsynResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Questions;
import com.example.trivia.utils.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionsTextview;
    private TextView questionCounterTextview;
    private Button trueButton;
    private Button falseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private  int currentQuestionIndex = 0;
    private List<Questions> questionsList;
    private  TextView currentScore;
    private  int ScoreCounter = 0;
    private TextView highestScore;
    private Button restartButton;
    private Prefs prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextview = findViewById(R.id.counter_text);
        questionsTextview = findViewById(R.id.questions_textview);
        currentScore = findViewById(R.id.currentscore_textview);
        highestScore = findViewById(R.id.Highestscore_textview);
        restartButton = findViewById(R.id.restart_button);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionIndex = 0;
                ScoreCounter = 0;
                currentScore.setText(MessageFormat.format("Current Score: {0}", ScoreCounter));

                questionsList = new QuestionBank().getQuestons(new AnswerListAsynResponse() {
                    @Override
                    public void processFinished(ArrayList<Questions> questionsArrayList) {

                        questionsTextview.setText(questionsArrayList.get(currentQuestionIndex).getAnswer());
                        questionCounterTextview.setText(MessageFormat.format("{0}/{1}", currentQuestionIndex, questionsArrayList.size()));

                        //Log.d("main", "processFinished: "+ questionsArrayList);
                    }
                });

            }
        });


        prefs = new Prefs(MainActivity.this);
        highestScore.setText(MessageFormat.format("Highest Score: {0}", String.valueOf(prefs.getHighestScore())));
        currentQuestionIndex = prefs.getlastquestion();
        ScoreCounter = prefs.getcurrentscore();
        currentScore.setText(MessageFormat.format("Current Score:{0}", String.valueOf(prefs.getcurrentscore())));

        questionsList = new QuestionBank().getQuestons(new AnswerListAsynResponse() {
            @Override
            public void processFinished(ArrayList<Questions> questionsArrayList) {

                questionsTextview.setText(questionsArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextview.setText(MessageFormat.format("{0}/{1}", currentQuestionIndex, questionsArrayList.size()));

                Log.d("main", "processFinished: "+ questionsArrayList);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.prev_button:
                if(currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex - 1)%questionsList.size();
                    updateQuestion();
                }

                break;

            case R.id.next_button:
                gonext();
                break;

            case R.id.true_button:
                checkAnswer(true);
                //updateQuestion();
                break;

            case R.id.false_button:

                checkAnswer(false);
               // updateQuestion();
                break;
        }
    }

    public void updateQuestion(){


        String question = questionsList.get(currentQuestionIndex).getAnswer();
        questionsTextview.setText(question);
        questionCounterTextview.setText(MessageFormat.format("{0}/{1}", currentQuestionIndex, questionsList.size()));


    }

    public void fadeanimation(){
        final CardView cardView = findViewById(R.id.cardView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(alphaAnimation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                gonext();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void shakeanimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_anim);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                gonext();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void checkAnswer(boolean userchooseanswer){

        boolean answerIsTrue = questionsList.get(currentQuestionIndex).isAnswerTrue();
        int toastmessageid = 0;
        if(userchooseanswer == answerIsTrue)
        {
            toastmessageid = R.string.correct_answer;
            fadeanimation();
            addScore();



            //Toast.makeText(MainActivity.this,"That's correct",Toast.LENGTH_SHORT).show();
        }
        else{
            toastmessageid = R.string.wrong_answer;
            shakeanimation();
            deductScore();

            //Toast.makeText(MainActivity.this,"That's incorrect",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(MainActivity.this,toastmessageid,Toast.LENGTH_SHORT).show();
    }

    private  void addScore(){
        ScoreCounter = ScoreCounter + 4;
        currentScore.setText(MessageFormat.format("Current Score: {0}", ScoreCounter));
        Log.d("current score", "addcounter: "+ScoreCounter);

    }

    private void gonext(){
        currentQuestionIndex = (currentQuestionIndex + 1)%questionsList.size();
        updateQuestion();
    }

    private void deductScore(){

        if(ScoreCounter>0){
            ScoreCounter = ScoreCounter - 1;
        }
        else {
            ScoreCounter = 0;
        }
        currentScore.setText(MessageFormat.format("Current Score: {0}", ScoreCounter));
    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(ScoreCounter);
        prefs.savestate(currentQuestionIndex);
        prefs.savecurrentscore(ScoreCounter);
        super.onPause();
    }


}
