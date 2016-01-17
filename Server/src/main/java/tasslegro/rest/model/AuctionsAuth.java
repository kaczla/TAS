package tasslegro.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Aukcja z autoryzacją")
public class AuctionsAuth extends Auctions {
	@ApiModelProperty(value = "Login użytkownika")
	String login = "";
	@ApiModelProperty(value = "Hasło użytkownika")
	String pass = "";

	public AuctionsAuth() {
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

}
