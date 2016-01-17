package tasslegro.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.BaseInformation;
import tasslegro.base.Http_Delete;
import tasslegro.base.Http_Get;
import tasslegro.base.Http_Put;
import tasslegro.base.ImageNoImage;
import tasslegro.base.ImageTasslegro;

public class AuctionInfo extends CustomComponent implements View, Button.ClickListener {
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
	String httpDeleteURL = BaseInformation.serverURL + "auctions/";
	String httpPutURL = BaseInformation.serverURL + "auctions/offers/";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Date dateStart = null;
	Date dateEnd = null;

	String auctionId = null;
	int auctionImageId;
	String auctionTitleString = null;
	String auctionDescriptionString = null;
	double auctionPriceDouble;
	Label auctionTitle = null;
	Label auctionDescription = null;
	Label auctionPrice = null;
	Label auctionDateStart = null;
	Label auctionDateEnd = null;
	Image auctionImage = new Image();
	Button auctionEdit = new Button("Edytuj", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.AUCTION_EDIT);
		}
	});
	Button auctionDelete = new Button("Usuń aukcjię", this);
	Label auctionBindLabel = new Label("Zalicytuj:");
	TextField auctionBind = new TextField();
	Button auctionBindButton = new Button("Licytuj", this);

	public AuctionInfo() {
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
		if (this.auctionId == null) {
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
				this.auctionTitleString = objects.getString("title");
				this.auctionDescriptionString = objects.getString("description");
				this.auctionPriceDouble = objects.getDouble("price");
				this.auctionTitle = new Label("Tytuł: " + this.auctionTitleString);
				this.layout.addComponent(this.auctionTitle);
				this.auctionPrice = new Label("Cena: " + String.valueOf(this.auctionPriceDouble) + " zł");
				this.layout.addComponent(this.auctionPrice);
				if (objects.getInt("imageId") > 0) {
					this.auctionImageId = objects.getInt("imageId");
					this.auctionImage.setSource(new ExternalResource(
							BaseInformation.serverURL + "images/" + String.valueOf(this.auctionImageId)));
				} else {
					this.auctionImage.setSource(ImageNoImage.getImageSource());
					this.auctionImageId = 0;
				}
				this.auctionImage.setWidth("250px");
				this.auctionImage.setHeight("250px");
				this.layout.addComponent(this.auctionImage);
				this.auctionDescription = new Label("Opis: " + this.auctionDescriptionString);
				this.layout.addComponent(this.auctionDescription);
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
				if (((MyUI) UI.getCurrent()).getLogged()) {
					if (((MyUI) UI.getCurrent()).getUserId() == objects.getInt("userId")) {
						this.layout.addComponent(this.auctionEdit);
						this.layout.addComponent(this.auctionDelete);
					} else {
						this.layout.addComponent(this.auctionBindLabel);
						this.layout.addComponent(this.auctionBind);
						this.auctionBindButton.setIcon(FontAwesome.SEND_O);
						this.layout.addComponent(this.auctionBindButton);
					}
				}
			}
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == auctionDelete) {
			JSONObject msg = new JSONObject();
			msg.put("login", (((MyUI) UI.getCurrent()).getUserLogin()));
			msg.put("pass", (((MyUI) UI.getCurrent()).getUserPass()));
			msg.put("userId", (((MyUI) UI.getCurrent()).getUserId()));
			msg.put("aucitonId", NumberUtils.toInt(this.auctionId));
			msg.put("imageId", this.auctionImageId);
			try {
				Http_Delete delete = new Http_Delete(this.httpDeleteURL, msg.toString());
				responseString = delete.getStrinResponse();
				if (delete.getStatusCode() == 201) {
					this.notification = new Notification("OK", "Pomyślnie usunięto aukcjię!",
							Notification.Type.WARNING_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					((MyUI) UI.getCurrent()).setAuctionId(null);
					getUI().getNavigator().navigateTo(MyUI.MAIN);
				} else {
					this.notification = new Notification("Error!", responseString, Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
				}
			} catch (IOException e) {
				System.err.println("-[ERROR] " + new Date() + ": " + e.getMessage());
				this.notification = new Notification("Error!", "Problem z połączeniem!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
			}
		} else if (event.getButton() == auctionBindButton) {
			if (!NumberUtils.isNumber(this.auctionBind.getValue())) {
				this.auctionPrice.setValue(this.auctionBind.getValue().replace(",", "."));
				if (!NumberUtils.isNumber(this.auctionBind.getValue())) {
					this.notification = new Notification("Error!", "Licytowana cena jest liczbą!",
							Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					return;
				}
			} else if (NumberUtils.toDouble(this.auctionBind.getValue()) <= auctionPriceDouble) {
				this.notification = new Notification("Error!", "Licytowana cena musi być większa niż podstawowa!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
				return;
			}
			JSONObject msg = new JSONObject();
			msg.put("login", (((MyUI) UI.getCurrent()).getUserLogin()));
			msg.put("pass", (((MyUI) UI.getCurrent()).getUserPass()));
			msg.put("aucitonId", NumberUtils.toInt(this.auctionId));
			msg.put("price", NumberUtils.toDouble(this.auctionBind.getValue()));
			msg.put("bindId", (((MyUI) UI.getCurrent()).getUserId()));
			try {
				Http_Put put = new Http_Put(this.httpPutURL, msg.toString());
				responseString = put.getStrinResponse();
				if (put.getStatusCode() == 201) {
					this.notification = new Notification("OK", "Pomyślnie zalicytowano!",
							Notification.Type.WARNING_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
					((MyUI) UI.getCurrent()).setAuctionId(null);
					getUI().getNavigator().navigateTo(MyUI.MAIN);
				} else {
					this.notification = new Notification("Error!", responseString, Notification.Type.ERROR_MESSAGE);
					this.notification.setDelayMsec(5000);
					this.notification.show(Page.getCurrent());
				}
			} catch (IOException e) {
				System.err.println("-[ERROR] " + new Date() + ": " + e.getMessage());
				this.notification = new Notification("Error!", "Problem z połączeniem!",
						Notification.Type.ERROR_MESSAGE);
				this.notification.setDelayMsec(5000);
				this.notification.show(Page.getCurrent());
			}
		}
	}

}
