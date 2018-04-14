USE Buysell;          

-- Select Location Based on Input
	SELECT LocationID FROM Location
		WHERE LocationCity = 'Riverside'
        AND LocationState = 'CA'
        AND LocationZipCode = '92504'
        AND LocationAddress = '8432 Magnolia Ave';

-- Find if user is a student
	SELECT StudentID FROM Student
		WHERE StudentID = 1;
        
-- Get users School
	SELECT fkSchoolID FROM Student 
		WHERE StudentID = 1;
        
-- Get School's Location
	SELECT fkLocationID FROM School 
		WHERE SchoolID = 1;
        
-- Select User's ID For New Student
	SELECT UserID FROM USER 
		WHERE UserDisplayName = 'Curtis' AND UserPassword = '12345';
    
-- Select School With Specific Name
	SELECT SchoolID FROM School 
		WHERE SchoolName = 'California Baptist';
        
-- Select User using UserEmail and UserPassword
	SELECT UserID FROM User
		WHERE UserEmail = 'curtisphohl@gmail.com' AND UserPassword = '12345';

-- Select for view Contact User
	SELECT UserEmail, UserPhoneNumber FROM USER
		WHERE UserDisplayName = 'Curtis';
        
-- Select for view School
	SELECT SchoolName, SchoolAbbreviation FROM School
		WHERE SchoolName = 'California Baptist University';

-- Select 5 most recent items from specific location
	SELECT ItemName, ItemPrice FROM Item 
		WHERE fkLocationID = 1  ORDER BY ItemID DESC LIMIT 5;
        
-- Find if user is a student
	Select UserDisplayName, UserEmail, UserDateOfBirth, UserSinceDate FROM User
		WHERE UserDisplayName = 'Curtis';

-- View For View Profile
	SELECT UserDisplayName, UserEmail, UserDateOfBirth, UserSinceDate
	FROM User
		WHERE UserDisplayName = 'Curtis';

-- View Items for Specific User
	SELECT ItemName, ItemPrice FROM Item, User
		WHERE UserDisplayName = 'Curtis' AND fkSellerID = UserID
			ORDER BY ItemID DESC LIMIT 5;
            
-- View Item
	SELECT ItemName, ItemPrice, ItemCondition, ItemDatePosted, ItemBrand, ItemDescription, UserDisplayName, SellerRatingAverage, LocationCity, LocationState, LocationZipCode, LocationAddress
    FROM Item, User, Seller, Location
		WHERE ItemID = 1 
        AND UserID = fkSellerID 
        AND SellerID = fkSellerID 
        AND fkLocationID = LocationID ;        

-- Computer Results
	SELECT ComputerGeneration, ComputerProcessor, ComputerStorageSpace FROM Computer 
		WHERE ComputerID = 1;
    
-- Book Results
	SELECT BookAuthor, BookEdition FROM Book
		WHERE BookID = 1;
        
-- Clothing Results 
	SELECT ClothingGender, ClothingSize, CLothingColor FROM Clothing
		WHERE ClothingID = 1;
        
-- Search For Items
	SELECT ItemID, ItemName, ItemPrice, ItemCondition, ItemDatePosted FROM Item, Location, School
		WHERE ItemName LIKE 'Ball' 
        AND ItemPrice <= 10.00 
        AND SchoolAbbreviation = 'California Baptist University'
		AND LocationID = School.fkLocationID;
        
-- Find LocationID
	SELECT LocationID FROM Location 
		WHERE LocationCity = 'Riverside'
        AND LocationState = 'CA'
        AND LocationZipCode = '92504'
        AND LocationAddress = '8432 Magnolia Ave';

-- Select for find School
	SELECT SchoolID, SchoolName, SchoolAbbreviation FROM School
		WHERE SchoolName = 'California Baptist University';

-- Update for Moderator 
UPDATE Location
	SET LocationZipCode = '92399'
	WHERE LocationID = 1;
    
-- Determine if User is a buyer
SELECT BuyerID FROM Buyer
	WHERE BuyerID = 1;
    

