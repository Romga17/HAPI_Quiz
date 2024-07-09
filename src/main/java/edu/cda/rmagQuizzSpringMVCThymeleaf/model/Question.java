package edu.cda.rmagQuizzSpringMVCThymeleaf.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Question {

    private String _id;

    private String question;

    private String answer;

    private ArrayList<String> badAnswers;

    private String category;

    private String difficulty;

    @Override
    public String toString() {
        return "Question{" +
                "_id='" + _id + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", badAnswers=" + badAnswers +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
