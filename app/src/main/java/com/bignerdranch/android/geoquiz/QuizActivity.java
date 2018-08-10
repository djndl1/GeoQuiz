package com.bignerdranch.android.geoquiz;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mSDKVer;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String BUTTON_STATE = "ButtonState";

    private int rightAnswers = 0;
    private boolean AllAnswered = false;

    //Initialize questions
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
    };

    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        }

        //TrueButton and FalseButton added and Listeners set
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                disableAnswerButtons();

            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                disableAnswerButtons();
            }
        });

        //Question Text
        mQuestionTextView = (TextView) findViewById(R.id.Question_text_view);
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Updating Question", new Exception());
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        //NextButton set listener
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
                }
        });

        //PrevButton set listener
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        mSDKVer = (TextView) findViewById(R.id.API_ver_text);
        mSDKVer.setText("API Level " + Build.VERSION.SDK_INT);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"OnStart called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.put
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    // Question updated in a circle
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        boolean answered = mQuestionBank[mCurrentIndex].isAnswered();

        AllAnswered = isAllAnswered();
        if (AllAnswered) {
            double score = rightAnswers * 100.0 / mQuestionBank.length;
            Toast.makeText(this,score + "%", Toast.LENGTH_SHORT).show();
            for (Question q : mQuestionBank) {
                q.setAnswered(false);
                rightAnswers = 0;
            }
            AllAnswered = false;
        }
        if (!answered)
            enableAnswerButtons();
        else
            disableAnswerButtons();
        mQuestionTextView.setText(question);
    }

    //Check if the user presses the correct button
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int MessageResId;

        mQuestionBank[mCurrentIndex].setAnswered(true);
        if (answerIsTrue == userPressedTrue) {
            MessageResId = R.string.correct_toast;
            rightAnswers ++;
        }
        else {
            MessageResId = R.string.false_toast;
        }

        Toast.makeText(this, MessageResId, Toast.LENGTH_SHORT).show();
    }

    private boolean isAllAnswered() {
        for (Question q : mQuestionBank)
        {
            if (q.isAnswered()==false)
            {
                return false;
            }
        }
        return true;
    }

    private void disableAnswerButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void enableAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }
}
