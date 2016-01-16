package tasslegro.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.BaseInformation;
import tasslegro.base.Http_Get;
import tasslegro.base.ImageNoImage;
import tasslegro.base.ImageTasslegro;

public class AuctionInfo extends CustomComponent implements View {
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

	Notification notification = null;
	String responseString = null;
	String httpGetURL = BaseInformation.serverURL + "auctions/";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Date dateStart = null;
	Date dateEnd = null;

	String idAuction = null;
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

		this.imageLogo.setSource(ImageTasslegro.getImageSource());
		this.layout.addComponent(this.imageLogo);

		this.idAuction = ((MyUI) UI.getCurrent()).getIdAuction();
		if (this.idAuction == null) {
			this.layout.addComponent(new Label("Nie wybrano aukcji!"));
		} else {
			try {
				Http_Get get = new Http_Get(this.httpGetURL + this.idAuction);
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
				this.auctionTitle = new Label("Tytuł: " + objects.getString("title"));
				this.layout.addComponent(this.auctionTitle);
				this.auctionPrice = new Label("Cena: " + String.valueOf(objects.getDouble("price")) + " zł");
				this.layout.addComponent(this.auctionPrice);
				if (objects.getInt("imageId") > 0) {
					this.auctionImage.setSource(new ExternalResource(
							BaseInformation.serverURL + "images/" + String.valueOf(objects.getInt("imageId"))));
				} else {
					this.auctionImage.setSource(ImageNoImage.getImageSource());
				}
				this.auctionImage.setWidth("250px");
				this.auctionImage.setHeight("250px");
				this.layout.addComponent(this.auctionImage);
				this.auctionDescription = new Label("Opis: " + objects.getString("description"));
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
					}
				}
			}
		}

	}

}
