package quiz_web.Models;

import java.sql.Timestamp;

public class QuizHistory {
    private String username;
    private String quizName;
    private String quizPic;
    private int quizId;
    private double score;
    private Timestamp take_date;
    private int timeNeeded;

    public QuizHistory(String username, String quizName, String quiPic, int quizId, double score, Timestamp take_date, int timeNeeded) {
        this.username = username;
        this.quizName = quizName;
        this.quizPic = quiPic;
        this.quizId = quizId;
        this.score = score;
        this.take_date = take_date;
        this.timeNeeded = timeNeeded;
    }

    public String getUsername() {
        return username;
    }
    public int getTimeNeeded() {return timeNeeded;}

    public String getQuizPic() {
        return quizPic;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getQuizId() {
        return quizId;
    }

    public double getScore() {
        return score;
    }

    public Timestamp getTakeDate() {
        return take_date;
    }
}
