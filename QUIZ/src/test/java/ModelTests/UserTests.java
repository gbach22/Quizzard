package ModelTests;

import org.junit.Test;
import quiz_web.Models.User;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTests {

    @Test
    public void test1() throws NoSuchAlgorithmException {
        ArrayList<String> passwords = new ArrayList<>(Arrays.asList("giorgi", "tako", "nika"));
        HashSet<String> hashPasswords = new HashSet<>();

        for(int i = 0; i < passwords.size(); i++) {
            hashPasswords.add(User.hashPassword(passwords.get(i)));
        }

        assertEquals(hashPasswords.size(), 3); // hashFunction return uniq hashs

        for(int i = 0; i < 3; i++) {
            assertTrue(hashPasswords.contains(User.hashPassword(passwords.get(i))));
        } // hashFunction return always same value for same password
    }

    @Test
    public void tes2() throws NoSuchAlgorithmException {
        String firstName = "Leo";
        String lastName = "Messi";
        String userName = "GOAT";
        String password = "barca123";
        String bio = "iLoveBarca";

        User user = new User(firstName, lastName, userName, password, bio, "", false);

        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(userName, user.getUserName());
        assertEquals(bio, user.getBio());

        // Verify that the password is hashed
        String hashedPassword = User.hashPassword(password);
        assertEquals(hashedPassword, user.getHashedPassword());
    }
}
