import java.sql.*;
import java.util.Scanner;

public class BuySell {
        private static int UserID = 0;

        public static void main(String args[]) throws SQLException {
            Connection con = connect();
            logIn(con);
            mainMenu(UserID, con);
        }

        private static Connection connect(){
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //here BuySell is database name, root is username, and password is empty
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BuySell", "root", "");
                //con.close();
            } catch (Exception e) {System.out.println(e);}
            return con;
        }

        private static void logIn(Connection con) throws SQLException {
            UserID = 0;
            Scanner scan = new Scanner(System.in);
            System.out.println("- Log In - ");
            System.out.println("Type 'l' to log in or 'r' to register: ");
            String choice = scan.nextLine();
            if(choice.equals("l")) {
                System.out.println("Please input your email address: ");
                String UserEmail = scan.nextLine();

                System.out.println("\nPlease input your password: ");
                String UserPassword = scan.nextLine();

                UserID = mysqlDataLayer.logIn(UserEmail, UserPassword, con);

                if (UserID == 0) {
                    System.out.println("*** ERROR: Login credentials invalid ***\n");
                    BuySell.logIn(con);
                } else {
                    System.out.println("Login successful.");
                    System.out.println();
                }
            }

            else if(choice.equals("r")){
                register(con);
                }

            else{
                System.out.println("Please enter either 'l' or 'r'");
                logIn(con);
            }
        }

        private static void register (Connection con) throws SQLException {
            String userSinceDate = String.valueOf(java.time.LocalDate.now());
            Scanner scan = new Scanner(System.in);
            System.out.println("- Registration - ");
            System.out.println("Email Address: ");
            String userEmail = scan.nextLine();

            System.out.println("Password: ");
            String userPassword = scan.nextLine();

            System.out.println("Full Name: ");
            String userFullName = scan.nextLine();

            System.out.println("Display Name: ");
            String userDisplayName = scan.nextLine();

            System.out.println("Phone Number: ");
            String userPhoneNumber = scan.nextLine();

            System.out.println("Date of Birth (YYYY-MM-DD): ");
            String userDateOfBirth = scan.nextLine();
            
            mysqlDataLayer.register(userEmail, userFullName, userDisplayName, userPassword, userPhoneNumber, userDateOfBirth, userSinceDate, con);
            System.out.println("" + userFullName + " is now registered.");
        }

        private static void mainMenu(int UserID, Connection con) throws SQLException {
            System.out.println("- Main Menu - ");
            System.out.println("What would you like to do next? Press \"h\" for options.");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();


            if(input.equals("h")){
                System.out.println("Type \'ci\' to create new item");
                System.out.println("Type \'cl\' to create new location");
                System.out.println("Type \'cs\' to create new school");
                System.out.println("Type \'vu\' to view a user profile");
                System.out.println("Type \'vi\' to view item");
                System.out.println("Type \'si\' to search for items");
                System.out.println("Type \'ru\' to rate a user");

                if (mysqlDataLayer.testModerator(UserID, con)){
                    System.out.println("Type \'m\' to use moderator modification");
                }
            input = scan.next();
            }
            if (input.equals("ci")) {
                createItem(UserID, con);
            }

            if (input.equals("cl")) {
                createLocation(con);
            }

            if (input.equals("cs")){
                createSchool(con);
            }

            if (input.equals("si")){
                searchItem(UserID, con);
            }

            if (input.equals("vu")){
                viewUser(con);
            }
            if (input.equals("vi")){
                viewItem(con);
            }
        }

        private static void searchItem(int UserID, Connection con) throws SQLException {
            System.out.println("- Search for Item - ");
            if (!mysqlDataLayer.testBuyer(UserID, con)){
                createBuyer(UserID, con);
            }

            int locationID = 0;
            Scanner scan = new Scanner(System.in);
            System.out.println("Please input Item name: ");
            String itemName = scan.nextLine();

            System.out.println("Please input Item maximum price (as a decimal): ");
            String itemPrice = scan.nextLine();

            if (mysqlDataLayer.testStudent(UserID, con)){
                System.out.println("The system has detected that you are a student. Do you want to search for Items at your school? \'y\' or \'n\'");
                String input = scan.nextLine();
                switch (input) {
                    case "y":
                        int schoolID = mysqlDataLayer.getSchoolID(UserID, con);
                        locationID = mysqlDataLayer.getSchoolLocationID(schoolID, con);
                        break;
                    case "n":
                        locationID = createLocation(con);
                        break;
                    default:
                        locationID = createLocation(con);
                        break;
                }
            }
            ResultSet si = mysqlDataLayer.searchItem(itemName, itemPrice, locationID, con);
            while (si.next()){
                int resultItemID = si.getInt(1);
                String resultItemName = si.getString(2);
                String resultItemPrice = si.getString(3);
                String resultItemCondition = si.getString(4);
                String resultItemDatePosted = si.getString(5);

                System.out.println("Item ID: " + resultItemID);
                System.out.println("Name: " + resultItemName);
                System.out.println("Price: " + resultItemPrice);
                System.out.println("Condition: " + resultItemCondition);
                System.out.println("Date Posted: " + resultItemDatePosted);
                System.out.println();
            }
            mainMenu(UserID, con);
        }

        private static void viewItem (Connection con) throws SQLException{
            Scanner scan = new Scanner(System.in);
            System.out.println("- View Item - ");
            System.out.println("Please input Item ID (Can be found by searching for item): ");
            int itemID = scan.nextInt();

                ResultSet vi = mysqlDataLayer.viewItem(itemID, con);
                while (vi.next()) {
                    String resultItemName = vi.getString(1);
                    double resultItemPrice = vi.getDouble(2);
                    String resultItemCondition = vi.getString(3);
                    String resultItemDatePosted = vi.getString(4);
                    String resultItemBrand = vi.getString(5);
                    String resultItemDescription = vi.getString(6);
                    String resultUserDisplayName = vi.getString(7);
                    float resultSellerRatingAverage = vi.getFloat(8);
                    String resultLocationCity = vi.getString(9);
                    String resultLocationState = vi.getString(10);
                    String resultLocationZipCode = vi.getString(11);
                    String resultLocationAddress = vi.getString(12);

                    System.out.println("Item name: " + resultItemName);
                    System.out.println("Price: $" + resultItemPrice);
                    System.out.println("Condition: " + resultItemCondition);
                    System.out.println("Date posted: " + resultItemDatePosted);
                    System.out.println("Brand: " + resultItemBrand);
                    System.out.println("Description: " + resultItemDescription);
                    System.out.println("Seller: " + resultUserDisplayName);
                    if (resultSellerRatingAverage != 0) {
                        System.out.println("Seller's average rating: " + resultSellerRatingAverage);
                    }
                    System.out.println("Location: " + resultLocationAddress + ", " + resultLocationCity + " " + resultLocationState + ", " + resultLocationZipCode);
                }
                ResultSet vc = mysqlDataLayer.viewComputer(itemID,con);
                while (vc.next()){
                    String resultComputerGeneration = vc.getString(1);
                    String resultComputerProcessor = vc.getString(2);
                    String resultComputerStorageSpace = vc.getString(3);

                    System.out.println("Computer generation: " + resultComputerGeneration);
                    System.out.println("Computer processor: " + resultComputerProcessor);
                    System.out.println("Computer storage space: " + resultComputerStorageSpace);
                }

                ResultSet vb = mysqlDataLayer.viewBook(itemID,con);
                while (vb.next()){
                    String resultBookAuthor = vb.getString(1);
                    String resultBookEdition = vb.getString(2);

                    System.out.println("Book author: " + resultBookAuthor);
                    System.out.println("Book Edition: " + resultBookAuthor);
                }

                ResultSet vcl = mysqlDataLayer.viewClothing(itemID,con);
                while (vcl.next()){
                    String resultClothingGender = vcl.getString(1);
                    String resultClothingSize = vcl.getString(2);
                    String resultClothingColor = vcl.getString(3);

                    System.out.println("Clothing gender: " + resultClothingGender);
                    System.out.println("Clothing size: " + resultClothingSize);
                    System.out.println("Clothing color: " + resultClothingColor);
                }
                    mainMenu(UserID, con);
        }




        private static void viewUser (Connection con) throws SQLException {
            Scanner scan = new Scanner(System.in);
            System.out.println("- View User - ");
            System.out.println("Please insert the User's display name: ");
            String userDisplayName = scan.nextLine();
            int resultUserID = 0;
            ResultSet vu = mysqlDataLayer.viewUser(userDisplayName, con);
            while (vu.next()) {
                String resultUserDisplayName = vu.getString(1);
                String resultUserEmail = vu.getString(2);
                String resultUserPhoneNumber = vu.getString(3);
                String resultUserDateOfBirth = vu.getString(4);
                String resultUserSinceDate = vu.getString(5);
                resultUserID = vu.getInt(6);

                if (resultUserID != 0) {
                    System.out.println("User display name: " + resultUserDisplayName);
                    System.out.println("Email: " + resultUserEmail);
                    System.out.println("Phone Number: " + resultUserPhoneNumber);
                    System.out.println("DOB: " + resultUserDateOfBirth);
                    System.out.println("User since: " + resultUserSinceDate);

                    int schoolID = mysqlDataLayer.getSchoolID(resultUserID, con);
                    if (schoolID != 0) {
                        System.out.println("Attending School: " + mysqlDataLayer.getSchoolName(schoolID, con));
                    }

                    int buyerRatingAverage = mysqlDataLayer.getBuyerRatingAverage(resultUserID, con);
                    if (buyerRatingAverage != 0){
                        System.out.println("Buyer average rating: " + buyerRatingAverage);
                    }
                    int sellerRatingAverage = mysqlDataLayer.getSellerRatingAverage(resultUserID, con);
                    if (sellerRatingAverage != 0){
                        System.out.println("Seller rating average: " + sellerRatingAverage);
                    }

                else if (resultUserID == 0){
                        System.out.println("User doesn't exist.");
                    }
                    mainMenu(UserID, con);
                }
            }
        }

        public static void createSeller(int UserID, Connection con) throws SQLException {
            System.out.println("Uh oh, you're not listed as a Seller yet. The system will now register you as a Seller.");
            mysqlDataLayer.createSeller(UserID, 0, con);
            System.out.println("Congratulations, you're now listed as a Beller.");
        }

        public static void createBuyer(int UserID, Connection con) throws SQLException {
            System.out.println("Uh oh, you're not listed as a Buyer yet. The system will now register you as a Buyer.");
            mysqlDataLayer.createBuyer(UserID, 0, con);
            System.out.println("Congratulations, you're now listed as a Buyer.");
        }

        private static void createItem(int UserID, Connection con) throws SQLException {
            System.out.println("- Create Item - ");
            if (!mysqlDataLayer.testSeller(UserID, con)){
                createSeller(UserID, con);
            }

            int itemID = 0;
            String date = String.valueOf(java.time.LocalDate.now());
            int locationID = 0;
            System.out.println("Please input Item Name: ");
            Scanner scan = new Scanner(System.in);
            String itemName = scan.nextLine();

            System.out.println("Please input Item Price (as a decimal): ");
            String itemPrice = scan.nextLine();

            System.out.println("Please input Item Condition: ");
            String itemCondition = scan.nextLine();

            System.out.println("Please input Item Brand: ");
            String itemBrand = scan.nextLine();

            System.out.println("Please input Item description: ");
            String itemDescription = scan.nextLine();

            if (mysqlDataLayer.testStudent(UserID, con)){
                System.out.println("The system has detected that you are a student. Do you want to set the Item's location to your school? \'y\' or \'n\'");
                String input = scan.nextLine();
                switch (input) {
                    case "y":
                        int schoolID = mysqlDataLayer.getSchoolID(UserID, con);
                        locationID = mysqlDataLayer.getSchoolLocationID(schoolID, con);
                        break;
                    case "n":
                        locationID = createLocation(con);
                        break;
                    default:
                        locationID = createLocation(con);
                        break;
                }
                itemID = mysqlDataLayer.createItem(UserID, locationID, itemName, itemPrice, itemCondition, date, itemBrand, itemDescription, con);
            }
            System.out.println("Is item a computer \'c\', book \'b\', clothing \'l\', or other \'o\'?");
            String input = scan.nextLine();

            switch (input) {
                case "c":
                    System.out.println("Please input the Computer's generation: ");
                    String computerGeneration = scan.nextLine();

                    System.out.println("Please input the Computer's processor: ");
                    String computerProcessor = scan.nextLine();

                    System.out.println("Please input the Computer's storage space: ");
                    String computerStorageSpace = scan.nextLine();

                    mysqlDataLayer.createComputer(itemID, computerGeneration, computerProcessor, computerStorageSpace, con);

                    break;
                case "b":
                    System.out.println("Please input the Book's author: ");
                    String bookAuthor = scan.nextLine();

                    System.out.println("Please input the Book's edition: ");
                    String bookEdition = scan.nextLine();

                    mysqlDataLayer.createBook(itemID, bookAuthor, bookEdition, con);
                    break;
                case "l":
                    System.out.println("Please input Clothing gender 'm' or 'f': ");
                    String clothingGender = scan.nextLine();

                    System.out.println("Please input Clothing's size: ");
                    String clothingSize = scan.nextLine();

                    System.out.println("Please input Clothing's color: ");
                    String clothingColor = scan.nextLine();

                    mysqlDataLayer.createClothing(itemID, clothingGender, clothingColor, clothingSize, con);
                    break;
                default:
                    mainMenu(UserID, con);
                    break;
            }
        }

        private static int createLocation(Connection con) throws SQLException {
            System.out.println("- Create Location - ");
            Scanner scan = new Scanner(System.in);
            System.out.println("Please enter a Location City: ");
            String locationCity = scan.next();

            System.out.println("Please enter a Location State (2-digit abbreviation): ");
            String locationState = scan.next();

            System.out.println("Please enter a Location Zip Code: ");
            String locationZipCode = scan.next();

            System.out.println("Please enter a Location Address: ");
            String locationAddress = scan.next();

            int testID = (mysqlDataLayer.getLocationID(locationCity, locationState, locationZipCode, locationAddress, con));
            if (testID == 0){
                mysqlDataLayer.createLocation(locationCity, locationState, locationZipCode, locationAddress, con);
                int locationID = mysqlDataLayer.getLocationID(locationCity, locationState, locationZipCode, locationAddress, con);
                System.out.println("Location created successfully.");
                return locationID;
            }
            else {
                System.out.println(testID);
                System.out.println("Location already exists.");
                return testID;
            }

        }

        private static void createSchool(Connection con) throws SQLException {
            System.out.println("- Create School - ");
            Scanner scan = new Scanner(System.in);
            System.out.println("Please input School's name: ");
            String schoolName = scan.nextLine();

            System.out.println("Please input School's abbreviation: ");
            String schoolAbbreviation = scan.nextLine();

            int locationID = createLocation(con);

            mysqlDataLayer.createSchool(locationID, schoolName, schoolAbbreviation, con);
            System.out.println("School created successfully.");
        }

        private static void moderatorModification(Connection con){
            Scanner scan = new Scanner(System.in);
            System.out.println("Modify School 's', or Location 'l'?");
            String choice = scan.nextLine();


        }
}