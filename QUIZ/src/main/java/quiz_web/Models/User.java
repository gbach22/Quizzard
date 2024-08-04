package quiz_web.Models;

import quiz_web.Database.UserDbManager;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class User  implements Serializable {
    private final String DEFAULT_PFP = "https://i.pinimg.com/564x/68/69/7e/68697ed39e4b7df530c3a61c1853b81a.jpg";
    private String firstName;
    private String lastName;
    private String userName;
    private String hashedPassword;
    private String pictureUrl;
    private String bio;
    private boolean isAdmin;

    public User(String firstName, String lastName, String userName, String hashedPassword, String bio, String pictureURL, boolean isAdmin){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        if (pictureURL.isEmpty()) this.pictureUrl = DEFAULT_PFP;
        else this.pictureUrl = pictureURL;
        this.bio = bio;
        this.isAdmin = isAdmin;
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean setFirstName(String newFirstName) throws SQLException {
        this.firstName = newFirstName;
        return false;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean setLastName(String newLastName) throws SQLException {
        this.lastName = newLastName;
        return false;
    }

    public String getUserName() {
        return userName;
    }
    public String getBio() {return bio;}
    public boolean setBio(String newBio) throws SQLException {
        this.bio = newBio;
        return false;
    }

    public String getPictureURL() {return pictureUrl;};

    public boolean setPictureURL(String newPicture) throws SQLException {
        this.pictureUrl = newPicture;

        return false;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean setHashedPassword(String newHashedPassword) throws SQLException {
        this.hashedPassword = newHashedPassword;
        return false;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
