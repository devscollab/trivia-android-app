package com.example.trivia.utils;

import android.app.Activity;

import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score){

        int currentScore = score;
        int last = preferences.getInt("highest_score",0);

        if(currentScore > last){
            preferences.edit().putInt("highest_score",currentScore).apply();//we have new high score save it
        }

    }

    public int getHighestScore(){
        return preferences.getInt("highest_score",0);
    }

    public void savestate(int question){
        int currentquestion = question;
        int lastquestion = preferences.getInt("last_question",0);
        preferences.edit().putInt("last_question",currentquestion).apply();

    }

    public int getlastquestion(){
        return preferences.getInt("last_question",0);
    }

    public void savecurrentscore(int current_Score){

        preferences.getInt("current_score",0);
        preferences.edit().putInt("current_score",current_Score).apply();

    }

    public int getcurrentscore(){
        return preferences.getInt("current_score",0);
    }
}
