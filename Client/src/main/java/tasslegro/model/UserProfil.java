package tasslegro.model;

import java.io.IOException;
import java.util.Date;

import org.json.JSONObject;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.BaseInformation;
import tasslegro.base.Http_Get;
import tasslegro.base.ImageTasslegro;

public class UserProfil extends CustomComponent implements View {
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

	Label userLogin = new Label();
	Label userEmail = new Label();
	Label userName = new Label();
	Label userSurname = new Label();
	Label userAddress = new Label();
	Label userZipCode = new Label();
	Label userTown = new Label();
	Label userPhone = new Label();
	Label userAccount = new Label();
	Button userEdir = new Button("Edytuj profil", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.USER_EDIT);
		}
	});

	int userId;

	public UserProfil() {
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
		this.layout.setComponentAlignment(this.imageLogo, Alignment.TOP_CENTER);
		this.layout.setComponentAlignment(this.panel, Alignment.TOP_CENTER);

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
			if (this.responseString == null) {
			} else {
				JSONObject objects = new JSONObject(this.responseString);
				this.userLogin = new Label("Login: " + objects.getString("login"));
				this.layout.addComponent(this.userLogin);
				this.userEmail = new Label("Email: " + objects.getString("email"));
				this.layout.addComponent(this.userEmail);
				this.userName = new Label("Imię: " + objects.getString("name"));
				this.layout.addComponent(this.userName);
				this.userSurname = new Label("Nazwisko: " + objects.getString("surname"));
				this.layout.addComponent(this.userSurname);
				this.userAddress = new Label("Adres: " + objects.getString("address"));
				this.layout.addComponent(this.userAddress);
				this.userZipCode = new Label("Kod pocztowy: " + objects.getString("zipCode"));
				this.layout.addComponent(this.userZipCode);
				this.userTown = new Label("Miasto: " + objects.getString("town"));
				this.layout.addComponent(this.userTown);
				if (objects.getInt("phone") == 0) {
					this.userPhone = new Label("Telefon:");
				} else {
					this.userPhone = new Label("Telefon: " + objects.getInt("phone"));
				}
				this.layout.addComponent(this.userPhone);
				if (objects.getInt("account") == 0) {
					this.userAccount = new Label("Numer konta:");
				} else {
					this.userAccount = new Label("Numer konta: " + objects.getInt("account"));
				}
				this.layout.addComponent(this.userAccount);
				this.layout.addComponent(this.userEdir);
			}
		}
	}

}
