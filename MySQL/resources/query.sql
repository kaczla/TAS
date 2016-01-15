DROP DATABASE IF EXISTS ONLINE_AUCTIONS;

DROP SCHEMA IF EXISTS ONLINE_AUCTIONS;

CREATE SCHEMA IF NOT EXISTS ONLINE_AUCTIONS DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE ONLINE_AUCTIONS;

DROP DATABASE IF EXISTS ONLINE_AUCTIONS;

CREATE DATABASE ONLINE_AUCTIONS
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

USE ONLINE_AUCTIONS;

CREATE TABLE USERS(
User_ID int NOT NULL AUTO_INCREMENT,
Name varchar(128),
Surname varchar(128),
Email varchar(128) NOT NULL UNIQUE,
Phone int,
Login varchar(128) NOT NULL UNIQUE,
Pass varchar(128) NOT NULL,
Account int,
Address varchar(128),
Town varchar(128),
ZipCode varchar(10),
PRIMARY KEY (User_ID)
) DEFAULT CHARSET=utf8;

CREATE TABLE AUCTIONS(
Auciton_ID int NOT NULL AUTO_INCREMENT,
User_ID int NOT NULL,
Image_ID int DEFAULT 0,
Title varchar(128) NOT NULL,
Description varchar(255) NOT NULL,
Start_Date datetime DEFAULT NOW(),
End_Date datetime DEFAULT NOW(),
Price float NOT NULL,
PRIMARY KEY (Auciton_ID),
FOREIGN KEY (User_ID) REFERENCES USERS(User_ID)
) DEFAULT CHARSET=utf8;

CREATE TABLE IMAGES(
ID int PRIMARY KEY AUTO_INCREMENT,
Image MEDIUMBLOB NOT NULL
);

CREATE TABLE TYPE_PAYMENT(
Type_payment varchar(255)
);

CREATE TABLE PRODUCT_CONDITION(
Product_Condition varchar(255)
);

CREATE TABLE TYPE_SHIPMENT(
Type_Shipment varchar(255)
);

CREATE TABLE PAYMENT(
Payment_ID int NOT NULL AUTO_INCREMENT,
Auction_ID int,
Type_Payment varchar(255),
PRIMARY KEY(Payment_ID),
FOREIGN KEY (Auction_ID) REFERENCES AUCTIONS(Auciton_ID)
);

CREATE TABLE SHIPMENT(
Shipment_ID int NOT NULL AUTO_INCREMENT,
Auction_ID int,
Type_Shipment varchar(255),
PRIMARY KEY(Shipment_ID),
FOREIGN KEY (Auction_ID) REFERENCES AUCTIONS(Auciton_ID)
);

CREATE TABLE BIDDING(
Bidding_ID int NOT NULL AUTO_INCREMENT,
Shipping_Adress varchar(255),
Type_Payment varchar(255),
Auction_ID int,
User_ID int,
Price int,
Date_Bidding date,
PRIMARY KEY (Bidding_ID),
FOREIGN KEY (Auction_ID) REFERENCES AUCTIONS(Auciton_ID),
FOREIGN KEY (User_ID) REFERENCES USERS(User_ID)
);

CREATE TABLE COMMENT(
Comment_ID int NOT NULL AUTO_INCREMENT,
Sender_ID int,
Rating int,
Add_Date datetime NOT NULL DEFAULT NOW(),
Auction_ID int,
Addressee_ID int,
Comment_a LONGTEXT,
PRIMARY KEY (Comment_ID),
FOREIGN KEY (Auction_ID) REFERENCES AUCTIONS(Auciton_ID),
FOREIGN KEY (Sender_ID) REFERENCES USERS(User_ID),
FOREIGN KEY (Addressee_ID) REFERENCES USERS(User_ID)
);

CREATE TABLE MESSAGE(
Message_ID int NOT NULL AUTO_INCREMENT,
Sender_ID int,
Addressee_ID int,
Message LONGTEXT,
Auction_ID int,
Add_Date datetime NOT NULL DEFAULT NOW(),
PRIMARY KEY (Message_ID),
FOREIGN KEY (Auction_ID) REFERENCES AUCTIONS(Auciton_ID),
FOREIGN KEY (Sender_ID) REFERENCES USERS(User_ID),
FOREIGN KEY (Addressee_ID) REFERENCES USERS(User_ID)
);

CREATE VIEW AUCTIONS_VIEW AS SELECT Auciton_ID, User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price FROM AUCTIONS WHERE End_Date > Start_Date;
CREATE VIEW USERS_VIEW AS SELECT User_ID, Name, Surname, Email, Phone, Login, Account, Address, Town, ZipCode FROM USERS;
CREATE VIEW IMAGES_VIEW AS SELECT ID FROM IMAGES;

INSERT INTO USERS( Name, Surname, Email, Phone, Login, Pass, Account, Address, Town, ZipCode )
VALUES ( "", "", "admin@tasslegro.com", 123456789, "admin", "pass123", 0, "", "", "" ),
( "Adam", "Nowak", "nowakadam@gmail.com", 0, "nowak", "abc123", 0, "Plac Wolności 22/4", "Warszawa", "11444");

INSERT INTO AUCTIONS( User_ID, Title, Description, Price, End_Date )
VALUES ( "1", "Title1", "Description1", 1, DATE_ADD(NOW(),INTERVAL 2 WEEK) ),
( "1", "Title2", "Description2", 2, DATE_ADD(NOW(),INTERVAL 2 WEEK) ),
( "1", "Title3", "Description3", 3, DATE_ADD(NOW(),INTERVAL 2 WEEK) ),
( "1", "Title4", "Description4", 4, DATE_ADD(NOW(),INTERVAL 2 WEEK) ),
( "1", "Po terminie", "Po terminie", 1000, NOW() ),
( "1", "Rubin", "Nowy rubin", 24, DATE_ADD(NOW(),INTERVAL 2 WEEK) );
