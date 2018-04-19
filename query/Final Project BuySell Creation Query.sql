DROP DATABASE IF EXISTS BuySell;
CREATE DATABASE IF NOT EXISTS BuySell;
USE BuySell;

-- Create Location Table
CREATE TABLE
	Location
    (
    LocationID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    LocationCity varchar(30) NOT NULL,
    LocationState varchar(2) NOT NULL,
    LocationZipcode varchar(5),
    LocationAddress varchar(50)
    );

-- Create School Table
CREATE TABLE 
	School
    (
    SchoolID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fkLocationID int,
    SchoolName varchar(50) UNIQUE,
    SchoolAbbreviation varchar(10),
    CONSTRAINT fkLocationID_FK FOREIGN KEY (fkLocationID) REFERENCES Location(LocationID)
    );
    
-- Create User Table
CREATE TABLE 
	User
	(
	UserID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    UserEmail varchar(75) NOT NULL UNIQUE,
    UserFullName varchar(75) NOT NULL,
    UserDisplayName varchar(30) NOT NULL UNIQUE,
    UserPassword varchar(20) NOT NULL,
    UserPhoneNumber varchar(30),
    UserDateOfBirth date NOT NULL,
    UserSinceDate date NOT NULL
    );
    
-- Create Seller Table
CREATE TABLE 
	Seller
    (
    SellerID int NOT NULL PRIMARY KEY,
    SellerRatingAverage float,
    CONSTRAINT SellerID_FK FOREIGN KEY (SellerID) REFERENCES User(UserID)
    );
    
-- Create Buyer Table
CREATE TABLE
	Buyer
    (
    BuyerID int NOT NULL PRIMARY KEY,
    BuyerRatingAverage float,
	CONSTRAINT BuyerID_FK FOREIGN KEY (BuyerID) REFERENCES User(UserID)
    );

-- Create Seller Rating Table   
CREATE TABLE 
	SellerRating
    (
    SellerRatingID int AUTO_INCREMENT PRIMARY KEY,
	fkSellerID int,
    fkBuyerID int,
    SellerRating int,
    CONSTRAINT fkSellerID_FK FOREIGN KEY (fkSellerID) REFERENCES Seller(SellerID),
    CONSTRAINT fkBuyerID_FK FOREIGN KEY (fkBuyerID) REFERENCES Buyer(BuyerID)
    );

-- Create Buyer Rating Table   
CREATE TABLE 
	BuyerRating
    (
    BuyerRatingID int AUTO_INCREMENT PRIMARY KEY,
    fkBuyerID int,
    fkSellerID int,
    BuyerRating int,
	CONSTRAINT bfkBuyerID_FK FOREIGN KEY (fkBuyerID) REFERENCES Buyer(BuyerID),
    CONSTRAINT bfkSellerID_FK FOREIGN KEY (fkSellerID) REFERENCES Seller(SellerID)
    );
    
-- Create Moderator Table
CREATE TABLE
	Moderator 
    (
    ModeratorID int NOT NULL PRIMARY KEY,
	CONSTRAINT ModeratorID_FK FOREIGN KEY (ModeratorID) REFERENCES User(UserID)
    );
    
-- Create Student Table
CREATE TABLE
	Student
    (
    StudentID int NOT NULL PRIMARY KEY,
    fkSchoolID int NOT NULL,
    StudentMajor varchar(50) NOT NULL,
    StudentSinceDate date NOT NULL,
	CONSTRAINT StudentID_FK FOREIGN KEY (StudentID) REFERENCES User(UserID),
    CONSTRAINT fkSchoolID_FK FOREIGN KEY (fkSchoolID) REFERENCES School(SchoolID)
    );

-- Create Item Table
CREATE TABLE
	Item
	(
	ItemID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fkSellerID int NOT NULL,
	fkLocationID int NOT NULL,
	ItemName text NOT NULL,
	ItemPrice decimal NOT NULL,
	ItemCondition varchar(20) NOT NULL,
	ItemDatePosted date NOT NULL,
	ItemBrand varchar(50) NOT NULL,
	ItemDescription text NOT NULL,
	CONSTRAINT cfkSellerID_FK FOREIGN KEY (fkSellerID) REFERENCES Seller(SellerID),
	CONSTRAINT cfkLocationID_FK FOREIGN KEY (fkLocationID) REFERENCES Location(LocationID)
	);

-- Create Computer Table
CREATE TABLE
	Computer
	(
	ComputerID int PRIMARY KEY,
	ComputerGeneration varchar(30),
	ComputerProcessor varchar(30),
	ComputerStorageSpace varchar(20),
	CONSTRAINT ComputerID_FK FOREIGN KEY (ComputerID) REFERENCES Item(ItemID)
    );

-- Create Book Table
CREATE TABLE
	Book
    (
    BookID int PRIMARY KEY,
    BookAuthor varchar(50),
    BookEdition varchar(30),
    CONSTRAINT BookID_FK FOREIGN KEY (BookID) REFERENCES Item(ItemID)
    );
    
-- Create Clothing Table
CREATE TABLE
	Clothing 
    (
    ClothingID int PRIMARY KEY,
    ClothingGender varchar(15),
    ClothingSize varchar(50),
    ClothingColor varchar(20),
    CONSTRAINT ClothingID_FK FOREIGN KEY (ClothingID) REFERENCES Item(ItemID)
    );


    
    
    
    