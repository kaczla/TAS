package tasslegro.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
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
import tasslegro.base.Http_Get;
import tasslegro.base.Http_Put;
import tasslegro.base.ImageTasslegro;
import tasslegro.base.ImageUploader;

public class AuctionEdit extends CustomComponent implements View, Button.ClickListener {
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
	String httpGetURL = BaseInformation.serverURL + "auctions/";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Date dateStart = null;
	Date dateEnd = null;

	String auctionId = null;
	Label labelTitle = new Label("Tytuł:");
	Label labelDescription = new Label("Opis:");
	Label Price = new Label("Cena:");
	TextField auctionTitle = new TextField();
	TextArea auctionDescription = new TextArea();
	TextField auctionPrice = new TextField();
	ImageUploader receiverImage = new ImageUploader();
	Upload uploadImage = new Upload(null, this.receiverImage);
	Image auctionImage = new Image();
	Label auctionDateStart = null;
	Label auctionDateEnd = null;
	Button buttonSend = new Button("Aktualizuj");

	public AuctionEdit() {
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

		this.auctionId = ((MyUI) UI.getCurrent()).getAuctionId();
		if (((MyUI) UI.getCurrent()).getLogged() == false) {
			this.layout.addComponent(new Label("Zaloguj się!"));
		} else if (this.auctionId == null) {
			this.layout.addComponent(new Label("Nie wybrano aukcji!"));
		} else {
			try {
				Http_Get get = new Http_Get(this.httpGetURL + this.auctionId);
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
				if (objects.getInt("userId") != (((MyUI) UI.getCurrent()).getUserId())) {
					this.layout.addComponent(new Label("Wybierz aukcjię do edycji!"));
					return;
				}
				this.layout.addComponent(this.labelTitle);
				this.auctionTitle.setValue(objects.getString("title"));
				this.layout.addComponent(this.auctionTitle);
				this.layout.addComponent(this.labelDescription);
				this.auctionDescription.setValue(objects.getString("description"));
				this.layout.addComponent(this.auctionDescription);
				this.layout.addComponent(this.Price);
				this.auctionPrice.setValue(String.valueOf(objects.getDouble("price")));
				this.layout.addComponent(this.auctionPrice);
				if (objects.getInt("imageId") > 0) {
					this.auctionImage.setSource(new ExternalResource(
							BaseInformation.serverURL + "images/" + String.valueOf(objects.getInt("imageId"))));
					this.auctionImage.setWidth("250px");
					this.auctionImage.setHeight("250px");
					this.layout.addComponent(this.auctionImage);
					this.receiverImage.setImageId(objects.getInt("imageId"));
					this.uploadImage.setButtonCaption("Edytuj zdjęcie");
				} else {
					this.uploadImage.setButtonCaption("Dodaj zdjęcie");
				}
				this.uploadImage.setImmediate(true);
				this.layout.addComponent(this.uploadImage);
				try {
					this.dateStart = DateUtils.parseDateStrictly(objects.getString("startDate"),
							new String[] { "yyyy-MM-dd HH:mm:ss.S" });
				} catch (JSONException e) {
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				} catch (ParseException e) {
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				}
				this.auctionDateStart = new Label("Wystawiono: " + this.dateFormat.format(this.dateStart));
				this.layout.addComponent(this.auctionDateStart);
				try {
					this.dateEnd = DateUtils.parseDateStrictly(objects.getString("endDate"),
							new String[] { "yyyy-MM-dd HH:mm:ss.S" });
				} catch (JSONException e) {
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				} catch (ParseException e) {
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				}
				this.auctionDateEnd = new Label("Koniec: " + this.dateFormat.format(this.dateEnd));
				this.layout.addComponent(this.auctionDateEnd);
				this.buttonSend.addClickListener(this);
				this.buttonSend.setIcon(FontAwesome.SEND_O);
				this.layout.addComponent(this.buttonSend);
			}
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
			HttpResponse response = this.receiverImage.updateImage();
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
		msg.put("login", (((MyUI) UI.getCurrent()).getUserLogin()));
		msg.put("pass", (((MyUI) UI.getCurrent()).getUserPass()));
		msg.put("title", auctionTitle.getValue());
		msg.put("description", auctionDescription.getValue());
		msg.put("price", auctionPrice.getValue());
		msg.put("userId", (((MyUI) UI.getCurrent()).getUserId()));
		msg.put("aucitonId", this.auctionId);
		msg.put("imageId", imageId);
		try {
			Http_Put put = new Http_Put(this.httpGetURL, msg.toString());
			responseString = put.getStrinResponse();
			if (put.getStatusCode() == 201) {
				this.notification = new Notification("OK", "Pomyślnie zaktualizowano aukcjię!",
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
