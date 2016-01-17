package tasslegro.rest.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tasslegro.rest.model.Auctions;
import tasslegro.rest.model.Images;
import tasslegro.rest.model.Users;

public class MySQL {

	private String Driver = "com.mysql.jdbc.Driver";
	private String ConnectionDBAddres = "jdbc:mysql://localhost:3306/";
	Connection ConnectionDB = null;
	Statement StatementDB = null;
	PreparedStatement preparedStatement = null;
	ResultSet ResultDB = null;
	int ResultDBCount = 0;
	String UserName = "root";
	String UserPassword = "root";
	String SQLQueryString;
	Boolean Connected = false;

	int selectLimit = 10;

	public MySQL() throws ClassNotFoundException, SQLException {
		this.StartConnection();
	}

	public MySQL(String driver, String connection_addres) throws ClassNotFoundException, SQLException {
		this.Driver = driver;
		this.ConnectionDBAddres = connection_addres;
		this.StartConnection();
	}

	public Connection getConnection() {
		return this.ConnectionDB;
	}

	public boolean IsConnected() {
		return this.Connected;
	}

	public void finalize() {
		this.Connected = false;
		try {
			if (this.StatementDB != null) {
				this.StatementDB.close();
			}
		} catch (SQLException error) {
			System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
		}
		try {
			if (this.ConnectionDB != null) {
				this.ConnectionDB.close();
			}
		} catch (SQLException error) {
			System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
		}
	}

	public void StartConnection() throws ClassNotFoundException, SQLException {
		Class.forName(this.Driver);
		try {
			this.ConnectionDB = DriverManager.getConnection(this.ConnectionDBAddres, this.UserName, this.UserPassword);
			this.StatementDB = this.ConnectionDB.createStatement();
		} catch (SQLException error) {
			System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
			System.err.println("[ERROR] " + new Date() + ": Can not connect: " + this.ConnectionDBAddres);
			this.Connected = false;
			return;
		} catch (Exception error) {
			System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
			System.err.println("[ERROR] " + new Date() + ": Can not connect: " + this.ConnectionDBAddres);
			this.Connected = false;
			return;
		}
		this.Connected = true;
	}

	/*
	 * USERS *
	 */

	public List<Users> getUsers() throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID, Name, Surname, Email, Phone, Login, Account, Address, Town, ZipCode "
						+ "FROM ONLINE_AUCTIONS.USERS_VIEW";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Users> UserList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Users User = new Users();
					User.setId(this.ResultDB.getInt("User_ID"));
					User.setName(this.ResultDB.getString("Name"));
					User.setSurname(this.ResultDB.getString("Surname"));
					User.setEmail(this.ResultDB.getString("Email"));
					User.setPhone(this.ResultDB.getInt("Phone"));
					User.setLogin(this.ResultDB.getString("Login"));
					User.setAccount(this.ResultDB.getInt("Account"));
					User.setAddress(this.ResultDB.getString("Address"));
					User.setTown(this.ResultDB.getString("Town"));
					User.setZipCode(this.ResultDB.getString("ZipCode"));
					UserList.add(User);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return UserList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public List<Users> getUsersBySearch(String login, String name, String surname) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID, Name, Surname, Email, Phone, Login, Account, Address, Town, ZipCode "
						+ "FROM ONLINE_AUCTIONS.USERS_VIEW WHERE Login LIKE ? AND Name LIKE ? AND Surname LIKE ? ";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, "%" + login + "%");
				this.preparedStatement.setString(2, name + "%");
				this.preparedStatement.setString(3, surname + "%");
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Users> UserList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Users User = new Users();
					User.setId(this.ResultDB.getInt("User_ID"));
					User.setName(this.ResultDB.getString("Name"));
					User.setSurname(this.ResultDB.getString("Surname"));
					User.setEmail(this.ResultDB.getString("Email"));
					User.setPhone(this.ResultDB.getInt("Phone"));
					User.setLogin(this.ResultDB.getString("Login"));
					User.setAccount(this.ResultDB.getInt("Account"));
					User.setAddress(this.ResultDB.getString("Address"));
					User.setTown(this.ResultDB.getString("Town"));
					User.setZipCode(this.ResultDB.getString("ZipCode"));
					UserList.add(User);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return UserList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public List<Users> getUsersByPage(int page) throws SQLException {
		if (this.Connected) {
			if (page < 1) {
				return null;
			}
			try {
				page = (page - 1) * this.selectLimit;
				this.SQLQueryString = "SELECT User_ID, Name, Surname, Email, Phone, Login, Account, Address, Town, ZipCode "
						+ "FROM ONLINE_AUCTIONS.USERS_VIEW LIMIT ? OFFSET ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, this.selectLimit);
				this.preparedStatement.setInt(1, page);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Users> UserList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Users User = new Users();
					User.setId(this.ResultDB.getInt("User_ID"));
					User.setName(this.ResultDB.getString("Name"));
					User.setSurname(this.ResultDB.getString("Surname"));
					User.setEmail(this.ResultDB.getString("Email"));
					User.setPhone(this.ResultDB.getInt("Phone"));
					User.setLogin(this.ResultDB.getString("Login"));
					User.setAccount(this.ResultDB.getInt("Account"));
					User.setAddress(this.ResultDB.getString("Address"));
					User.setTown(this.ResultDB.getString("Town"));
					User.setZipCode(this.ResultDB.getString("ZipCode"));
					UserList.add(User);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return UserList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Users getUserById(int id) throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID, Name, Surname, Email, Phone, Login, Account, Address, Town, ZipCode "
						+ "FROM ONLINE_AUCTIONS.USERS_VIEW WHERE User_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, id);
				this.ResultDB = this.preparedStatement.executeQuery();
				Users User = new Users();
				if (this.ResultDB.next()) {
					User.setId(this.ResultDB.getInt("User_ID"));
					User.setName(this.ResultDB.getString("Name"));
					User.setSurname(this.ResultDB.getString("Surname"));
					User.setEmail(this.ResultDB.getString("Email"));
					User.setPhone(this.ResultDB.getInt("Phone"));
					User.setLogin(this.ResultDB.getString("Login"));
					User.setAccount(this.ResultDB.getInt("Account"));
					User.setAddress(this.ResultDB.getString("Address"));
					User.setTown(this.ResultDB.getString("Town"));
					User.setZipCode(this.ResultDB.getString("ZipCode"));
				} else {
					System.out.println("[LOG] " + new Date() + ": User with id: " + id + " not found!");
					this.ResultDB = null;
					return null;
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return User;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Users getUserByLogin(String login) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID, Name, Surname, Email, Phone, Login, Account, Address, Town, ZipCode "
						+ "FROM ONLINE_AUCTIONS.USERS_VIEW " + "WHERE Login = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, login);
				this.ResultDB = this.preparedStatement.executeQuery();
				Users User = new Users();
				if (this.ResultDB.next()) {
					User.setId(this.ResultDB.getInt("User_ID"));
					User.setName(this.ResultDB.getString("Name"));
					User.setSurname(this.ResultDB.getString("Surname"));
					User.setEmail(this.ResultDB.getString("Email"));
					User.setPhone(this.ResultDB.getInt("Phone"));
					User.setLogin(this.ResultDB.getString("Login"));
					User.setAccount(this.ResultDB.getInt("Account"));
					User.setAddress(this.ResultDB.getString("Address"));
					User.setTown(this.ResultDB.getString("Town"));
					User.setZipCode(this.ResultDB.getString("ZipCode"));
				} else {
					System.out.println("[LOG] " + new Date() + ": User " + login + " not found!");
					this.ResultDB = null;
					return null;
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return User;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public boolean checkExistUserByLogin(String login) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID FROM ONLINE_AUCTIONS.USERS_VIEW WHERE Login = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, login);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.ResultDB = null;
					return true;
				} else {
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	public boolean checkExistUserByEmail(String email) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID FROM ONLINE_AUCTIONS.USERS_VIEW WHERE Email = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, email);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.ResultDB = null;
					return true;
				} else {
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	public boolean checkExistUserById(int id) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID FROM ONLINE_AUCTIONS.USERS_VIEW WHERE User_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, id);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.ResultDB = null;
					return true;
				} else {
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	public boolean checkExistUserByLoginEmail(String login, String email) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID FROM ONLINE_AUCTIONS.USERS_VIEW WHERE Login = ? AND Email = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, login);
				this.preparedStatement.setString(2, email);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.ResultDB = null;
					return true;
				} else {
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	public Users addUser(Users user) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "INSERT INTO ONLINE_AUCTIONS.USERS(Name, Surname, Email, Phone, Login, Pass, Account, Address, Town, ZipCode) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, user.getName());
				this.preparedStatement.setString(2, user.getSurname());
				this.preparedStatement.setString(3, user.getEmail());
				this.preparedStatement.setInt(4, user.getPhone());
				this.preparedStatement.setString(5, user.getLogin());
				this.preparedStatement.setString(6, user.getPass());
				this.preparedStatement.setInt(7, user.getAccount());
				this.preparedStatement.setString(8, user.getAddress());
				this.preparedStatement.setString(9, user.getTown());
				this.preparedStatement.setString(10, user.getZipCode());
				this.preparedStatement.executeUpdate();
				this.ResultDB = this.preparedStatement.getGeneratedKeys();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					user.setId(this.ResultDB.getInt(1));
					this.ResultDB = null;
					return user;
				} else {
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Users updateUser(Users user) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "UPDATE ONLINE_AUCTIONS.USERS SET  Name = ?, Surname = ?, Email = ?, Phone = ?, Pass = ?, Account = ?, Address = ?, Town = ?, ZipCode = ? "
						+ "WHERE User_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, user.getName());
				this.preparedStatement.setString(2, user.getSurname());
				this.preparedStatement.setString(3, user.getEmail());
				this.preparedStatement.setInt(4, user.getPhone());
				this.preparedStatement.setString(5, user.getPass());
				this.preparedStatement.setInt(6, user.getAccount());
				this.preparedStatement.setString(7, user.getAddress());
				this.preparedStatement.setString(8, user.getTown());
				this.preparedStatement.setString(9, user.getZipCode());
				this.preparedStatement.setInt(10, user.getId());
				this.preparedStatement.executeUpdate();
				this.ResultDBCount = this.preparedStatement.getUpdateCount();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return user;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Users deleteUserById(Users user) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "DELETE FROM ONLINE_AUCTIONS.USERS WHERE User_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, user.getId());
				this.ResultDBCount = this.preparedStatement.executeUpdate();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return user;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Boolean checkAuthorization(String login, String pass) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT User_ID FROM ONLINE_AUCTIONS.USERS WHERE Login = ? AND Pass = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, login);
				this.preparedStatement.setString(2, pass);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return true;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	/*
	 * AUCTIONS *
	 */

	public List<Auctions> getAuctions() throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT Auciton_ID, User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price "
						+ "FROM ONLINE_AUCTIONS.AUCTIONS_VIEW";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Auctions> AuctionList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Auctions Auction = new Auctions();
					Auction.setAucitonId(this.ResultDB.getInt("Auciton_ID"));
					Auction.setUserId(this.ResultDB.getInt("User_ID"));
					Auction.setImageId(this.ResultDB.getInt("Image_ID"));
					Auction.setTitle(this.ResultDB.getString("Title"));
					Auction.setDescription(this.ResultDB.getString("Description"));
					Auction.setStartDate(this.ResultDB.getString("Start_Date"));
					Auction.setEndDate(this.ResultDB.getString("End_Date"));
					Auction.setPrice(this.ResultDB.getFloat("Price"));
					AuctionList.add(Auction);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return AuctionList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public List<Auctions> getAuctionBySearch(String title, String description, float price, int userId) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT Auciton_ID, User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price "
						+ "FROM ONLINE_AUCTIONS.AUCTIONS_VIEW WHERE Title LIKE ? AND Description LIKE ? ";
				if (price > 0) {
					this.SQLQueryString += "AND Price < ? ";
				} else {
					this.SQLQueryString += "AND Price > ? ";
				}
				if (userId > 0) {
					this.SQLQueryString += "AND User_ID = ? ";
				} else {
					this.SQLQueryString += "AND User_ID != ? ";
				}
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, "%" + title + "%");
				this.preparedStatement.setString(2, "%" + description + "%");
				this.preparedStatement.setFloat(3, price);
				this.preparedStatement.setInt(4, userId);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Auctions> AuctionList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Auctions Auction = new Auctions();
					Auction.setAucitonId(this.ResultDB.getInt("Auciton_ID"));
					Auction.setUserId(this.ResultDB.getInt("User_ID"));
					Auction.setImageId(this.ResultDB.getInt("Image_ID"));
					Auction.setTitle(this.ResultDB.getString("Title"));
					Auction.setDescription(this.ResultDB.getString("Description"));
					Auction.setStartDate(this.ResultDB.getString("Start_Date"));
					Auction.setEndDate(this.ResultDB.getString("End_Date"));
					Auction.setPrice(this.ResultDB.getFloat("Price"));
					AuctionList.add(Auction);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return AuctionList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public List<Auctions> getAuctionsByPage(int page) throws SQLException {
		if (this.Connected) {
			if (page < 1) {
				return null;
			}
			try {
				page = (page - 1) * this.selectLimit;
				this.SQLQueryString = "SELECT Auciton_ID, User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price "
						+ "FROM ONLINE_AUCTIONS.AUCTIONS_VIEW LIMIT ? OFFSET ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, this.selectLimit);
				this.preparedStatement.setInt(2, page);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Auctions> AuctionList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Auctions Auction = new Auctions();
					Auction.setAucitonId(this.ResultDB.getInt("Auciton_ID"));
					Auction.setUserId(this.ResultDB.getInt("User_ID"));
					Auction.setImageId(this.ResultDB.getInt("Image_ID"));
					Auction.setTitle(this.ResultDB.getString("Title"));
					Auction.setDescription(this.ResultDB.getString("Description"));
					Auction.setStartDate(this.ResultDB.getString("Start_Date"));
					Auction.setEndDate(this.ResultDB.getString("End_Date"));
					Auction.setPrice(this.ResultDB.getFloat("Price"));
					AuctionList.add(Auction);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return AuctionList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Auctions addAuction(Auctions auction) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "INSERT INTO ONLINE_AUCTIONS.AUCTIONS(User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price) "
						+ "VALUES (?, ?, ?, ?, NOW(), DATE_ADD(NOW(),INTERVAL 2 WEEK), ?)";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, auction.getUserId());
				this.preparedStatement.setInt(2, auction.getImageId());
				this.preparedStatement.setString(3, auction.getTitle());
				this.preparedStatement.setString(4, auction.getDescription());
				this.preparedStatement.setFloat(5, auction.getPrice());
				this.preparedStatement.executeUpdate();
				this.ResultDB = this.preparedStatement.getGeneratedKeys();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					auction.setAucitonId(this.ResultDB.getInt(1));
					this.ResultDB = null;
					return auction;
				} else {
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Auctions updateAuction(Auctions auction) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "UPDATE ONLINE_AUCTIONS.AUCTIONS SET Title = ?, Description = ?, Price = ?, Image_ID = ? "
						+ "WHERE Auciton_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setString(1, auction.getTitle());
				this.preparedStatement.setString(2, auction.getDescription());
				this.preparedStatement.setFloat(3, auction.getPrice());
				this.preparedStatement.setInt(4, auction.getImageId());
				this.preparedStatement.setInt(5, auction.getAucitonId());
				this.preparedStatement.executeUpdate();
				this.ResultDBCount = this.preparedStatement.getUpdateCount();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return auction;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Auctions getAuctionById(int id) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT Auciton_ID, User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price "
						+ "FROM ONLINE_AUCTIONS.AUCTIONS_VIEW WHERE Auciton_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, id);
				this.ResultDB = this.preparedStatement.executeQuery();
				Auctions tmp = new Auctions();
				if (this.ResultDB.next()) {
					tmp.setAucitonId(this.ResultDB.getInt("Auciton_ID"));
					tmp.setUserId(this.ResultDB.getInt("User_ID"));
					tmp.setImageId(this.ResultDB.getInt("Image_ID"));
					tmp.setTitle(this.ResultDB.getString("Title"));
					tmp.setDescription(this.ResultDB.getString("Description"));
					tmp.setStartDate(this.ResultDB.getString("Start_Date"));
					tmp.setEndDate(this.ResultDB.getString("End_Date"));
					tmp.setPrice(this.ResultDB.getFloat("Price"));
				} else {
					System.out.println("[LOG] " + new Date() + ": Auction with id \"" + id + "\" not found!");
					this.ResultDB = null;
					return null;
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return tmp;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Auctions getAuctionByTitleDescriptionId(Auctions auction) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT Auciton_ID, User_ID, Image_ID, Title, Description, Start_Date, End_Date, Price "
						+ "FROM ONLINE_AUCTIONS.AUCTIONS WHERE User_ID = ? AND Title = ? AND Description = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, auction.getUserId());
				this.preparedStatement.setString(2, auction.getTitle());
				this.preparedStatement.setString(3, auction.getDescription());
				this.ResultDB = this.preparedStatement.executeQuery();
				Auctions tmp = new Auctions();
				if (this.ResultDB.next()) {
					tmp.setAucitonId(this.ResultDB.getInt("Auciton_ID"));
					tmp.setUserId(this.ResultDB.getInt("User_ID"));
					tmp.setImageId(this.ResultDB.getInt("Image_ID"));
					tmp.setTitle(this.ResultDB.getString("Title"));
					tmp.setDescription(this.ResultDB.getString("Description"));
					tmp.setStartDate(this.ResultDB.getString("Start_Date"));
					tmp.setEndDate(this.ResultDB.getString("End_Date"));
					tmp.setPrice(this.ResultDB.getFloat("Price"));
				} else {
					System.out.println(
							"[LOG] " + new Date() + ": Auction with title \"" + auction.getTitle() + "\" not found!");
					this.ResultDB = null;
					return null;
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return tmp;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Boolean checkExistAuctionByIds(int auction_id, int user_id) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT Auciton_ID, User_ID FROM ONLINE_AUCTIONS.AUCTIONS_VIEW WHERE Auciton_ID = ? AND User_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, auction_id);
				this.preparedStatement.setInt(2, user_id);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return true;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	public Boolean checkAuctionOfferPrice(Auctions auction) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT Auciton_ID FROM ONLINE_AUCTIONS.AUCTIONS_VIEW WHERE Auciton_ID = ? AND Price < ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, auction.getAucitonId());
				this.preparedStatement.setFloat(2, auction.getPrice());
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return true;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}

	public Auctions updateAuctionOffer(Auctions auction) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "UPDATE ONLINE_AUCTIONS.AUCTIONS SET Price = ?, Bind_ID = ? "
						+ "WHERE Auciton_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setFloat(1, auction.getPrice());
				this.preparedStatement.setInt(2, auction.getBindId());
				this.preparedStatement.setInt(3, auction.getAucitonId());
				this.preparedStatement.executeUpdate();
				this.ResultDBCount = this.preparedStatement.getUpdateCount();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return auction;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Auctions deleteAuctionById(Auctions auction) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "DELETE FROM ONLINE_AUCTIONS.AUCTIONS WHERE Auciton_ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, auction.getAucitonId());
				this.ResultDBCount = this.preparedStatement.executeUpdate();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					return auction;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	/*
	 * IMAGES *
	 */

	public List<Images> getImages() throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT ID FROM ONLINE_AUCTIONS.IMAGES_VIEW";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Images> ImageList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Images Image = new Images();
					Image.setId(this.ResultDB.getInt("ID"));
					ImageList.add(Image);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return ImageList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public List<Images> getImagesByPage(int page) throws SQLException {
		if (this.Connected) {
			if (page < 1) {
				return null;
			}
			try {
				page = (page - 1) * this.selectLimit;
				this.SQLQueryString = "SELECT ID FROM ONLINE_AUCTIONS.IMAGES_VIEW LIMIT ? OFFSET ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, this.selectLimit);
				this.preparedStatement.setInt(2, page);
				this.ResultDB = this.preparedStatement.executeQuery();
				List<Images> ImageList = new ArrayList<>();
				while (this.ResultDB.next()) {
					Images Image = new Images();
					Image.setId(this.ResultDB.getInt("ID"));
					ImageList.add(Image);
				}
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				this.ResultDB = null;
				return ImageList;
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Images addImage(Images image) throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "INSERT INTO ONLINE_AUCTIONS.IMAGES(Image) VALUES(?)";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setBlob(1, image.getImage());
				this.preparedStatement.executeUpdate();
				this.ResultDB = this.preparedStatement.getGeneratedKeys();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					Images tmp = new Images();
					tmp.setId(this.ResultDB.getInt(1));
					this.ResultDB = null;
					return tmp;
				} else {
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Images updateImage(int id, Images image) throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "UPDATE ONLINE_AUCTIONS.IMAGES SET Image = ? WHERE ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setBlob(1, image.getImage());
				this.preparedStatement.setInt(2, id);
				this.preparedStatement.executeUpdate();
				this.ResultDBCount = this.preparedStatement.getUpdateCount();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					image.setId(id);
					return image;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Images getImageById(int id) throws SQLException {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT ID, Image FROM ONLINE_AUCTIONS.IMAGES WHERE ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, id);
				this.ResultDB = this.preparedStatement.executeQuery();
				Images Image = new Images();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					Image.setId(this.ResultDB.getInt("ID"));
					Image.setImage(this.ResultDB.getBinaryStream("Image"));
					this.ResultDB = null;
					return Image;
				} else {
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public Images deleteImageById(int id) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "DELETE FROM ONLINE_AUCTIONS.IMAGES WHERE ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, id);
				this.ResultDBCount = this.preparedStatement.executeUpdate();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDBCount > 0) {
					this.preparedStatement = null;
					this.ResultDB = null;
					Images tmp = new Images();
					tmp.setId(id);
					return tmp;
				} else {
					this.preparedStatement = null;
					this.ResultDB = null;
					return null;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return null;
			}
		}
		return null;
	}

	public boolean checkExistImageById(int id) {
		if (this.Connected) {
			try {
				this.SQLQueryString = "SELECT ID FROM ONLINE_AUCTIONS.IMAGES_VIEW WHERE ID = ?";
				this.preparedStatement = this.ConnectionDB.prepareStatement(this.SQLQueryString);
				this.preparedStatement.setInt(1, id);
				this.ResultDB = this.preparedStatement.executeQuery();
				System.out.println("[LOG] " + new Date() + ": Done query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				if (this.ResultDB.next()) {
					Images Image = new Images();
					Image.setId(this.ResultDB.getInt("ID"));
					return true;
				} else {
					this.ResultDB = null;
					return false;
				}
			} catch (SQLException error) {
				this.ResultDB = null;
				System.err.println("[ERROR] " + new Date() + ": " + error.getMessage());
				System.err.println("[ERROR] " + new Date() + ": Can not do query: " + this.preparedStatement.toString()
						+ " in connection: " + this.ConnectionDBAddres);
				return false;
			}
		}
		return false;
	}
}