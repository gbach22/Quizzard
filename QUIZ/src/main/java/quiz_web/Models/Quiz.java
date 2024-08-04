package quiz_web.Models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Quiz  implements Serializable {
    private int quizId;
    private String quizName;
    private String quizDescription;
    private String creatorUsername;
    private String pictureUrl;
    private boolean multiPage;
    private boolean random;
    private Timestamp createdTime;
    private boolean immediateCorrection;
    private boolean practiceMode;
    private Categories category;
    private int views;
    private int taken;

    public Quiz(int quizId, String quizName, String quizDescription, String creatorUsername,
                String pictureUrl, boolean multiPage, boolean random, boolean immediateCorrection,
                boolean practiceMode, Timestamp createdTime, Categories category, int views, int taken) {

        this.quizId = quizId;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.creatorUsername = creatorUsername;
        this.pictureUrl = pictureUrl;
        this.multiPage = multiPage;
        this.random = random;
        this.immediateCorrection = immediateCorrection;
        this.practiceMode = practiceMode;
        this.createdTime = createdTime;
        this.category = category;
        this.views = views;
        this.taken = taken;
    }

    public int getQuizId() {
        return quizId;
    }
    public String getQuizName() {
        return quizName;
    }
    public String getQuizDescription() {
        return quizDescription;
    }
    public String getCreatorUsername() {
        return creatorUsername;
    }
    public boolean isMultiPage() {
        return multiPage;
    }
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public String getPictureUrl() {
        return pictureUrl.trim();
    }
    public boolean isRandom() {
        return random;
    }
    public boolean isImmediateCorrection() {
        return immediateCorrection;
    }
    public boolean isPracticeMode() {
        return practiceMode;
    }

    public Categories getCategory() {
        return category;
    }

    public int getViews() {
        return views;
    }
    public int getTaken() {
        return taken;
    }

    @Override
    public String toString() {
        return "id: " + quizId + ", name: " + quizName + ", description: " + quizDescription +
                ", creator username: " + creatorUsername + ", is Multi Page? " + multiPage +
                ", creation time: " + createdTime.toString();
    }
}
