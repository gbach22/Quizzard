package DatabaseTests;

import org.junit.Before;
import org.junit.Test;
import quiz_web.Database.AnnouncementDbManager;
import quiz_web.Database.DbConnection;
import quiz_web.Models.Announcement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// to check line coverage you have to add AnnouncementDbManager.java from Database package, and Announcement.java from Models
// in modify run configuration

public class AnnouncementDbManagerTests {

    private DbConnection dbCon;
    private AnnouncementDbManager db;
    private ArrayList<String> announcements;
    private ArrayList<String> admins;
    private String user;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        announcements = new ArrayList<String>(Arrays.asList("best quiz web site ever", "try this quiz", "tonight is competition", "be more creative", "who is champ?"));
        admins = new ArrayList<>(Arrays.asList("Bacha", "almasxit", "kingslayer"));
        user = "testUser";
        dbCon = new DbConnection();
        dbCon.runSqlFile("testUsers.sql");
        db = new AnnouncementDbManager(dbCon.getConnection(), true);
    }

    @Test
    public void testAddAnnouncement() throws SQLException {
        for(int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
        }

        ArrayList<Announcement> userAnnouncements = db.getAnnouncements(user);

        assertEquals(5, userAnnouncements.size());

        assertEquals(user, userAnnouncements.get(0).getAnnouncemetOwner());

        for(int i = 0; i < announcements.size(); i++) {
            assertEquals(announcements.get(i), userAnnouncements.get(i).getAnnouncement());
        }

        db.deleteAll(user);

        for (int i = 0; i < 5; i++) {
            db.addAnnouncement(user, "Announcement " + i);
        }

        userAnnouncements = db.getAnnouncements(user);
        assertEquals(5, userAnnouncements.size());
    }

    @Test
    public void testDeleteAll() throws SQLException {
        for (int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));;
        }

        db.deleteAll(user);
        ArrayList<Announcement> userAnnouncements = db.getAnnouncements(user);
        assertTrue(userAnnouncements.isEmpty());

        for (int i = 0; i < admins.size(); i++) {
            db.deleteAll(admins.get(i));
            ArrayList<Announcement> adminAnnouncements = db.getAnnouncements(admins.get(i));
            assertTrue(adminAnnouncements.isEmpty());
        }
    }

    @Test
    public void testGetAnnouncements() throws SQLException {

        for (int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
        }

        ArrayList<Announcement> userAnouncements = db.getAnnouncements(user);
        assertEquals(5, userAnouncements.size());

        db.deleteAll(user);
        userAnouncements = db.getAnnouncements(user);
        assertTrue(userAnouncements.isEmpty());

        int counter = 0;
        for (int i = 0; i < admins.size(); i++) {
            ArrayList<Announcement> adminAnnouncement = db.getAnnouncements(admins.get(i));
            counter += adminAnnouncement.size();
        }
        assertEquals(33, counter);

        for(int i = 0; i < admins.size(); i++) {
            db.deleteAll(admins.get(i));
            ArrayList<Announcement> adminAnnouncement = db.getAnnouncements(admins.get(i));
            assertEquals(0, adminAnnouncement.size());
        }
    }

    @Test
    public void testGetAnnouncementsById() throws SQLException {

        for(int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
        }

        int id = 34;
        for(int i = 0; i < announcements.size(); i++) {
            Announcement announcement = db.getAnnouncementById(id);
            assertTrue(announcements.get(i).equals(announcement.getAnnouncement()));
            id++;
        }

        for(int i = 4; i < 34; i++) {
            Announcement announcement = db.getAnnouncementById(i);
            if(i >= 3 && i < 14) {
                assertTrue(admins.get(1).equals(announcement.getAnnouncemetOwner()));
            }else if(i >= 14 && i < 24){
                assertTrue(admins.get(0).equals(announcement.getAnnouncemetOwner()));
            }else{
                assertTrue(admins.get(2).equals(announcement.getAnnouncemetOwner()));
            }
        }
    }

    @Test
    public void testGetAnnouncementsByTimeRange() throws SQLException {
        Instant now = Instant.now();
        Timestamp oneMinuteBefore = Timestamp.from(now.minusSeconds(60));

        for(int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
            if(i != announcements.size() - 1)sleep(1000);
        }

        now = Instant.now();
        Timestamp end = Timestamp.from(now);
        ArrayList<Announcement> timeRangeAnnouncements;

        timeRangeAnnouncements = db.getAnnouncementsByTimeRange(oneMinuteBefore, end, false, "All");
        assertEquals(38, timeRangeAnnouncements.size());

        timeRangeAnnouncements = db.getAnnouncementsByTimeRange(null, null, true, "All");
        assertEquals(38, timeRangeAnnouncements.size());

        timeRangeAnnouncements = db.getAnnouncementsByTimeRange(oneMinuteBefore, end, false, user);
        assertEquals(5, timeRangeAnnouncements.size());
    }

    @Test
    public void testGetUnseenAnnouncements() throws SQLException {

        for(int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
        }

        int counter = 0;
        for(int i = 0; i < admins.size(); i++) {
            ArrayList<Announcement> unseenAnnouncements = db.getUnseenAnnouncements(admins.get(i), Integer.MAX_VALUE);
            counter += unseenAnnouncements.size();
        }

        assertEquals(45, counter);
        db.deleteAll(user);
        db.deleteUnseenAnnouncementsByUsername(user);

        for(int i = 0; i < admins.size(); i++) {
            db.addAnnouncement(admins.get(i), announcements.get(i));
        }

        counter = 0;
        for(int i = 0; i < admins.size(); i++) {
            ArrayList<Announcement> unseenAnnouncements = db.getUnseenAnnouncements(admins.get(i), Integer.MAX_VALUE);
            counter += unseenAnnouncements.size();
        }

        assertEquals(36, counter);
    }

    @Test
    public void testDeleteUnseenAnnouncementsByUsername() throws SQLException {

        for(int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
        }

        int counter = 0;
        for(int i = 0; i < admins.size(); i++) {
            ArrayList<Announcement> unseenAnnouncements = db.getUnseenAnnouncements(admins.get(i), Integer.MAX_VALUE);
            counter += unseenAnnouncements.size();
        }

        assertEquals(counter, 45);

        for(int i = 0; i < admins.size(); i++) {
            db.deleteUnseenAnnouncementsByUsername(admins.get(i));
            ArrayList<Announcement> unseenAnnouncements = db.getUnseenAnnouncements(admins.get(i), Integer.MAX_VALUE);
            assertEquals(0, unseenAnnouncements.size());
        }

    }

    @Test
    public void testSawAnnouncements() throws SQLException {

        for(int i = 0; i < announcements.size(); i++) {
            db.addAnnouncement(user, announcements.get(i));
        }

        for(int i = 0; i < admins.size(); i++) {
            db.sawAnnouncements(admins.get(i), Integer.MAX_VALUE);
        }

        for(int i = 0; i < admins.size(); i++) {
            ArrayList<Announcement> unseenAnnouncements = db.getUnseenAnnouncements(admins.get(i), Integer.MAX_VALUE);
            assertTrue(unseenAnnouncements.isEmpty());
        }
    }

    @Test
    public void testAnnouncementObject() throws SQLException {
        Random random = new Random();
        Instant instant = Instant.now();
        Timestamp now = Timestamp.from(instant);

        ArrayList<Announcement> userAnnouncements = new ArrayList<Announcement>();

        for(int i = 0; i < announcements.size(); i++) {
            Announcement announcement = new Announcement(i, user, announcements.get(i), now);
            userAnnouncements.add(announcement);
        }


        for(int i = 0; i < userAnnouncements.size(); i++) {
            assertTrue(userAnnouncements.get(i).getCreationDate().equals(now));
            assertTrue(userAnnouncements.get(i).getAnnouncement().equals(announcements.get(i)));
            assertTrue(userAnnouncements.get(i).getAnnouncemetOwner().equals(user));
        }
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
