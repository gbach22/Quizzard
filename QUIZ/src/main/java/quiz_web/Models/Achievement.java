package quiz_web.Models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Achievement implements Serializable {
    private int achievementId;
    private String username;
    private int achievementType;
    private Timestamp received_date;

    public Achievement(int achievementId, String username, int achievementType, Timestamp received_date) {
        this.achievementId = achievementId;
        this.username = username;
        this.achievementType = achievementType;
        this.received_date = received_date;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public int getAchievementType() {
        return achievementType;
    }

    public Timestamp getReceived_date() {
        return received_date;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Achievement)
        {
            Achievement other = (Achievement) obj;
            return this.username.equals(other.getUsername()) && this.achievementType == other.getAchievementType();
        }
        return false;
    }
}
