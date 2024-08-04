package quiz_web.Models;

import java.io.Serializable;
import java.util.HashMap;

public class QuizResults  implements Serializable {
    private int quizId;
    private String quizName;
    private int correctAnswersCount;
    HashMap<Integer, Result> resultsMap;

    public QuizResults(int quizId, String quizName) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.resultsMap = new HashMap<>();

    }

    public void addResult(Integer q, Result r) {
        resultsMap.put(q, r);
    }

    public Result getResult(Integer q) {
        return resultsMap.get(q);
    }

}
