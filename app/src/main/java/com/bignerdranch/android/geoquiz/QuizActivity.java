package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mSDKVer;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private static final int REQUEST_CODE_CHEAT = 0;

    private int mCurrentIndex = 0;
    private int rightAnswers = 0;
    private boolean AllAnswered = false;
    private boolean mIsCheater;

    //Initialize questions
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_quiz);

        // retrieving saved mCurrentIndex
        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        }

        //TrueButton and FalseButton added and Listeners set
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener((View v) -> {
            checkAnswer(true);
            disableAnswerButtons();
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener((View v) -> {
            checkAnswer(false);
            disableAnswerButtons();
        });

        //Question Text
        mQuestionTextView = (TextView) findViewById(R.id.Question_text_view);
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
        mQuestionTextView.setOnClickListener((View v) -> {
            Log.d(TAG,"Updating Question", new Exception());
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
            mIsCheater = false; //Cheating state removed when proceeding to another question
        });

        //NextButton set listener
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener((View v) -> {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
            mIsCheater = false; //Cheating state removed when proceeding to another question
        });

        //PrevButton set listener
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener((View v) -> {
            mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
            updateQuestion();
            mIsCheater = false; //Cheating state removed when proceeding to another question
        });

        //CheatButton set listener to start CheatActivity
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener((View v) -> {
            boolean answer = mQuestionBank[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(QuizActivity.this, answer);
            startActivityForResult(intent, REQUEST_CODE_CHEAT); // seeking to know if the player has cheated
        });

        //Displaying the SDK version
        mSDKVer = (TextView) findViewById(R.id.API_ver_text);
        mSDKVer.setText("API Level " + Build.VERSION.SDK_INT);
    }

    // Handling the result returned by CheatActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
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

    // save mCurrentIndex
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
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

        if (mIsCheater) {
            MessageResId = R.string.judgment_toast;
        } else {
                if (answerIsTrue == userPressedTrue) {
                    MessageResId = R.string.correct_toast;
                    rightAnswers++;
                } else {
                    MessageResId = R.string.false_toast;
                }
        }
        Toast answerToast = Toast.makeText(this, MessageResId, Toast.LENGTH_SHORT);
        answerToast.setGravity(Gravity.TOP, 0, 0);
        answerToast.show();
    }

    // Checking if all questions have been answered
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
