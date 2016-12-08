package com.example.meganpekarek.grinchgame;

/**
 * Created by meganpekarek on 12/5/16.
 */
public class Trivia {

    private String question;

    private String answer1;

    private String answer2;

    private String answer3;

    private String answerRight;

    private int pointChange;

    public Trivia(String question, String answerRight, String answer1, String answer2, String answer3, int pointChange) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answerRight = answerRight;
        this.pointChange = pointChange;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerRight() {
        return answerRight;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public int getPointChange() {
        return pointChange;
    }
}
