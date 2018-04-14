USE BuySell;
-- Insert For New Location
INSERT INTO Location 
	(LocationID, LocationCity, LocationState, LocationZipcode, LocationAddress)
		VALUES
			(default, 'Riverside', 'CA', '92504', '8432 Magnolia Ave');
            
-- Insert for New School
INSERT INTO School
    (SchoolID, fkLocationID, SchoolName, SchoolAbbreviation)
		VALUES
			(default, 1, 'California Baptist University', 'CBU');

-- Insert For New User
INSERT INTO User
    (UserID, UserEmail, UserFullName, UserDisplayName, UserPassword, UserPhoneNumber, UserDateOfBirth, UserSinceDate)
		VALUES
			(default,'curtisphohl@gmail.com', 'Curtis Hohl', 'Curtis', '12345', '1099099099', '2018-04-06', '2018-04-06');
            
-- Insert for New Student
INSERT INTO Student
	(StudentID, fkSchoolID, StudentMajor, StudentSinceDate)
		VALUES
			(1, 1, 'Computer Science', '2018-04-06');

-- Insert for Seller
INSERT INTO Seller
	(SellerID, SellerRatingAverage)
		VALUES
			(1, 0);

-- Insert for Buyer
INSERT INTO Buyer
	(BuyerID, BuyerRatingAverage)
		VALUES
			(1, 0);            

-- Insert for Moderator
INSERT INTO Moderator
	(ModeratorID, ModeratorPrivilege)
		VALUES
			(1, 'This Moderator can do great things');
            
-- Insert for Item
INSERT INTO Item
	(ItemID, fkSellerID, fkLocationID, ItemName, ItemPrice, ItemCondition, ItemDatePosted, ItemBrand, ItemDescription)
		VALUES
			(default, 1, 1, 'Ball', 9.00, 'Pretty Beat', '2018-04-06', 'Ball Inc.', 'I like this ball.  It is red.');
            
-- Insert for Computer
INSERT INTO Computer
	(ComputerID, ComputerGeneration, ComputerProcessor, ComputerStorageSpace)
		VALUES
			(1, 'Ball', '$75.00', 'Pretty Beat');
            
-- Insert for Book
INSERT INTO Book
	(BookID, BookAuthor, BookEdition)
		VALUES
			(1, 'Ball Book', '$75.00');

-- Insert For Clothing
INSERT INTO Clothing
	(ClothingID, ClothingGender, ClothingSize, ClothingColor)
		VALUES
			(1, 'Ball Clothing', '$75.00', 'green');
            
            -- Insert into SellerRating
INSERT INTO SellerRating
	(SellerRatingID, fkSellerID, fkBuyerID, SellerRating)
		VALUES (default, 1, 1, 5);

 