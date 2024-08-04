package quiz_web.Models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement implements Serializable {
    private int announcementId;
    private String username;
    private String announcement;
    private Timestamp creationDate;

    public Announcement(int announcementId, String username, String announcement, Timestamp creationDate) {
        this.announcementId = announcementId;
        this.username = username;
        this.announcement = announcement;
        this.creationDate = creationDate;
    }

    public String getAnnouncemetOwner() {
        return username;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }
}
