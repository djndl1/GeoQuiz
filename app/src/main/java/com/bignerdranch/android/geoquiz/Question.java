package com.bignerdranch.android.geoquiz;

/**
 * Created by djn on 18-8-8.
 */

public class Question {

    private int mTextResId; // Question text
    private boolean mAnswerTrue; //the answer to the question

    public Question(int TextResId, boolean AnswerTrue){
        mTextResId = TextResId;
        mAnswerTrue = AnswerTrue;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {

        return mTextResId;
    }

    public boolean isAnswerTrue() {

        return mAnswerTrue;
    }
}
