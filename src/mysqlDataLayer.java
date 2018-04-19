import java.sql.*;


public class mysqlDataLayer {

    public static int register(String UserEmail, String UserFullName, String UserDisplayName,
                               String UserPassword, String UserPhoneNumber, String UserDateOfBirth,
                               String UserSinceDate, Connection con) throws SQLException {
        int UserID = 0;
        Statement reg = con.createStatement();
        int executeUpdate = reg.executeUpdate("INSERT INTO User (UserID, UserEmail, UserFullName, UserDisplayName, UserPassword, UserPhoneNumber, UserDateOfBirth, UserSinceDate) VALUES (default, \"" + UserEmail + "\",\"" + UserFullName + "\" ,\"" + UserDisplayName + "\",\"" + UserPassword + "\",\"" + UserPhoneNumber + "\",\"" + UserDateOfBirth + "\",\"" + UserSinceDate + "\");");

        UserID = logIn(UserEmail, UserPassword, con);
        return UserID;
    }

    public static int logIn(String UserEmail, String UserPassword, Connection con) throws SQLException {
        int UserID = 0;

        Statement logIn = con.createStatement();

        ResultSet rs = logIn.executeQuery("SELECT UserID FROM User WHERE UserEmail = \""
                + UserEmail + "\" AND UserPassword = \"" + UserPassword + "\";");
        while (rs.next()) {
            UserID = rs.getInt(1);
        }
        return UserID;
    }

    public static int getUserID(String UserDisplayName, Connection con) throws SQLException {
        int UserID = 0;

        Statement guid = con.createStatement();

        ResultSet rs = guid.executeQuery("SELECT UserID FROM User WHERE UserDisplayName = \"" + UserDisplayName + "\";");
        while (rs.next()) {
            UserID = rs.getInt(1);
        }
        return UserID;
    }

    public static boolean testModerator(int UserID, Connection con) throws SQLException {
        boolean isModerator = false;
        int ModeratorID = 0;
        Statement testM = con.createStatement();

        ResultSet rs = testM.executeQuery("SELECT ModeratorID FROM Moderator WHERE ModeratorID = \"" + UserID + "\";");
        while (rs.next()) {
            ModeratorID = rs.getInt(1);
        }
        if (ModeratorID != 0) {
            isModerator = true;
        }
        return isModerator;
    }

    public static boolean testItemIsUsers(int UserID, int ItemID, Connection con) throws SQLException {
        boolean isUsersItem = false;
        int resultItemID = 0;

        Statement testM = con.createStatement();

        ResultSet rs = testM.executeQuery("SELECT ItemID FROM Item WHERE fkSellerID = \"" + UserID + "\";");

        while (rs.next()) {
            resultItemID = rs.getInt(1);
            if (resultItemID == ItemID){
                isUsersItem = true;
            }
        }
        return isUsersItem;
    }

    public static boolean testStudent(int UserID, Connection con) throws SQLException {
        boolean isStudent = false;
        int StudentID = 0;

        Statement testS = con.createStatement();
        ResultSet rs = testS.executeQuery("SELECT StudentID FROM Student WHERE StudentID = \"" + UserID + "\";");
        while (rs.next()) {
            StudentID = rs.getInt(1);
        }
        if (StudentID != 0) {
            isStudent = true;
        }
        return isStudent;
    }

    public static boolean testBuyer(int UserID, Connection con) throws SQLException {
        boolean isBuyer = false;
        int BuyerID = 0;

        Statement testS = con.createStatement();
        ResultSet rs = testS.executeQuery("SELECT BuyerID FROM Buyer WHERE BuyerID = \"" + UserID + "\";");
        while (rs.next()) {
            BuyerID = rs.getInt(1);
        }
        if (BuyerID != 0) {
            isBuyer = true;
        }
        return isBuyer;
    }

    public static boolean testSeller(int UserID, Connection con) throws SQLException {
        boolean isSeller = false;
        int SellerID = 0;

        Statement testS = con.createStatement();
        ResultSet rs = testS.executeQuery("SELECT SellerID FROM Seller WHERE SellerID = \"" + UserID + "\";");
        while (rs.next()) {
            SellerID = rs.getInt(1);
        }
        if (SellerID != 0) {
            isSeller = true;
        }
        return isSeller;
    }

    public static ResultSet viewUsersItems (int UserID, Connection con) throws SQLException {
        Statement vui = con.createStatement();
        ResultSet rs = vui.executeQuery("SELECT ItemID, ItemName, ItemPrice, ItemCondition, ItemDatePosted FROM Item WHERE fkSellerID = " + UserID + ";");

        return rs;
    }

    public static ResultSet viewItem (int ItemID, Connection con) throws SQLException {
        Statement vi = con.createStatement();
        ResultSet rs = vi.executeQuery("SELECT ItemName, ItemPrice, ItemCondition, ItemDatePosted, ItemBrand, ItemDescription, UserDisplayName, SellerRatingAverage, LocationCity, LocationState, LocationZipCode, LocationAddress FROM Item, User, Seller, Location WHERE ItemID = \"" + ItemID + "\" AND UserID = fkSellerID AND SellerID = fkSellerID AND fkLocationID = LocationID;");

        return rs;
    }

    public static ResultSet viewComputer (int ItemID, Connection con) throws SQLException {
        Statement vc = con.createStatement();
        ResultSet rs = vc.executeQuery("SELECT ComputerGeneration, ComputerProcessor, ComputerStorageSpace FROM Computer WHERE ComputerID = \"" + ItemID + "\";");

        return rs;
    }

    public static ResultSet viewBook (int ItemID, Connection con) throws SQLException {
        Statement vb = con.createStatement();
        ResultSet rs = vb.executeQuery("SELECT BookAuthor, BookEdition FROM Book WHERE BookID = \"" + ItemID + "\";");

        return rs;
    }
    public static ResultSet viewClothing (int ItemID, Connection con) throws SQLException {
        Statement vcl = con.createStatement();
        ResultSet rs = vcl.executeQuery("SELECT ClothingGender, ClothingSize, ClothingColor FROM Clothing WHERE ClothingID = \"" + ItemID + "\";");

        return rs;
    }

    public static ResultSet viewUser(String UserDisplayName, Connection con) throws SQLException {
        Statement vu = con.createStatement();
        ResultSet rs = vu.executeQuery("SELECT UserDisplayName, UserEmail, UserPhoneNumber, UserDateOfBirth, UserSinceDate, UserID FROM User WHERE UserDisplayName = \"" + UserDisplayName + "\";");

        return rs;
    }

    public static ResultSet viewLocation(String LocationID, Connection con) throws SQLException {
        Statement vl = con.createStatement();
        ResultSet rs = vl.executeQuery("SELECT LocationID, LocationCity, LocationState, LocationAddress FROM Location WHERE LocationID = \"" + LocationID + "\";");

        return rs;
    }

    public static ResultSet viewSchool (String SchoolName, Connection con) throws SQLException {
        Statement vl = con.createStatement();
        ResultSet rs = vl.executeQuery("SELECT SchoolID, SchoolName, SchoolAbbreviation, fkLocationID FROM School WHERE SchoolName = \"" + SchoolName + "\";");

        return rs;
    }

    public static int getSchoolIDFromStudent(int UserID, Connection con) throws SQLException {
        int SchoolID = 0;
        Statement gsid = con.createStatement();
        ResultSet rs = gsid.executeQuery("SELECT fkSchoolID FROM Student WHERE StudentID = \"" + UserID + "\";");

        while (rs.next()) {
            SchoolID = rs.getInt(1);
        }
        return SchoolID;
    }

    public static int getSchoolIDFromName(String SchoolName, Connection con) throws SQLException {
        int SchoolID = 0;
        Statement gsid = con.createStatement();
        ResultSet rs = gsid.executeQuery("SELECT SchoolID FROM School WHERE SchoolName = \"" + SchoolName + "\";");

        while (rs.next()) {
            SchoolID = rs.getInt(1);
        }
        return SchoolID;
    }


    public static float getSellerRatingAverage(int UserID, Connection con) throws SQLException {
        int SellerRatingAverage = 0;

        Statement gsra = con.createStatement();
        ResultSet rs = gsra.executeQuery("SELECT SellerRatingAverage FROM Seller WHERE SellerID = \"" + UserID + "\";");

        while (rs.next()) {
            SellerRatingAverage = rs.getInt(1);
        }
        return SellerRatingAverage;
    }

    public static float getBuyerRatingAverage(int UserID, Connection con) throws SQLException {
        int BuyerRatingAverage = 0;

        Statement gsra = con.createStatement();
        ResultSet rs = gsra.executeQuery("SELECT BuyerRatingAverage FROM Buyer WHERE BuyerID = \"" + UserID + "\";");

        while (rs.next()) {
            BuyerRatingAverage = rs.getInt(1);
        }
        return BuyerRatingAverage;
    }

    public static String getSchoolName(int SchoolID, Connection con) throws SQLException {
        String SchoolName = "";

        Statement gsid = con.createStatement();
        ResultSet rs = gsid.executeQuery("SELECT SchoolName FROM School WHERE SchoolID = \"" + SchoolID + "\";");

        while (rs.next()) {
            SchoolName = rs.getString(1);
        }
        return SchoolName;
    }

    public static int getSchoolLocationID(int SchoolID, Connection con) throws SQLException {
        int LocationID = 0;
        Statement gslid = con.createStatement();
        ResultSet rs = gslid.executeQuery("SELECT fkLocationID FROM School WHERE SchoolID = \"" + SchoolID + "\";");

        while (rs.next()) {
            LocationID = rs.getInt(1);
        }
        return LocationID;
    }

    public static int getLocationID(String LocationCity, String LocationState, String LocationZipCode, String LocationAddress, Connection con) throws SQLException {
        int LocationID = 0;
        Statement glid = con.createStatement();
        ResultSet rs = glid.executeQuery("SELECT LocationID FROM Location WHERE LocationCity = \"" + LocationCity + "\" AND LocationState = \"" + LocationState + "\" AND LocationZipCode = \"" + LocationZipCode + "\" AND LocationAddress = \"" + LocationAddress + "\";");

        while (rs.next()) {
            LocationID = rs.getInt(1);
        }
        return LocationID;
    }

    public static int getItemID(int SellerID, int LocationID, String ItemName, String ItemPrice, String ItemCondition, String ItemDatePosted, String ItemBrand, String ItemDescription, Connection con) throws SQLException {
        int ItemID = 0;
        Statement giid = con.createStatement();
        ResultSet rs = giid.executeQuery("SELECT ItemID FROM ITEM WHERE fkSellerID = \"" + SellerID + "\" AND fkLocationID = \"" + LocationID + "\" AND ItemName = \"" + ItemName + "\" AND ItemPrice = \"" + ItemPrice + "\" AND ItemCondition = \"" + ItemCondition + "\" AND ItemDatePosted = \"" + ItemDatePosted + "\" AND ItemBrand = \"" + ItemBrand + "\" AND ItemDescription = \"" + ItemDescription + "\";");

        while (rs.next()) {
            ItemID = rs.getInt(1);
        }
        return ItemID;
    }

    public static void createModerator(int ModeratorID, Connection con) throws SQLException {
        Statement cm = con.createStatement();
        int executeUpdate = cm.executeUpdate("INSERT INTO Moderator (ModeratorID) VALUES (" + ModeratorID + ");");
    }

    public static int createItem(int SellerID, int LocationID, String ItemName, String ItemPrice, String ItemCondition, String ItemDatePosted, String ItemBrand, String ItemDescription, Connection con) throws SQLException {
        int ItemID = 0;
        Statement cl = con.createStatement();
        int executeUpdate = cl.executeUpdate("INSERT INTO Item (ItemID, fkSellerID, fkLocationID, ItemName, ItemPrice, ItemCondition, ItemDatePosted, ItemBrand, ItemDescription) VALUES (default, \"" + SellerID + "\", \"" + LocationID + "\", \"" + ItemName + "\", \"" + ItemPrice + "\", \"" + ItemCondition + "\", \"" + ItemDatePosted + "\", \"" + ItemBrand + "\", \"" + ItemDescription + "\");");

        ItemID = getItemID(SellerID, LocationID, ItemName, ItemPrice, ItemCondition, ItemDatePosted, ItemBrand, ItemDescription, con);
        return ItemID;
    }

    public static void createComputer(int ItemID, String ComputerGeneration, String ComputerProcessor, String ComputerStorageSpace, Connection con) throws SQLException {
        Statement cc = con.createStatement();
        int executeUpdate = cc.executeUpdate("INSERT INTO Computer (ComputerID, ComputerGeneration, ComputerProcessor, ComputerStorageSpace) VALUES (\"" + ItemID + "\", \"" + ComputerGeneration + "\", \"" + ComputerProcessor + "\", \"" + ComputerStorageSpace + "\");");
    }

    public static void createBook(int ItemID, String BookAuthor, String BookEdition, Connection con) throws SQLException {
        Statement cb = con.createStatement();
        int executeUpdate = cb.executeUpdate("INSERT INTO Book (BookID, BookAuthor, BookEdition) VALUES (\"" + ItemID + "\", \"" + BookAuthor + "\", \"" + BookEdition + "\");");
    }

    public static void createClothing(int ItemID, String ClothingGender, String ClothingColor, String ClothingSize, Connection con) throws SQLException {
        Statement ccl = con.createStatement();
        int executeUpdate = ccl.executeUpdate("INSERT INTO Clothing (ClothingID, ClothingGender, ClothingColor, ClothingSize) VALUES (\"" + ItemID + "\", \"" + ClothingGender + "\", \"" + ClothingColor + "\", + \"" + ClothingSize + "\");");

    }

    public static void createBuyer(int UserID, int BuyerRatingAverage, Connection con) throws SQLException {
        Statement cs = con.createStatement();
        int executeUpdate = cs.executeUpdate("INSERT INTO Buyer (BuyerID, BuyerRatingAverage) VALUES (\"" + UserID + "\", \"" + BuyerRatingAverage + "\");");
    }

    public static void createStudent(int UserID, int SchoolID, String StudentMajor, String StudentSinceDate, Connection con) throws SQLException {
        Statement cs = con.createStatement();
        int executeUpdate = cs.executeUpdate("INSERT INTO Student (StudentID, fkSchoolID, StudentMajor, StudentSinceDate) VALUES (" + UserID + "," + SchoolID +",\"" + StudentMajor + "\",\"" + StudentSinceDate + "\");");
    }

    public static void createSchool(int LocationID, String SchoolName, String SchoolAbbreviation, Connection con) throws SQLException {
        Statement csl = con.createStatement();
        int executeUpdate = csl.executeUpdate("INSERT INTO School (SchoolID, fkLocationID, SchoolName, SchoolAbbreviation) VALUES (default, \"" + LocationID + "\", \"" + SchoolName + "\", \"" + SchoolAbbreviation + "\");");
    }

    public static void createSeller(int UserID, int SellerRatingAverage, Connection con) throws SQLException {
        Statement cs = con.createStatement();
        int executeUpdate = cs.executeUpdate("INSERT INTO Seller (SellerID, SellerRatingAverage) VALUES (\"" + UserID + "\", \"" + SellerRatingAverage + "\");");
    }

    public static int createLocation(String LocationCity, String LocationState, String LocationZipCode, String LocationAddress, Connection con) throws SQLException {
        int LocationID = 0;
        Statement cl = con.createStatement();
        int executeUpdate = cl.executeUpdate("INSERT INTO Location (LocationID, LocationCity, LocationState, LocationZipcode, LocationAddress) VALUES (default, \"" + LocationCity + "\",\"" + LocationState + "\" ,\"" + LocationZipCode + "\",\"" + LocationAddress + "\");");

        LocationID = getLocationID(LocationCity, LocationState, LocationZipCode, LocationAddress, con);
        return LocationID;
    }


    public static ResultSet searchItem(String ItemName, String ItemPrice, int LocationID, Connection con) throws SQLException {
        Statement sit = con.createStatement();
        ResultSet rs = sit.executeQuery("SELECT ItemID, ItemName, ItemPrice, ItemCondition, ItemDatePosted FROM Item, Location WHERE ItemName LIKE \"" + ItemName + "\"  AND ItemPrice <= \"" + ItemPrice + "\"  AND LocationID = \"" + LocationID + "\";");

        return rs;
    }

    public static ResultSet searchItemBasic(String ItemName, String ItemPrice, String LocationCity, Connection con) throws SQLException {
        Statement sit = con.createStatement();
        ResultSet rs = sit.executeQuery("SELECT ItemID, ItemName, ItemPrice, ItemCondition, ItemDatePosted FROM Item, Location WHERE ItemName LIKE \"" + ItemName + "\" AND ItemPrice <= \"" + ItemPrice + "\" AND LocationCity = \"" + LocationCity + "\" AND item.fkLocationID = location.LocationID;");

        return rs;
    }

    public static float rateBuyer(int ratedID, int raterID, int rating, Connection con) throws SQLException {
        float buyerRatingAvg = 0;
        int numRatings = 0;
        float currentAvg = 0;
        Statement rsl = con.createStatement();
        int executeUpdate = rsl.executeUpdate("INSERT INTO BuyerRating (BuyerRatingID, fkSellerID, fkBuyerID, BuyerRating) VALUES (default, " + raterID + "," + ratedID + ", " + rating + ");");

        ResultSet rs = rsl.executeQuery("SELECT COUNT(*) FROM BuyerRating WHERE fkBuyerID =" + ratedID + ";");
        while(rs.next()){
            numRatings = rs.getInt(1);
        }
        numRatings--;
        ResultSet rs1 = rsl.executeQuery("SELECT BuyerRatingAverage FROM Buyer WHERE BuyerID =" + ratedID);
        while (rs1.next()){
            currentAvg = rs1.getFloat(1);
        }
        buyerRatingAvg = (currentAvg + rating) / numRatings;
        int executeUpdate1 = rsl.executeUpdate("UPDATE Buyer SET BuyerRatingAverage = " + buyerRatingAvg + " WHERE BuyerID = " + ratedID + ";");
        return buyerRatingAvg;
    }

    public static float rateSeller(int ratedID, int raterID, int rating, Connection con) throws SQLException {
        float sellerRatingAvg = 0;
        int numRatings = 0;
        float currentAvg = 0;
        Statement rsl = con.createStatement();
        int executeUpdate = rsl.executeUpdate("INSERT INTO SellerRating (SellerRatingID, fkSellerID, fkBuyerID, SellerRating) VALUES (default, " + ratedID + "," + raterID + ", " + rating + ");");

        ResultSet rs = rsl.executeQuery("SELECT COUNT(*) FROM sellerRating WHERE fkSellerID =" + ratedID + ";");
        while(rs.next()){
             numRatings = rs.getInt(1);
        }
        numRatings--;
        ResultSet rs1 = rsl.executeQuery("SELECT SellerRatingAverage FROM Seller WHERE SellerID =" + ratedID + ";");
        while (rs1.next()){
            currentAvg = rs1.getFloat(1);
        }
        sellerRatingAvg = (currentAvg + rating) / numRatings;
        int executeUpdate1 = rsl.executeUpdate("UPDATE Seller SET SellerRatingAverage = " + sellerRatingAvg + " WHERE SellerID = " + ratedID + ";");
        return sellerRatingAvg;
    }

    public static void moderatorModification(String IsName, String Name, String TableToModify, String AttributeToModify, String NewValue, Connection con) throws SQLException {
        Statement mm = con.createStatement();
        int executeUpdate = mm.executeUpdate("UPDATE " + TableToModify + " SET " + AttributeToModify +" = \"" + NewValue + "\" WHERE " + IsName + " = \"" + Name + "\";");

    }
    public static void deleteItem(int ItemID, Connection con) throws SQLException {
        Statement di = con.createStatement();
        int executeUpdate = di.executeUpdate("DELETE FROM Computer Where ComputerID = " + ItemID + ";");
        int executeUpdate1 = di.executeUpdate("DELETE FROM Book Where BookID = " + ItemID + ";");
        int executeUpdate2 = di.executeUpdate("DELETE FROM Clothing Where ClothingID = " + ItemID + ";");
        int executeUpdate3 = di.executeUpdate("DELETE FROM Item Where ItemID = " + ItemID + ";");


    }
}