package quiz_web.Models;

import java.io.Serializable;

public class Answer implements Serializable {
    private int questionId;
    private String answer;
    private int answerNum;
    public Answer(int questionId, String answer, int answerNum) {
        this.questionId = questionId;
        this.answer = answer;
        this.answerNum = answerNum;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return this.answer;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Answer)
        {
            Answer otherAns = (Answer) obj;
            return (this.answer.trim().equalsIgnoreCase(otherAns.getAnswer().trim()));
        }
        return false;
    }
}
