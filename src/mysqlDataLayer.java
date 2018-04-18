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

    public static boolean testBuyer(int UserID, Connection con) throws SQLException {
        boolean isBuyer = false;
        int BuyerID = 0;

        Statement testS = con.createStatement();
        ResultSet rs = testS.executeQuery("SELECT BuyerID FROM Seller WHERE BuyerID = \"" + UserID + "\";");
        while (rs.next()) {
            BuyerID = rs.getInt(1);
        }
        if (BuyerID != 0) {
            isBuyer = true;
        }
        return isBuyer;
    }


    public static ResultSet searchItem(String ItemName, String ItemPrice, int LocationID, Connection con) throws SQLException {
        Statement sit = con.createStatement();
        ResultSet rs = sit.executeQuery("SELECT ItemID, ItemName, ItemPrice, ItemCondition, ItemDatePosted FROM Item, Location WHERE ItemName LIKE \"" + ItemName + "\"  AND ItemPrice <= \"" + ItemPrice + "\"  AND LocationID = \"" + LocationID + "\";");

        return rs;
    }

    public static ResultSet viewUser(String UserDisplayName, Connection con) throws SQLException {
        Statement vu = con.createStatement();
        ResultSet rs = vu.executeQuery("SELECT UserDisplayName, UserEmail, UserPhoneNumber, UserDateOfBirth, UserSinceDate, UserID FROM User WHERE UserDisplayName = \"" + UserDisplayName + "\";");

        return rs;
    }

    public static int getSchoolID(int UserID, Connection con) throws SQLException {
        int SchoolID = 0;

        Statement gsid = con.createStatement();
        ResultSet rs = gsid.executeQuery("SELECT fkSchoolID FROM Student WHERE StudentID = \"" + UserID + "\";");

        while (rs.next()) {
            SchoolID = rs.getInt(1);
        }
        return SchoolID;
    }

    public static int getSellerRatingAverage(int UserID, Connection con) throws SQLException {
        int SellerRatingAverage = 0;

        Statement gsra = con.createStatement();
        ResultSet rs = gsra.executeQuery("SELECT SellerRatingAverage FROM Seller WHERE SellerID = \"" + UserID + "\";");

        while (rs.next()) {
            SellerRatingAverage = rs.getInt(1);
        }
        return SellerRatingAverage;
    }

    public static int getBuyerRatingAverage(int UserID, Connection con) throws SQLException {
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
}