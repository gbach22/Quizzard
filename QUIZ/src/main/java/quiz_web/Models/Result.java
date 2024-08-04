package quiz_web.Models;

import java.io.Serializable;
import java.util.*;

import static quiz_web.Models.questionTypes.*;

public class Result implements Serializable {
    private ArrayList<Answer> userAnswers;
    private ArrayList<Answer> actualAnswers;
    private questionTypes type;
    private Question question;
    private boolean correctnessCalculated;
    private boolean isCorrect;
    private double score;
    private List<String> option;

    public Result(Question question, ArrayList<Answer> userAns, ArrayList<Answer> actualAns, List<String> option) {
        correctnessCalculated = false;
        isCorrect = false;
        this.type = question.getQuestionType();
        this.userAnswers = userAns;
        this.actualAnswers = actualAns;
        this.option = option;
        this.question = question;
        if (question.isSortedRelevant()) {
            actualAnswers.sort(new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return a.getAnswerNum() - b.getAnswerNum();
                }
            });
            userAnswers.sort(new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return a.getAnswerNum() - b.getAnswerNum();
                }
            });
        } else {
            actualAnswers.sort(new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return a.getAnswer().compareTo(b.getAnswer());
                }
            });
            userAnswers.sort(new Comparator<Answer>() {
                public int compare(Answer a, Answer b) {
                    return a.getAnswer().compareTo(b.getAnswer());
                }
            });

        }

    }

    public String userAnswerToString() {
        return userAnswers.toString();
        // TODO might need to remove first and last char
    }

    public String actualAnswerToString() {
        return actualAnswers.toString();
    }

    public boolean isCorrect() {
        if (correctnessCalculated) return isCorrect;
        double questionScore = this.question.getPoint();
        this.score = 0.0;
        correctnessCalculated = true;
        if (type == QUESTION_RESPONSE || type == PICTURE_RESPONSE || type == MULTIPLE_CHOICE) {
            Answer userAns = userAnswers.get(0);
            for (Answer actualAnswer : actualAnswers) {
                if (userAns.equals(actualAnswer)) {
                    score += questionScore;
                    break;
                }
            }
        } else if (type == MULTI_ANSWER) {
            if (!question.isSortedRelevant()) {
                int correctCount = getCorrectCount2();
                int numAnswers = actualAnswers.size();
                score += ((double)correctCount / (double)numAnswers) * questionScore;
            } else {
                for (int i = 0; i < actualAnswers.size(); i++) {
                    Answer userAns = userAnswers.get(i);
                    Answer actualAns = actualAnswers.get(i);
                    if (userAns.equals(actualAns))
                        score += questionScore / actualAnswers.size();
                }
            }

        } else if (type == MATCHING) {
            for (int i = 0; i < actualAnswers.size(); i++) {
                Answer userAns = userAnswers.get(i);
                Answer actualAns = actualAnswers.get(i);
                if (userAns.equals(actualAns))
                    score += questionScore / actualAnswers.size();
            }
        } else if (type == FILL_IN_THE_BLANK) {
            int correctCount = getCorrectCount();
            int numAnswers = actualAnswers.get(actualAnswers.size() - 1).getAnswerNum();
            score += ((double)correctCount / (double)numAnswers) * questionScore;

        } else if (type == MULTI_CHOICE_MULTI_ANS) {
            double singleAnsPoint = questionScore / actualAnswers.size();
            double errorFee = questionScore / (option.size() - actualAnswers.size());

            for (Answer a : userAnswers) {
                if (actualAnswers.contains(a)) this.score += singleAnsPoint;
                else this.score -= errorFee;
            }
            this.score = Math.max(0, this.score);
        }
        isCorrect = this.score == questionScore;
        return isCorrect;
    }

    private int getCorrectCount() {
        int correctCount = 0;
        Map<Integer, List<Answer>> answerMap = new HashMap<>();
        for (Answer a : actualAnswers) {
            if (answerMap.containsKey(a.getAnswerNum())) {
                answerMap.get(a.getAnswerNum()).add(a);
            } else {
                List<Answer> newAL = new ArrayList<>();
                newAL.add(a);
                answerMap.put(a.getAnswerNum(), newAL);
            }
        }
        for (Integer i : answerMap.keySet())
            if (answerMap.get(i).contains(userAnswers.get(i-1))) correctCount++;
        return correctCount;
    }


    private int getCorrectCount2() {
        int correctCount = 0;
        for (Answer userAns : userAnswers) {
            if (actualAnswers.contains(userAns)) correctCount++;

        }
        return correctCount;
    }

    public double getScore() {
        return this.score;
    }

    public double getMaxScore() { return this.question.getPoint();
    }
}
