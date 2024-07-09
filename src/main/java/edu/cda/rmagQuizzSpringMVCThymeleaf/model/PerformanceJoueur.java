package edu.cda.rmagQuizzSpringMVCThymeleaf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceJoueur {
    private String pseudo;
    private int scorefinal;

    @Override
    public String toString() {
        return  pseudo + ", " + scorefinal + " points" ;
    }
}
