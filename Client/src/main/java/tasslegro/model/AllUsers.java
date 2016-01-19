package tasslegro.model;

import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.BaseInformation;
import tasslegro.base.Http_Get;
import tasslegro.base.ImageTasslegro;

public class AllUsers extends CustomComponent implements View {
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
	Table table = new Table("Użytkownicy:");

	Notification notification = null;
	String responseString = null;
	String httpGetURL = BaseInformation.serverURL + "users";

	public AllUsers() {
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

		try {
			Http_Get get = new Http_Get(this.httpGetURL);
			this.responseString = get.getStrinResponse();
			if (get.getStatusCode() == 200) {
			} else {
				this.notification = new Notification("Error!", this.responseString, Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				this.responseString = null;
			}
		} catch (IOException e) {
			System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
			this.notification = new Notification("Error!", "Problem z połączeniem!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			this.responseString = null;
		}

		this.table = new Table("Użytkownicy:");
		this.table.addContainerProperty("Nazwa", String.class, null);
		this.table.addContainerProperty("Email", String.class, null);
		this.table.addContainerProperty("Imię", String.class, null);
		this.table.addContainerProperty("Nazwisko", String.class, null);
		this.table.addContainerProperty("Adres", String.class, null);
		this.table.addContainerProperty("Kod pocztowy", String.class, null);
		this.table.addContainerProperty("Miasto", String.class, null);
		this.table.addContainerProperty("Telefon", Integer.class, null);
		this.table.addContainerProperty("Numer konta", Integer.class, null);

		if (this.responseString == null) {
		} else {
			JSONArray jsonArray = new JSONArray(responseString);
			int counter = 1;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject objects = jsonArray.optJSONObject(i);
				this.table
						.addItem(
								new Object[] { objects.getString("login"), objects.getString("email"),
										objects.getString("name"), objects.getString("surname"),
										objects.getString("address"), objects.getString("zipCode"),
										objects.getString("town"), objects.getInt("phone"), objects.getInt("account") },
								counter++);
			}
		}

		this.table.setHeight("400px");
		this.table.setWidth("95%");
		this.table.setColumnCollapsingAllowed(true);
		this.layout.addComponent(this.table);
	}
}
