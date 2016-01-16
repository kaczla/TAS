package tasslegro;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import tasslegro.model.AddAuction;
import tasslegro.model.AllAuctions;
import tasslegro.model.AllUsers;
import tasslegro.model.AuctionEdit;
import tasslegro.model.AuctionInfo;
import tasslegro.model.LoginUser;
import tasslegro.model.LogoutUser;
import tasslegro.model.MainSite;
import tasslegro.model.Register;

@Theme("mytheme")
@Widgetset("tasslegro.MyAppWidgetset")
public class MyUI extends UI {
	public Navigator navigator;

	public static final String MAIN = "main";
	public static final String USER = "users";
	public static final String REGISTER = "register";
	public static final String AUCTION_ADD = "auction_add";
	public static final String AUCTION = "auctions";
	public static final String AUCTION_INFO = "auction_info";
	public static final String AUCTION_EDIT = "auction_edit";
	public static final String LOGIN_USER = "login_user";
	public static final String LOGOUT_USER = "logout_user";

	String idAuction = null;
	int userId = -1;
	String userLogin = null;
	String userPass = null;
	Boolean logged = false;

	public MyUI() {
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		this.loadCookies();
		getPage().setTitle("Tasslegro");
		this.navigator = new Navigator(this, this);
		this.navigator.addView(MyUI.MAIN, new MainSite());
		this.navigator.addView(MyUI.REGISTER, new Register());
		this.navigator.addView(MyUI.USER, new AllUsers());
		this.navigator.addView(MyUI.AUCTION_ADD, new AddAuction());
		this.navigator.addView(MyUI.AUCTION, new AllAuctions());
		this.navigator.addView(MyUI.AUCTION_INFO, new AuctionInfo());
		this.navigator.addView(MyUI.AUCTION_EDIT, new AuctionEdit());
		this.navigator.addView(MyUI.LOGIN_USER, new LoginUser());
		this.navigator.addView(MyUI.LOGOUT_USER, new LogoutUser());
		this.navigator.navigateTo(MyUI.MAIN);
	}

	void loadCookies() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for (Cookie cookie : cookies) {
			if ("userLogin".equals(cookie.getName()) && cookie.getValue().isEmpty() == false) {
				this.userLogin = cookie.getValue();
			} else if ("userPass".equals(cookie.getName()) && cookie.getValue().isEmpty() == false) {
				this.userPass = cookie.getValue();
			} else if ("userId".equals(cookie.getName()) && cookie.getValue().isEmpty() == false) {
				this.userId = Integer.parseInt(cookie.getValue());
			}
		}
		if (this.userId > 0 && this.userLogin.isEmpty() == false && this.userPass.isEmpty() == false) {
			this.logged = true;
		}
	}

	public String getIdAuction() {
		return this.idAuction;
	}

	public void setIdAuction(String idAuction) {
		this.idAuction = idAuction;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserLogin() {
		return this.userLogin;
	}

	public void setUserLogin(String userNick) {
		this.userLogin = userNick;
	}

	public String getUserPass() {
		return this.userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public Boolean getLogged() {
		return this.logged;
	}

	public void setLogged(Boolean logged) {
		this.logged = logged;
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
	public static class MyUIServlet extends VaadinServlet {
	}
}
