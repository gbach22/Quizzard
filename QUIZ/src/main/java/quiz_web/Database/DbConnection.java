package quiz_web.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static quiz_web.Database.DatabaseInfo.*;

public class DbConnection {
    private Connection connection;

    public DbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(SERVER, USERNAME, PASSWORD);

    }

    public Connection getConnection() {
        return connection;
    }

    public void runSqlFile(String filename) throws SQLException {
        int commandCnt = 0;
        List<String> commands;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder sqlStatements = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (isComment(line)) continue;

                sqlStatements.append(line);
                sqlStatements.append(" ");
            }

            String stmnt = sqlStatements.toString();
            commands = splitInCommands(stmnt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Statement stmt = connection.createStatement();
        for (String command : commands) {
            stmt.executeUpdate(command);
        }

    }

    private List<String> splitInCommands(String stmt) {
        List<String> res = new ArrayList<>();

        int begin = 0;
        for (int i = 0; i < stmt.length(); i++) {
            if (stmt.charAt(i) == ';') {
                res.add(stmt.substring(begin, i + 1));

                begin = i + 1;
            }
        }

        return res;
    }


    private boolean isComment(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') continue;
            if (line.charAt(i) == '-' || line.charAt(i) == '<' || line.charAt(i) == '>') {
                return true;
            } else return false;
        }
        return false;
    }

}
