package tasslegro.model;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.BaseInformation;
import tasslegro.base.Http_Post;
import tasslegro.base.ImageTasslegro;
import tasslegro.base.ImageUploader;

public class AddAuction extends CustomComponent implements View, Button.ClickListener {
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
	TextField auctionTitle = new TextField();
	TextArea auctionDescription = new TextArea();
	TextField auctionPrice = new TextField();
	ImageUploader receiverImage = new ImageUploader();
	Upload uploadImage = new Upload(null, this.receiverImage);
	Button auctionButtonAdd = new Button("Wyślij", this);

	Label labelTitle = new Label("Tytuł:");
	Label labelDescription = new Label("Opis:");
	Label labelPrice = new Label("Cena:");
	Label labelImage = new Label("Zdjęcie:");

	int userId = 1;

	Notification notification = null;
	String responseString = null;
	String httpPostURL = BaseInformation.serverURL + "auctions";

	public AddAuction() {
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
		this.imageLogo.setSource(ImageTasslegro.getImageSource());
		this.layout.addComponent(this.imageLogo);
		this.layout.addComponent(this.panel);
		this.layout.setComponentAlignment(this.imageLogo, Alignment.TOP_CENTER);
		this.layout.setComponentAlignment(this.panel, Alignment.TOP_CENTER);

		if (((MyUI) UI.getCurrent()).getLogged() == false) {
			this.layout.addComponent(new Label("Zaloguj się!"));
		} else {
			this.layout.addComponent(this.labelTitle);
			this.layout.addComponent(this.auctionTitle);
			this.layout.addComponent(this.labelDescription);
			this.layout.addComponent(this.auctionDescription);
			this.layout.addComponent(this.labelPrice);
			this.layout.addComponent(this.auctionPrice);
			this.layout.addComponent(this.labelImage);
			this.uploadImage.setButtonCaption("Dodaj");
			this.uploadImage.setImmediate(true);
			this.layout.addComponent(this.uploadImage);
			this.auctionButtonAdd.setIcon(FontAwesome.SEND);
			this.layout.addComponent(this.auctionButtonAdd);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		int imageId = 0;
		if (this.auctionTitle.getValue().equals("")) {
			this.notification = new Notification("Error!", "Tytuł jest wymagany!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		} else if (this.auctionDescription.getValue().equals("")) {
			this.notification = new Notification("Error!", "Opis jest wymagany!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		} else if (this.auctionPrice.getValue().equals("")) {
			this.notification = new Notification("Error!", "Cena jest wymagany!", Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		} else if (!NumberUtils.isNumber(this.auctionPrice.getValue())) {
			this.auctionPrice.setValue(this.auctionPrice.getValue().replace(",", "."));
			if (!NumberUtils.isNumber(this.auctionPrice.getValue())) {
				this.notification = new Notification("Error!", "Cena jest liczbą!", Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			}
		} else if (NumberUtils.toFloat(this.auctionPrice.getValue()) <= 0.0) {
			this.notification = new Notification("Error!", "Cena musi być większa niż 0!",
					Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			return;
		} else if (this.receiverImage.returnInputStream() != null) {
			HttpResponse response = this.receiverImage.uploadImage();
			if (response == null) {
				this.notification = new Notification("Error!", "Problem z połączeniem!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			} else {
				HttpEntity entity = response.getEntity();
				String responseString = null;
				try {
					responseString = EntityUtils.toString(entity, "UTF-8");
				} catch (IOException e) {
					this.notification = new Notification("Error!", "Problem z połączeniem!",
							Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
					return;
				}
				if (response.getStatusLine().getStatusCode() == 201) {
					JSONObject msg = new JSONObject(responseString);
					imageId = msg.getInt("id");
				} else {
					this.notification = new Notification("Error!", responseString, Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					return;
				}
			}
		}
		JSONObject msg = new JSONObject();
		msg.put("title", auctionTitle.getValue());
		msg.put("description", auctionDescription.getValue());
		msg.put("price", auctionPrice.getValue());
		msg.put("userId", userId);
		msg.put("bindId", userId);
		msg.put("imageId", imageId);
		try {
			Http_Post post = new Http_Post(this.httpPostURL, msg.toString());
			responseString = post.getStrinResponse();
			if (post.getStatusCode() == 201) {
				this.notification = new Notification("OK", "Pomyślnie dodano aukcjię!",
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
