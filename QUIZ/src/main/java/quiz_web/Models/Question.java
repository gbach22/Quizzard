package quiz_web.Models;

import java.io.Serializable;

public class Question implements Serializable {
    private int questionId;
    private String questionText;
    private questionTypes questionType;
    private String pictureUrl;
    private boolean sortedRelevant;
    private int quizId;

    private double point;
    public Question(int questionId, String questionText, questionTypes questionType, String pictureUrl, boolean sortedRelevant, int quizId, double point) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.pictureUrl = pictureUrl;
        this.sortedRelevant = sortedRelevant;
        this.quizId = quizId;
        this.point = point;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public questionTypes getQuestionType() {
        return questionType;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public boolean isSortedRelevant() {
        return sortedRelevant;
    }

    public int getQuizId() {
        return quizId;
    }

    public double getPoint() {
        return point;
    }

    public void setQuestionId(int id) {this.questionId = id;}

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setQuestionType(questionTypes questionType) {
        this.questionType = questionType;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setSortedRelevant(boolean sortedRelevant) {
        this.sortedRelevant = sortedRelevant;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "questionId: " + questionId + ", questionText: " + questionText +
                ", questionType: " + questionType.name() +
                ", pictureUrl: " + pictureUrl + ", isSortedRelevant? " + sortedRelevant +
                ", quizId: " + quizId;
    }
}
