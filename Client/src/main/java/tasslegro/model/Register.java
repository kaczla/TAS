package tasslegro.model;

import java.io.IOException;
import java.util.Date;

import org.json.simple.JSONObject;

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
import tasslegro.base.Http_Post;
import tasslegro.base.ImageTasslegro;

public class Register extends CustomComponent implements View, Button.ClickListener {
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
	Label labelNoLogged = new Label("Nie zalogowany!");
	Label labelLogged = new Label();
	Image imageLogo = new Image();
	TextField name = new TextField();
	TextField surname = new TextField();
	TextField email = new TextField();
	TextField phone = new TextField();
	TextField login = new TextField();
	PasswordField pass = new PasswordField();
	PasswordField passrep = new PasswordField();
	TextField account = new TextField();
	TextField address = new TextField();
	TextField town = new TextField();
	TextField zipCode = new TextField();
	Button register = new Button("ZAREJESTRUJ");

	Label labelName = new Label("Imie:");
	Label labelSurname = new Label("Nazwisko:");
	Label labelEmail = new Label("Email:*");
	Label labelPhone = new Label("Telefon");
	Label labelLogin = new Label("Login:*");
	Label labelPass = new Label("Hasło:*");
	Label labelPassrep = new Label("Powtórz Haslo:*");
	Label labelAccount = new Label("Numer Konta:");
	Label labelAddress = new Label("Adres:");
	Label labelTown = new Label("Miasto:");
	Label labelZipCode = new Label("Kod Pocztowy:");
	Label require = new Label("*wymagane");

	Notification notification = null;
	String responseString = null;
	String httpPostURL = BaseInformation.serverURL + "users";

	public Register() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
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
			this.labelLogged = new Label("Zalogowany jako: " + ((MyUI) UI.getCurrent()).getUserLogin());
			this.panel.addComponent(this.labelLogged);
			this.buttonLogoutUser.setIcon(FontAwesome.LOCK);
			this.panel.addComponent(this.buttonLogoutUser);
		} else {
			this.panel.addComponent(this.labelNoLogged);
			this.buttonLoginUser.setIcon(FontAwesome.LOCK);
			this.panel.addComponent(this.buttonLoginUser);
		}
		this.layout.addComponent(this.panel);

		if (((MyUI) UI.getCurrent()).getLogged() == true) {
			this.layout.addComponent(new Label("Jesteś już zalogowany!"));
		} else {
			this.imageLogo.setSource(ImageTasslegro.getImageSource());
			this.layout.addComponent(this.imageLogo);
			this.layout.addComponent(this.labelName);
			this.layout.addComponent(this.name);
			this.layout.addComponent(this.labelSurname);
			this.layout.addComponent(this.surname);
			this.layout.addComponent(this.labelEmail);
			this.layout.addComponent(this.email);
			this.layout.addComponent(this.labelPhone);
			this.layout.addComponent(this.phone);
			this.layout.addComponent(this.labelLogin);
			this.login.setValidationVisible(true);
			this.layout.addComponent(this.login);
			this.layout.addComponent(this.labelPass);
			this.layout.addComponent(this.pass);
			this.layout.addComponent(this.labelPassrep);
			this.layout.addComponent(this.passrep);
			this.layout.addComponent(this.labelAddress);
			this.layout.addComponent(this.address);
			this.layout.addComponent(this.labelTown);
			this.layout.addComponent(this.town);
			this.layout.addComponent(this.labelZipCode);
			this.layout.addComponent(this.zipCode);
			this.layout.addComponent(require);
			this.register.setIcon(FontAwesome.SEND);
			this.register.addClickListener(this);
			this.layout.addComponent(this.register);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (this.login.getValue().equals("")) {
			this.notification = new Notification("Error!", "Login jest wymagany!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		}
		if (this.email.getValue().equals("")) {
			this.notification = new Notification("Error!", "Email jest wymagany!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		}
		if (this.pass.getValue().equals("")) {
			this.notification = new Notification("Error!", "Hasło jest wymagane!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		}
		if (this.passrep.getValue().equals("")) {
			this.notification = new Notification("Error!", "Powtórz Hasło jest wymagane!",
					Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		}
		if (!this.pass.getValue().equals(this.passrep.getValue())) {
			this.notification = new Notification("Error!", "Hasła nie są takie same!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		}
		JSONObject msg = new JSONObject();
		msg.put("name", name.getValue());
		msg.put("surname", surname.getValue());
		msg.put("login", login.getValue());
		msg.put("pass", pass.getValue());
		msg.put("email", email.getValue());
		msg.put("address", address.getValue());
		msg.put("town", town.getValue());
		msg.put("zipCode", zipCode.getValue());
		msg.put("phone", phone.getValue());
		msg.put("account", account.getValue());
		try {
			Http_Post post = new Http_Post(this.httpPostURL, msg.toString());
			if (post.getStatusCode() == 201) {
				this.notification = new Notification("OK", "Pomyślnie dodano użytkownika!",
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
