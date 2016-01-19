package tasslegro.model;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.BaseInformation;
import tasslegro.base.Http_Get;
import tasslegro.base.Http_Put;
import tasslegro.base.ImageTasslegro;

public class UserEdit extends CustomComponent implements View, Button.ClickListener {
	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout panel = new HorizontalLayout();
	Button buttonMainSite = new Button("Strona główna", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.MAIN);
		}
	});
	Button buttonLoginUser = new Button("Zaloguj", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.LOGIN_USER);
		}
	});
	Button buttonLogoutUser = new Button("Wyloguj", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.LOGOUT_USER);
		}
	});
	Button buttonUserProfil = new Button("Profil", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.USER_PROFIL);
		}
	});
	Label labelNoLogged = new Label("Nie zalogowany!");
	Label labelLogged = new Label();
	Image imageLogo = new Image();

	Notification notification = null;
	String responseString = null;
	String httpGetURL = BaseInformation.serverURL + "users/";
	String httpPutURL = BaseInformation.serverURL + "users/";

	Label userLogin = new Label();
	PasswordField userPass = new PasswordField();
	PasswordField userPassrep = new PasswordField();
	TextField userEmail = new TextField();
	TextField userName = new TextField();
	TextField userSurname = new TextField();
	TextField userAddress = new TextField();
	TextField userZipCode = new TextField();
	TextField userTown = new TextField();
	TextField userPhone = new TextField();
	TextField userAccount = new TextField();
	Button buttonSend = new Button("Aktualizuj");

	Label labelLogin = new Label("Login:");
	Label labelPass = new Label("Nowe hasło:");
	Label labelPassrep = new Label("Powtórz nowe hasło:");
	Label labelEmail = new Label("Email:");
	Label labelName = new Label("Imię:");
	Label labelSurname = new Label("Nazwisko:");
	Label labelAddress = new Label("Adres:");
	Label labelZipCode = new Label("Kod pocztowy:");
	Label labelTown = new Label("Miasto:");
	Label labelPhone = new Label("Telefon:");
	Label labelAccount = new Label("Numer konta:");

	int userId;

	public UserEdit() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (((MyUI) UI.getCurrent()).getLogged() == false) {
			getUI().getNavigator().navigateTo(MyUI.LOGIN_USER);
		}

		this.layout = new VerticalLayout();
		setCompositionRoot(this.layout);
		this.layout.setSizeFull();
		this.layout.setMargin(true);
		this.layout.setSpacing(true);

		this.panel = new HorizontalLayout();
		this.panel.setSpacing(true);
		this.buttonMainSite.setIcon(FontAwesome.HOME);
		this.panel.addComponent(this.buttonMainSite);
		if (((MyUI) UI.getCurrent()).getLogged()) {
			this.labelLogged = new Label("Zalogowany jako:");
			this.panel.addComponent(this.labelLogged);
			this.buttonUserProfil.setCaption(((MyUI) UI.getCurrent()).getUserLogin());
			this.panel.addComponent(this.buttonUserProfil);
			this.buttonLogoutUser.setIcon(FontAwesome.LOCK);
			this.panel.addComponent(this.buttonLogoutUser);
		} else {
			this.panel.addComponent(this.labelNoLogged);
			this.buttonLoginUser.setIcon(FontAwesome.LOCK);
			this.panel.addComponent(this.buttonLoginUser);
		}
		this.layout.addComponent(this.panel);

		this.imageLogo.setSource(ImageTasslegro.getImageSource());
		this.layout.addComponent(this.imageLogo);

		this.userId = ((MyUI) UI.getCurrent()).getUserId();
		if (this.userId > 0) {
			try {
				Http_Get get = new Http_Get(this.httpGetURL + ((MyUI) UI.getCurrent()).getUserLogin());
				this.responseString = get.getStrinResponse();
				if (get.getStatusCode() == 200) {
				} else {
					this.notification = new Notification("Error!", this.responseString,
							Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					this.responseString = null;
				}
			} catch (IOException e) {
				System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				this.notification = new Notification("Error!", "Problem z połączeniem!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				this.responseString = null;
			}

		}
		if (this.responseString == null) {
		} else {
			JSONObject objects = new JSONObject(this.responseString);
			this.layout.addComponent(this.labelLogin);
			this.userLogin = new Label(objects.getString("login"));
			this.layout.addComponent(this.userLogin);
			this.layout.addComponent(this.labelPass);
			this.layout.addComponent(this.userPass);
			this.layout.addComponent(this.labelPassrep);
			this.layout.addComponent(this.userPassrep);
			this.layout.addComponent(this.labelEmail);
			this.userEmail.setValue(objects.getString("email"));
			this.layout.addComponent(this.userEmail);
			this.layout.addComponent(this.labelName);
			this.userName.setValue(objects.getString("name"));
			this.layout.addComponent(this.userName);
			this.layout.addComponent(this.labelSurname);
			this.userSurname.setValue(objects.getString("surname"));
			this.layout.addComponent(this.userSurname);
			this.layout.addComponent(this.labelAddress);
			this.userAddress.setValue(objects.getString("address"));
			this.layout.addComponent(this.userAddress);
			this.layout.addComponent(this.labelZipCode);
			this.userZipCode.setValue(objects.getString("zipCode"));
			this.layout.addComponent(this.userZipCode);
			this.layout.addComponent(this.labelTown);
			this.userTown.setValue(objects.getString("town"));
			this.layout.addComponent(this.userTown);
			this.layout.addComponent(this.labelPhone);
			if (objects.getInt("phone") == 0) {
				this.userPhone.setValue("");
			} else {
				this.userPhone.setValue(String.valueOf(objects.getInt("phone")));
			}
			this.layout.addComponent(this.userPhone);
			this.layout.addComponent(this.labelAccount);
			if (objects.getInt("account") == 0) {
				this.userAccount.setValue("");
			} else {
				this.userAccount.setValue(String.valueOf(objects.getInt("account")));
			}
			this.layout.addComponent(this.userAccount);
			this.buttonSend.addClickListener(this);
			this.buttonSend.setIcon(FontAwesome.SEND_O);
			this.layout.addComponent(this.buttonSend);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (this.userEmail.getValue().equals("")) {
			this.notification = new Notification("Error!", "Email jest wymagany!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		} else if (this.userPass.getValue().equals("") == false && this.userPassrep.getValue().equals("") == false) {
			if (!this.userPass.getValue().equals(this.userPassrep.getValue())) {
				this.notification = new Notification("Error!", "Hasła nie są takie same!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			}
		} else if (this.userPhone.isEmpty() == false) {
			if (NumberUtils.isNumber(this.userPhone.getValue()) == false
					|| NumberUtils.toInt(this.userPhone.getValue()) < 0) {
				this.notification = new Notification("Error!", "Podałeś niepoprawny telefon!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			}
		} else if (this.userAccount.isEmpty() == false) {
			if (NumberUtils.isNumber(this.userAccount.getValue()) == false
					|| NumberUtils.toInt(this.userAccount.getValue()) < 0) {
				this.notification = new Notification("Error!", "Podałeś niepoprawny numer konta!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			}
		}
		JSONObject msg = new JSONObject();
		msg.put("id", (((MyUI) UI.getCurrent()).getUserId()));
		msg.put("name", this.userName.getValue());
		msg.put("surname", this.userSurname.getValue());
		msg.put("login", this.userLogin.getValue());
		if (this.userPass.isEmpty()) {
			msg.put("pass", ((MyUI) UI.getCurrent()).getUserPass());
		} else {
			msg.put("pass", this.userPass.getValue());
		}
		msg.put("email", this.userEmail.getValue());
		msg.put("address", this.userAddress.getValue());
		msg.put("town", this.userTown.getValue());
		msg.put("zipCode", this.userZipCode.getValue());
		if (this.userPhone.isEmpty()) {
			msg.put("phone", 0);
		} else {
			msg.put("phone", this.userPhone.getValue());
		}
		if (this.userAccount.isEmpty()) {
			msg.put("account", 0);
		} else {
			msg.put("account", this.userAccount.getValue());
		}
		try {
			Http_Put put = new Http_Put(this.httpPutURL, msg.toString());
			responseString = put.getStrinResponse();
			if (put.getStatusCode() == 200) {
				if (this.userPass.isEmpty() == false) {
					((MyUI) UI.getCurrent()).setUserPass(this.userPass.getValue());
				}
				this.notification = new Notification("OK", "Pomyślnie edytowano dane użytkownika!",
						Notification.Type.WARNING_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
			} else {
				this.notification = new Notification("Error!", responseString, Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
			}
		} catch (IOException e) {
			System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
			this.notification = new Notification("Error!", "Problem z połączeniem!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
		}
	}
}
