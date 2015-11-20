package MySQL;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;
import java.util.Date;

public class MySQL {
	private String Driver;
	private String ConnectionDBAddres;
	Connection ConnectionDB = null;
	Statement StatementDB = null;
	ResultSet ResultDB = null;
	String UserName;
	String UserPassword;
	String SQLQueryString;
	Boolean Connected = false;
	
	public MySQL(){
		this.Driver = "com.mysql.jdbc.Driver";
		this.ConnectionDBAddres = "jdbc:mysql://localhost:3306/";
		this.UserName = "root";
		this.UserPassword = "root";
	}
	
	public MySQL( String driver, String connection_addres ){
		this.Driver = driver;
		this.ConnectionDBAddres = connection_addres;
		this.UserName = "root";
		this.UserPassword = "root";
	}
	
	public void finalize() {
		this.Connected = false;
		try{
			if( this.StatementDB != null ){
				this.StatementDB.close();
			}
		}
		catch( SQLException error ){
			System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
		}
		try{
			if( this.ConnectionDB != null ){
				this.ConnectionDB.close();
			}
		}
		catch( SQLException error ){
			System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
		}
		System.out.println( "[LOG] " + new Date() + ": Connection closed!" );
	}
	
	protected void StartConnection() throws ClassNotFoundException, SQLException{
		System.out.println( "[LOG] " + new Date() + ": Driver: " + this.Driver );
		Class.forName( this.Driver );
		try{
			System.out.println( "[LOG] " + new Date() + ": Connecting: " + this.ConnectionDBAddres );
			this.ConnectionDB = DriverManager.getConnection( this.ConnectionDBAddres, this.UserName, this.UserPassword );
			System.out.println( "[LOG] " + new Date() + ": Connected!" );
			System.out.println( "[LOG] " + new Date() + ": Creating: Statement" );
			this.StatementDB = this.ConnectionDB.createStatement();
			System.out.println( "[LOG] " + new Date() + ": Created: Statement" );
		}
		catch( SQLException error ){
			System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
			System.err.println( "[ERROR] " + new Date() + ": Can not connect: " + this.ConnectionDBAddres );
			this.Connected = false;
			return;
		}
		catch( Exception error ){
			System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
			System.err.println( "[ERROR] " + new Date() + ": Can not connect: " + this.ConnectionDBAddres );
			this.Connected = false;
			return;
		}
		this.Connected = true;
	}
	
	//Create database
	protected void CreateDatabase( String database ) throws SQLException{
		if( this.Connected ){
			try{
				System.out.println( "[LOG] " + new Date() + ": Creating database: " + database );
				this.SQLQueryString = "CREATE DATABASE " + database;
				this.StatementDB.executeUpdate( this.SQLQueryString );
				System.out.println( "[LOG] " + new Date() + ": Created database: " + database );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not create database: " + database + " in connection: "+ this.ConnectionDBAddres );
				return;
			}
		}
	}
	
	//Drop database
	protected void DropDatabase( String database ) throws SQLException{
		if( this.Connected ){
			try{
				System.out.println( "[LOG] " + new Date() + ": Deleting database: " + database );
				this.SQLQueryString = "DROP DATABASE " + database;
				this.StatementDB.executeUpdate( this.SQLQueryString );
				System.out.println( "[LOG] " + new Date() + ": Deleted database: " + database );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not delete database: " + database + " in connection: "+ this.ConnectionDBAddres );
				return;
			}
		}
	}
	
	//Change database
	protected void ChangeDatabase( String database ) throws SQLException{
		if( this.Connected ){
			try{
				System.out.println( "[LOG] " + new Date() + ": Changing database: " + database );
				this.ConnectionDB.setCatalog( database );
				System.out.println( "[LOG] " + new Date() + ": Changed database: " + database );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not change database: " + database + " in connection: "+ this.ConnectionDBAddres );
				return;
			}
		}
	}
	
	//Create table
	protected void CreateTable( String table ) throws SQLException{
		if( this.Connected ){
			try{
				System.out.println( "[LOG] " + new Date() + ": Creating table: " + table );
				this.SQLQueryString = "CREATE TABLE " + table;
				this.StatementDB.executeUpdate( this.SQLQueryString );
				System.out.println( "[LOG] " + new Date() + ": Created table: " + table );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not create table: " + table + " in connection: "+ this.ConnectionDBAddres );
				return;
			}
		}
	}
	
	//Drop table
	protected void DropTable( String table ) throws SQLException{
		if( this.Connected ){
			try{
				System.out.println( "[LOG] " + new Date() + ": Deleting table: " + table );
				this.SQLQueryString = "DROP TABLE " + table;
				this.StatementDB.executeUpdate( this.SQLQueryString );
				System.out.println( "[LOG] " + new Date() + ": Deleted table: " + table );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not delate table: " + table + " in connection: "+ this.ConnectionDBAddres );
				return;
			}
		}
	}
	
	//Do query
	protected void DoQuery( String query ) throws SQLException{
		if( this.Connected ){
			try{
				System.out.println( "[LOG] " + new Date() + ": Doing query: " + query );
				this.SQLQueryString = query;
				this.StatementDB.executeUpdate( this.SQLQueryString );
				System.out.println( "[LOG] " + new Date() + ": Done query:" + query );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not do query: " + query + " in connection: "+ this.ConnectionDBAddres );
				return;
			}
		}
	}
	
	protected void DoQueryFromFile( String file ) throws SQLException, FileNotFoundException{
		if( this.Connected ){
			try{
				InputStream FileName = MySQL.class.getResourceAsStream( file );
				System.out.println( "[LOG] " + new Date() + ": Doing query from file: " + file );
				Scanner File = new Scanner( FileName );
				File.useDelimiter("(;(\r)?\n)|(--\n)");
				while( File.hasNext() ){
					String line = File.next();
					System.out.println( "[LOG] " + new Date() + ": Doing query: " + line );
					if( line.startsWith( "/*!" ) && line.endsWith( "*/" ) ){
						int i = line.indexOf( ' ' );
						line = line.substring( i + 1, line.length() - " */".length() );
					}
					if (line.trim().length() > 0){
						this.SQLQueryString = line;
						this.StatementDB.execute( this.SQLQueryString );
					}
					System.out.println();
				}
				File.close();
				System.out.println( "[LOG] " + new Date() + ": Done query from file: " + file );
			}
			catch( SQLException error ){
				System.err.println( "[ERROR] " + new Date() + ": " + error.getMessage() );
				System.err.println( "[ERROR] " + new Date() + ": Can not do query: " + file + " in connection: " + this.ConnectionDBAddres );
				return;	
			}
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException,SQLException, FileNotFoundException{
		MySQL Database = new MySQL();
		Database.StartConnection();
		Database.DoQueryFromFile( "/query.sql" );
		Database.finalize();
	}
}