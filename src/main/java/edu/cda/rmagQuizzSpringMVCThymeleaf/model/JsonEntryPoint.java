package edu.cda.rmagQuizzSpringMVCThymeleaf.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class JsonEntryPoint {

    private int count;
    private ArrayList<Question> quizzes;

    @Override
    public String toString() {
        return "JsonEntryPoint{" +
                "quizzes=" + quizzes +
                '}';
    }
}
