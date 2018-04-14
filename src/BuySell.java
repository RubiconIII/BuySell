import java.sql.*;
import java.util.Scanner;

public class BuySell {

        public static void main(String args[]) throws SQLException {
            Connection con = connect();
            int UserID = logIn(con);
            dashboardMenu(UserID, con);
        }
        public static Connection connect(){
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //here BuySell is database name, root is username, and password is empty
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BuySell", "root", "");
                //con.close();
            } catch (Exception e) {System.out.println(e);}
            return con;
        }

        public static int logIn(Connection con) throws SQLException {
            Scanner scan = new Scanner(System.in);
            System.out.println("Type 'l' to log in or 'r' to register: ");
            String choice = scan.next();
            if(choice.equals("l")) {
                System.out.println("- LOG IN - ");
                System.out.println("Please input your email address: ");
                String UserEmail = scan.next();

                System.out.println("\nPlease input your password: ");
                String UserPassword = scan.next();

                int UserID = mysqlDataLayer.logIn(UserEmail, UserPassword, con);

                if (UserID == 0) {
                    System.out.println("*** ERROR: Login credentials invalid ***\n");
                    BuySell.logIn(con);
                } else {
                    System.out.println("Login successful.");
                    return UserID;
                }
            }
            else if(choice.equals("r")){
                System.out.println("- REGISTRATION - ");
                System.out.println("Email Address: ");
                String UserEmail = scan.next();

                System.out.println("Password: ");
                String UserPassword = scan.next();

                System.out.println("Full Name: ");
                String UserFullName = scan.next();

                System.out.println("Display Name: ");
                String UserDisplayName = scan.next();

                System.out.println("Phone Number: ");
                String UserPhoneNumber = scan.next();

                System.out.println("Date of Birth: ");
                String UserDateOfBirth = scan.next();
                }

           return 0;
        }

        public static void dashboardMenu(int UserID, Connection con) throws SQLException {
            System.out.println("What would you like to do next? Press \"h\" for options.");
            Scanner scan = new Scanner(System.in);
            String input = scan.next();

            if(input.equals("h")){
                System.out.println("Type \'c -i\' to create new item");
                System.out.println("Type \'c -l\' to create new location");
                System.out.println("Type \'c -s\' to create new school");
                System.out.println("Type \'v -p\' to view a user profile");
                System.out.println("Type \'v -c\' to view a user's contact information");
                System.out.println("Type \'v -i\' to view item");
                System.out.println("Type \'s -i\' to search for items");
                System.out.println("Type \'r -u\' to rate a user");

                if (mysqlDataLayer.testModerator(UserID, con)){
                    System.out.println("Type \'m\' to use moderator modification");
                }

            }
            else System.out.println("error");
        }

    }

