package com.example.trivia.data;

import com.example.trivia.model.Questions;

import java.util.ArrayList;

public interface AnswerListAsynResponse {

    void processFinished(ArrayList<Questions> questionsArrayList);
}
