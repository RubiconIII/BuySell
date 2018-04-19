import java.sql.*;
import java.util.Scanner;

public class BuySell {
        private static int UserID = 0;

        public static void main(String args[]) throws SQLException {
            Connection con = connect();
            logIn(con);
            mainMenu(con);
        }

        private static Connection connect(){
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //here BuySell is database name, root is username, and password is empty
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BuySell", "root", "");
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

        private static void logout (Connection con) throws SQLException {
            System.out.println("Thanks for using the BuySell application! Goodbye.");
            con.close();
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

            UserID = mysqlDataLayer.register(userEmail, userFullName, userDisplayName, userPassword, userPhoneNumber, userDateOfBirth, userSinceDate, con);
            System.out.println("" + userFullName + " is now registered.");
        }

        private static void mainMenu(Connection con) throws SQLException {
            System.out.println("- Main Menu - ");
            System.out.println("What would you like to do next? Press \"h\" for options.");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            boolean isModerator = mysqlDataLayer.testModerator(UserID, con);

            if(input.equals("h")){
                System.out.println("Type \'ci\' to create new item");
                System.out.println("Type \'cl\' to create new location");
                System.out.println("Type \'cs\' to create new school");
                System.out.println("Type \'di\' to delete an item");
                if (!mysqlDataLayer.testStudent(UserID,con)) {
                    System.out.println("Type \'bs\' to become a student");
                }
                System.out.println("Type \'si\' to search for items");
                System.out.println("Type \'vu\' to view a user profile");
                if(mysqlDataLayer.testSeller(UserID,con)){
                    System.out.println("Type \'vmi\' to view your items for sale");
                }
                System.out.println("Type \'vi\' to view a specific item");
                System.out.println("Type \'vl\' to view location");
                System.out.println("Type \'vs\' to view school");
                System.out.println("Type \'ru\' to rate a user");

                if (isModerator){
                    System.out.println("Type \'mm\' to use moderator modification");
                    System.out.println("Type \'mp\' to give a user moderator privileges");
                }
                System.out.println("Type \'lo\' to log out");

            input = scan.next();
            }
            if (input.equals("ci")) {
                createItem(con);
            }

            if (input.equals("cl")) {
                createLocation(true, con);
            }

            if (input.equals("cs")){
                createSchool(true, con);
            }

            if(input.equals("di")){
                deleteItem(con);
            }

            if (!mysqlDataLayer.testStudent(UserID, con) && input.equals("bs")){
                createStudent(con);
            }

            if (input.equals("si")){
                searchItem(con);
            }

            if (input.equals("vu")){
                viewUser(con);
            }

            if (mysqlDataLayer.testSeller(UserID, con) && input.equals("vmi")){
                viewUsersItems(con);
            }

            if (input.equals("vi")){
                viewItem(con);
            }

            if (input.equals("vl")) {
                viewLocation(true, "", con);
            }

            if (input.equals("vs")){
                viewSchool(true, "", con);
            }

            if (input.equals("ru")){
                rateUser(con);
            }

            if (isModerator && input.equals("mm")) {
                moderatorModification(con);
            }
            if (isModerator && input.equals("mp")) {
                addModerator(con);
            }
            if(input.equals("lo")){
                logout(con);
            }

        }

        private static void searchItem(Connection con) throws SQLException {
            ResultSet si, sib;
            System.out.println("- Search for Item - ");
            if (!mysqlDataLayer.testBuyer(UserID, con)){
                createBuyer(con);
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
                        int schoolID = mysqlDataLayer.getSchoolIDFromStudent(UserID, con);
                        locationID = mysqlDataLayer.getSchoolLocationID(schoolID, con);
                        si = mysqlDataLayer.searchItem(itemName, itemPrice, locationID, con);
                        break;
                    case "n":
                        System.out.println("Please input the city you would like to search within: ");
                        String LocationCity = scan.nextLine();
                        si = mysqlDataLayer.searchItemBasic(itemName, itemPrice, LocationCity, con);
                        break;
                    default:
                        locationID = createLocation(false, con);
                        si = mysqlDataLayer.searchItem(itemName, itemPrice, locationID, con);
                        break;
                }
            }
            else {
                System.out.println("Please input the city you would like to search within: ");
                String LocationCity = scan.nextLine();
                si = mysqlDataLayer.searchItemBasic(itemName, itemPrice, LocationCity, con);
            }

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
            mainMenu(con);
        }

        private static void viewUsersItems(Connection con) throws SQLException {
            System.out.println("- View Your Items -");
            ResultSet si = mysqlDataLayer.viewUsersItems(UserID, con);

            while (si.next()) {
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
            mainMenu(con);
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
                    mainMenu(con);
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

                    int schoolID = mysqlDataLayer.getSchoolIDFromStudent(resultUserID, con);
                    String schoolName = mysqlDataLayer.getSchoolName(schoolID, con);

                    if (schoolID != 0) {
                        viewSchool(false, schoolName, con);
                    }

                    float buyerRatingAverage = mysqlDataLayer.getBuyerRatingAverage(resultUserID, con);
                    if (buyerRatingAverage != 0){
                        System.out.println("Buyer average rating: " + buyerRatingAverage);
                    }
                    float sellerRatingAverage = mysqlDataLayer.getSellerRatingAverage(resultUserID, con);
                    if (sellerRatingAverage != 0){
                        System.out.println("Seller rating average: " + sellerRatingAverage);
                    }

                else if (resultUserID == 0){
                        System.out.println("User doesn't exist.");
                    }
                    mainMenu(con);
                }
            }
        }

    private static void viewLocation(boolean insertName, String searchName, Connection con) throws SQLException {
        String name = searchName;
        Scanner scan = new Scanner(System.in);
        if (insertName){
            System.out.println("Please input location's ID: ");
            name = scan.nextLine();
        }

        ResultSet vl = mysqlDataLayer.viewLocation(name, con);
        while(vl.next()){
            int locationID = vl.getInt(1);
            String locationCity = vl.getString(2);
            String locationState = vl.getString(3);
            String locationAddress = vl.getString(4);

            System.out.println("Location ID: " + locationID);
            System.out.println("Location City: " + locationCity);
            System.out.println("Location State: " + locationState);
            System.out.println("Location Address: " + locationAddress);
            System.out.println();
        }
        if (insertName){
            mainMenu(con);
        }
    }

    private static void viewSchool(boolean insertName, String searchName, Connection con) throws SQLException {
        Scanner scan = new Scanner(System.in);
        String name = searchName;
        if (insertName){
            System.out.println("Please input School's name: ");
            name = scan.nextLine();

        }
        ResultSet vs = mysqlDataLayer.viewSchool(name, con);
        while (vs.next()) {
            int schoolID = vs.getInt(1);
            String schoolName = vs.getString(2);
            String schoolAbbreviation = vs.getString(3);
            int fkLocationID = vs.getInt(4);

            System.out.println("School ID: " + schoolID);
            System.out.println("School Name: " + schoolName);
            System.out.println("School Abbreviation: " + schoolAbbreviation);
            System.out.println("School's Location ID: " + fkLocationID);
            System.out.println();

        }
        if (insertName){
            mainMenu(con);
        }
    }

        private static void createSeller(Connection con) throws SQLException {
            System.out.println("Uh oh, you're not listed as a Seller yet. The system will now register you as a Seller.");
            mysqlDataLayer.createSeller(UserID, 0, con);
            System.out.println("Congratulations, you're now listed as a Seller.");
        }

        private static void createBuyer(Connection con) throws SQLException {
            System.out.println("Uh oh, you're not listed as a Buyer yet. The system will now register you as a Buyer.");
            mysqlDataLayer.createBuyer(UserID, 0, con);
            System.out.println("Congratulations, you're now listed as a Buyer.");
        }

        private static void createStudent(Connection con) throws SQLException {
            String studentSinceDate = String.valueOf(java.time.LocalDate.now());
            Scanner scan = new Scanner(System.in);
            int schoolID = 0;
            String schoolName = "";
            System.out.println("Input the name of your School: ");
            schoolName = scan.nextLine();

            schoolID = mysqlDataLayer.getSchoolIDFromName(schoolName, con);

            if (schoolID != 0) {
                System.out.println("Input your major: ");
                String studentMajor = scan.nextLine();

                mysqlDataLayer.createStudent(UserID, schoolID, studentMajor, studentSinceDate, con);
                System.out.println("Congratulations, you are now listed as a student.");
            }else{
                System.out.println("The school you input does not exist.  Please create a new school.");
                createSchool(false, con);

                System.out.println("Great. Let's try again: ");
                System.out.println("Input the name of your School: ");
                schoolName = scan.nextLine();
                schoolID = mysqlDataLayer.getSchoolIDFromName(schoolName, con);

                System.out.println("Input your major: ");
                String studentMajor = scan.nextLine();

                mysqlDataLayer.createStudent(UserID, schoolID, studentMajor, studentSinceDate, con);
                System.out.println("Congratulations, you are now listed as a Student.");
            }
        }

        private static void createItem(Connection con) throws SQLException {
            System.out.println("- Create Item - ");
            if (!mysqlDataLayer.testSeller(UserID, con)){
                createSeller(con);
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
                        int schoolID = mysqlDataLayer.getSchoolIDFromStudent(UserID, con);
                        locationID = mysqlDataLayer.getSchoolLocationID(schoolID, con);
                        break;
                    case "n":
                        locationID = createLocation(false, con);
                        break;
                    default:
                        locationID = createLocation(false, con);
                        break;
                }
            }
            else {
                locationID = createLocation(false, con);
            }
            itemID = mysqlDataLayer.createItem(UserID, locationID, itemName, itemPrice, itemCondition, date, itemBrand, itemDescription, con);

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
                    System.out.println("Your Item: " + itemName + " was added.");
                    mainMenu(con);
                    break;
                case "b":
                    System.out.println("Please input the Book's author: ");
                    String bookAuthor = scan.nextLine();

                    System.out.println("Please input the Book's edition: ");
                    String bookEdition = scan.nextLine();

                    mysqlDataLayer.createBook(itemID, bookAuthor, bookEdition, con);
                    System.out.println("Your Item: " + itemName + " was added.");
                    mainMenu(con);
                    break;
                case "l":
                    System.out.println("Please input Clothing gender 'm' or 'f': ");
                    String clothingGender = scan.nextLine();

                    System.out.println("Please input Clothing's size: ");
                    String clothingSize = scan.nextLine();

                    System.out.println("Please input Clothing's color: ");
                    String clothingColor = scan.nextLine();

                    mysqlDataLayer.createClothing(itemID, clothingGender, clothingColor, clothingSize, con);
                    System.out.println("Your Item: " + itemName + " was added.");
                    mainMenu(con);
                    break;
                default:
                    System.out.println("Your Item: " + itemName + " was added.");
                    mainMenu(con);
                    break;
            }
        }

        private static int createLocation(boolean callMenu, Connection con) throws SQLException {
            System.out.println("- Create Location - ");
            Scanner scan = new Scanner(System.in);
            System.out.println("Please input the Location City: ");
            String locationCity = scan.nextLine();

            System.out.println("Please input the Location State (2-digit abbreviation): ");
            String locationState = scan.nextLine();

            System.out.println("Please input the Location Zip Code: ");
            String locationZipCode = scan.nextLine();

            System.out.println("Please input the Location Address: ");
            String locationAddress = scan.nextLine();

            int testID = (mysqlDataLayer.getLocationID(locationCity, locationState, locationZipCode, locationAddress, con));
            if (testID == 0){
                mysqlDataLayer.createLocation(locationCity, locationState, locationZipCode, locationAddress, con);
                int locationID = mysqlDataLayer.getLocationID(locationCity, locationState, locationZipCode, locationAddress, con);
                System.out.println("Location created successfully with ID: " + locationID);
                if (callMenu){
                    mainMenu(con);
                }
                return locationID;
            }
            else {
                System.out.println("Location already exists with ID: " + testID);
                if (callMenu){
                    mainMenu(con);
                }
                return testID;
            }
        }

        private static void createSchool(boolean callMenu, Connection con) throws SQLException {
            System.out.println("- Create School - ");
            Scanner scan = new Scanner(System.in);

            System.out.println("Please input School's name: ");
            String schoolName = scan.nextLine();

            System.out.println("Please input School's abbreviation: ");
            String schoolAbbreviation = scan.nextLine();

            int locationID = createLocation(false, con);

            mysqlDataLayer.createSchool(locationID, schoolName, schoolAbbreviation, con);
            System.out.println("School created successfully.");
            if (callMenu) {
                mainMenu(con);
            }
        }

        private static void rateUser(Connection con) throws SQLException {
            System.out.println("- Rate User -");
            Scanner scan = new Scanner(System.in);

            System.out.println("Please input the display name of the User you would like to rate: ");
            String ratedUserDisplayName = scan.nextLine();
            int ratedUserID = mysqlDataLayer.getUserID(ratedUserDisplayName, con);

            System.out.println("Are you rating a buyer 'b', or a seller 's'?");
            String choice = scan.nextLine();

            if (choice.equals("b")) {
                boolean ratedIsBuyer;
                boolean raterIsSeller;
                ratedIsBuyer = mysqlDataLayer.testBuyer(ratedUserID, con);
                raterIsSeller = mysqlDataLayer.testSeller(UserID, con);

                if (ratedIsBuyer && (ratedUserID != UserID) && raterIsSeller) {
                    System.out.println("What do you rate Buyer? (Rating can be from 1 to 5):");
                    int rating = scan.nextInt();
                    float newRating = mysqlDataLayer.rateBuyer(ratedUserID, UserID, rating, con);
                    System.out.println(ratedUserDisplayName + "'s new Buyer rating is: " + newRating);
                } else if (!raterIsSeller) {
                    System.out.println("You are not a Seller, so you can't rate Buyers yet.");
                } else if (!ratedIsBuyer) {
                    System.out.println("This User is not a Buyer.");
                } else if (ratedUserID != UserID){
                    System.out.println("You can't rate yourself.");
                }
            }
            if (choice.equals("s")) {
                boolean ratedIsSeller;
                boolean raterIsBuyer;
                ratedIsSeller = mysqlDataLayer.testSeller(ratedUserID, con);
                raterIsBuyer = mysqlDataLayer.testBuyer(UserID, con);

                if (ratedIsSeller && (ratedUserID != UserID) && raterIsBuyer) {
                    System.out.println("What do you rate Seller? (Rating can be from 1 to 5):");
                    int rating = scan.nextInt();
                    float newRating = mysqlDataLayer.rateSeller(ratedUserID, UserID, rating, con);
                    System.out.println(ratedUserDisplayName + "'s new Seller rating is: " + newRating);
                } else if (!raterIsBuyer) {
                    System.out.println("You are not a Buyer, so you can't rate Sellers yet.");
                } else if (!ratedIsSeller) {
                    System.out.println("This User is not a Seller.");
                } else if (ratedUserID == UserID) {
                    System.out.println("You can't rate yourself.");
                }
            }
            mainMenu(con);
        }


        private static void moderatorModification(Connection con) throws SQLException {
            System.out.println("- Moderator Modification -");
            Scanner scan = new Scanner(System.in);
            System.out.println("Modify School 's', or Location 'l'?");
            String choice = scan.nextLine();
            String attributeToModify = "attribute";
            String newValue = "newValue";
            String name = "name";
            String isName = "isName";
            String tableToModify = "tableToModify";

            if (choice.equals("s")) {
                tableToModify = "School";
                isName = "SchoolName";
                System.out.println("Please input the School's Name: ");
                name = scan.nextLine();

                System.out.println("Is this the School you wish to modify? ('y' or 'n') ");
                viewSchool(false, name, con);
                String input = scan.nextLine();

                if (input.equals("y")) {
                    System.out.println("Please enter School's attribute to modify ('fkLocationID', 'SchoolName', or 'SchoolAbbreviation'): ");
                    attributeToModify = scan.nextLine();

                    System.out.println("Please enter new value for " + attributeToModify + ": ");
                    newValue = scan.nextLine();

                    mysqlDataLayer.moderatorModification(isName, name, tableToModify, attributeToModify, newValue, con);
                    System.out.println("Great! The " + tableToModify + " " + attributeToModify + " is updated. See here: ");
                    viewSchool(false, name, con);
                    mainMenu(con);
                }

            else if (choice.equals("l")) {
               tableToModify = "Location";
               isName = "LocationID";
               System.out.println("Please insert the Location's ID: ");
               name = scan.nextLine();

               System.out.println("Is this the Location you wish to modify? ('y' or 'n') ");
               viewSchool(false, name, con);

               if (input.equals("y")) {
                   System.out.println("Please enter Location's attribute to modify ('LocationCity', 'LocationState', or 'LocationAddress'): ");
                   attributeToModify = scan.nextLine();

                   System.out.println("Please enter new value for " + attributeToModify + ": ");
                   newValue = scan.nextLine();

                   mysqlDataLayer.moderatorModification(isName, name, tableToModify, attributeToModify, newValue, con);
                   System.out.println("Great! The " + tableToModify + " " + attributeToModify + " is updated. See here: ");

                   viewLocation(false, name, con);
                   mainMenu(con);
               }
            }
        }
    }

    private static void addModerator (Connection con) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("- Give Moderator Privileges -");
        System.out.println("Please input the User's display name who should receive Moderator privileges:  ");
        String moderatorName = scan.nextLine();

        int userIDToAdd = mysqlDataLayer.getUserID(moderatorName,con);

        mysqlDataLayer.createModerator(userIDToAdd,con);

        System.out.println();
        System.out.println("User: " + moderatorName + " was updated.");
        mainMenu(con);
    }

    private static void deleteItem (Connection con) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("- Delete Item -");
        System.out.println("Input the ID of the Item you want to delete: ");
        int itemID = scan.nextInt();
        boolean isUsersItem = false;
        isUsersItem = mysqlDataLayer.testItemIsUsers(UserID, itemID, con);

        if(isUsersItem){
            mysqlDataLayer.deleteItem(itemID, con);
            System.out.println("You've deleted the item.");
        }else if (!isUsersItem){
            System.out.println("You can't delete someone else's item.");
        }
        mainMenu(con);
    }
}