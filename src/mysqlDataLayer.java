import java.sql.*;


public class mysqlDataLayer {

    public static int register(String UserEmail, String UserFullName, String UserDisplayName,
                               String UserPassword, String UserPhoneNumber, String UserDateOfBirth,
                               String UserSinceDate, Connection con) throws SQLException {
        int UserID = 0;
        Statement logIn=con.createStatement();

        ResultSet rs=logIn.executeQuery("SELECT UserID FROM User WHERE UserEmail = \""
                + UserEmail + "\" AND UserPassword = \"" + UserPassword + "\";");
        return UserID;
    }

    public static int logIn(String UserEmail, String UserPassword, Connection con) throws SQLException {
        int UserID = 0;

        Statement logIn=con.createStatement();

        ResultSet rs=logIn.executeQuery("SELECT UserID FROM User WHERE UserEmail = \""
                                        + UserEmail + "\" AND UserPassword = \"" + UserPassword + "\";");
        while(rs.next()) {
            UserID = rs.getInt(1);
        }
        return UserID;
    }

    public static boolean testModerator(int UserID, Connection con) throws SQLException {
        boolean isModerator = false;
        int ModeratorID = 0;
        Statement logIn=con.createStatement();

        ResultSet rs=logIn.executeQuery("SELECT ModeratorID FROM Moderator WHERE ModeratorID = \"" + UserID + "\";");
        while(rs.next()) {
           ModeratorID = rs.getInt(1);
        }
        if(ModeratorID != 0){
            isModerator = true;
        }
        return isModerator;
        }
    }

