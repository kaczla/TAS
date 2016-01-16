package tasslegro.model;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.Cookie;

import org.json.JSONObject;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
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
import tasslegro.base.Http_Post;
import tasslegro.base.ImageTasslegro;

public class LoginUser extends CustomComponent implements View, Button.ClickListener {
	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout panel = new HorizontalLayout();
	Button buttonMainSite = new Button("Main Site", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.MAIN);
		}
	});
	Button buttonUserAdd = new Button("Zarejestruj się", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.REGISTER);
		}
	});
	Image imageLogo = new Image();
	TextField login = new TextField();
	PasswordField pass = new PasswordField();
	Label labelLogin = new Label("Login:");
	Label labelPass = new Label("Hasło:");
	Button buttonSend = new Button("Zaloguj");

	Notification notification = null;
	String responseString = null;
	String httpPostURL = BaseInformation.serverURL + "users/ids/";
	String httpGetURL = BaseInformation.serverURL + "users/";

	public LoginUser() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (((MyUI) UI.getCurrent()).getLogged()) {
			getUI().getNavigator().navigateTo(MyUI.MAIN);
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
		this.layout.addComponent(this.panel);

		this.imageLogo.setSource(ImageTasslegro.getImageSource());
		this.layout.addComponent(this.imageLogo);

		this.layout.addComponent(this.labelLogin);
		this.login.setValidationVisible(true);
		this.layout.addComponent(this.login);
		this.layout.addComponent(this.labelPass);
		this.layout.addComponent(this.pass);
		this.buttonSend.setIcon(FontAwesome.LOCK);
		this.buttonSend.addClickListener(this);
		this.layout.addComponent(this.buttonSend);
		this.layout.addComponent(this.buttonUserAdd);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (this.login.getValue().equals("")) {
			this.notification = new Notification("Error!", "Login jest wymagany!", Notification.Type.ERROR_MESSAGE);
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

		try {
			Http_Get get = new Http_Get(this.httpGetURL + this.login.getValue());
			this.responseString = get.getStrinResponse();
			if (get.getStatusCode() == 200) {
			} else {
				this.notification = new Notification("Error!", "Błąd autoryzacji!", Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				this.responseString = null;
				return;
			}
		} catch (IOException e) {
			System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
			this.notification = new Notification("Error!", "Problem z połączeniem!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			this.responseString = null;
			return;
		}

		if (this.responseString == null) {
		} else {
			JSONObject objects = new JSONObject(this.responseString);
			JSONObject msg = new JSONObject();
			msg.put("id", objects.getInt("id"));
			msg.put("login", this.login.getValue());
			msg.put("pass", this.pass.getValue());
			try {
				Http_Post post = new Http_Post(this.httpPostURL + String.valueOf(objects.getInt("id")), msg.toString());
				this.responseString = post.getStrinResponse();
				if (post.getStatusCode() == 201 || post.getStatusCode() == 200) {
					this.notification = new Notification("OK", "Pomyślnie zalogowano!",
							Notification.Type.WARNING_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());

				} else if (post.getStatusCode() == 401) {
					this.notification = new Notification("Error!", "Błąd autoryzacji!",
							Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					return;
				} else {
					this.notification = new Notification("Error!", responseString, Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					System.out.println(post.getStatusCode());
					this.notification.show(Page.getCurrent());
					return;
				}
			} catch (IOException e) {
				System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				this.notification = new Notification("Error!", "Problem z połączeniem!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			}
			objects = new JSONObject(this.responseString);
			((MyUI) UI.getCurrent()).setLogged(true);
			((MyUI) UI.getCurrent()).setUserLogin(this.login.getValue());
			((MyUI) UI.getCurrent()).setUserPass(this.pass.getValue());
			((MyUI) UI.getCurrent()).setUserId(objects.getInt("id"));

			{
				Cookie newCookie = new Cookie("userLogin", this.login.getValue());
				newCookie.setComment("userLogin user");
				newCookie.setMaxAge(-1);
				newCookie.setPath("/");
				VaadinService.getCurrentResponse().addCookie(newCookie);
				newCookie = new Cookie("userPass", this.pass.getValue());
				newCookie.setComment("pass user");
				newCookie.setMaxAge(-1);
				newCookie.setPath("/");
				VaadinService.getCurrentResponse().addCookie(newCookie);
				newCookie = new Cookie("userId", String.valueOf(objects.getInt("id")));
				newCookie.setComment("id user");
				newCookie.setMaxAge(-1);
				newCookie.setPath("/");
				VaadinService.getCurrentResponse().addCookie(newCookie);
			}

			getUI().getNavigator().navigateTo(MyUI.MAIN);
		}
	}
}
