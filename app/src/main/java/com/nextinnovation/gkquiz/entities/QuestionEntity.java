package com.nextinnovation.gkquiz.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by rifqi on Mar 06, 2016.
 */
public class QuestionEntity extends BaseEntity {
    @DatabaseField(columnName = "difficulty", canBeNull = false)
    private int difficulty;

    @DatabaseField(columnName = "question", canBeNull = false)
    private String question;

    @DatabaseField(columnName = "answer", canBeNull = false)
    private int answer;

    @DatabaseField(columnName = "options", canBeNull = false)
    private String options;

    @DatabaseField(columnName = "question_category_id", canBeNull = false)
    private int questionCategoryId;

    @DatabaseField(columnName = "answered", canBeNull = false)
    private boolean isAnswered;

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(int questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }
}
