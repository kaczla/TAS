package tasslegro.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Użytkownik")
public class Users {
	@ApiModelProperty(value = "Identyfikator użytkownika")
	private int id;
	@ApiModelProperty(value = "Login użytkownika")
	private String login = "";
	@ApiModelProperty(value = "Hasło użytkownika")
	private String pass = "";
	@ApiModelProperty(value = "Imię użytkownika")
	private String name = "";
	@ApiModelProperty(value = "Nazwisko użytkownika")
	private String surname = "";
	@ApiModelProperty(value = "Email użytkownika")
	private String email = "";
	@ApiModelProperty(value = "Telefon użytkownika")
	private int phone;
	@ApiModelProperty(value = "Numer konta użytkownika")
	private int account;
	@ApiModelProperty(value = "Adres użytkownika")
	private String address = "";
	@ApiModelProperty(value = "Miasto użytkownika")
	private String town = "";
	@ApiModelProperty(value = "Kod pocztowy użytkownika")
	private String zipCode = "";

	public Users() {
	}

	public Users(String login, String pass, String email) {
		this.login = login;
		this.pass = pass;
		this.email = email;
	}

	@ApiModelProperty(value = "Zwraca identyfikator użytkownika")
	public int getId() {
		return this.id;
	}

	@ApiModelProperty(value = "Ustala identyfikator użytkownika")
	public void setId(int id) {
		this.id = id;
	}

	@ApiModelProperty(value = "Zwraca login użytkownika")
	public String getLogin() {
		return this.login;
	}

	@ApiModelProperty(value = "Ustala login użytkownika")
	public void setLogin(String login) {
		this.login = login;
	}

	@ApiModelProperty(value = "Zwraca hasło użytkownika")
	public String getPass() {
		return this.pass;
	}

	@ApiModelProperty(value = "Ustala hasło użytkownika")
	public void setPass(String pass) {
		this.pass = pass;
	}

	@ApiModelProperty(value = "Zwraca imię użytkownika")
	public String getName() {
		return this.name;
	}

	@ApiModelProperty(value = "Ustala imię użytkownika")
	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value = "Zwraca nazwisko użytkownika")
	public String getSurname() {
		return this.surname;
	}

	@ApiModelProperty(value = "Ustala nazwisko użytkownika")
	public void setSurname(String surname) {
		this.surname = surname;
	}

	@ApiModelProperty(value = "Zwraca email użytkownika")
	public String getEmail() {
		return this.email;
	}

	@ApiModelProperty(value = "Ustala email użytkownika")
	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(value = "Zwraca telefon użytkownika")
	public int getPhone() {
		return this.phone;
	}

	@ApiModelProperty(value = "Ustala telefon użytkownika")
	public void setPhone(int phone) {
		this.phone = phone;
	}

	@ApiModelProperty(value = "Zwraca numer konta użytkownika")
	public int getAccount() {
		return this.account;
	}

	@ApiModelProperty(value = "Ustala numer konta użytkownika")
	public void setAccount(int account) {
		this.account = account;
	}

	@ApiModelProperty(value = "Zwraca adres użytkownika")
	public String getAddress() {
		return this.address;
	}

	@ApiModelProperty(value = "Ustala adres użytkownika")
	public void setAddress(String address) {
		this.address = address;
	}

	@ApiModelProperty(value = "Zwraca miasto użytkownika")
	public String getTown() {
		return this.town;
	}

	@ApiModelProperty(value = "Ustala miasto użytkownika")
	public void setTown(String town) {
		this.town = town;
	}

	@ApiModelProperty(value = "Zwraca kod pocztowy użytkownika")
	public String getZipCode() {
		return this.zipCode;
	}

	@ApiModelProperty(value = "Ustala kod pocztowy użytkownika")
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
