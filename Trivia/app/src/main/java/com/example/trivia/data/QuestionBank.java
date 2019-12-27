package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Questions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.trivia.controller.AppController.TAG;

public class QuestionBank {

    ArrayList<Questions> questionsArrayList = new ArrayList<>();
    private  String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Questions> getQuestons(final AnswerListAsynResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("Json stuff", "onResponse: " +response);
                        for(int i = 0; i<response.length();i++){

                            try {
                                Questions questions = new Questions();
                                questions.setAnswer(response.getJSONArray(i).get(0).toString());
                                questions.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                //add question array to list

                                questionsArrayList.add(questions);
                                // Log.d("hello", "onResponse: " +questions );

                                //Log.d("Json", "onResponse: " + response.getJSONArray(i).get(0));
                                //Log.d("Json2", "onResponse: " + response.getJSONArray(i).getBoolean(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(callBack != null)callBack.processFinished(questionsArrayList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return  questionsArrayList;
    }
}
