package tasslegro.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
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
import tasslegro.base.ImageNoImage;
import tasslegro.base.ImageTasslegro;

public class AllAuctions extends CustomComponent implements View {
	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout panel = new HorizontalLayout();
	HorizontalLayout panelPage = new HorizontalLayout();
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
	Notification notification = null;
	String responseString = null;
	Table table = new Table("Aukcje:");
	Image imageLogo = new Image();
	String httpGetURL = BaseInformation.serverURL + "auctions/pages/";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Date date = null;
	Button buttoncurrentPage = new Button("0", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			auctionPage = 1;
			((MyUI) UI.getCurrent()).setAuctionPage(auctionPage);
			getUI().getNavigator().navigateTo(MyUI.AUCTION);
		}
	});
	Button buttonPreviousPage = new Button("Poprzednia strona", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			--auctionPage;
			if (auctionPage < 1) {
				auctionPage = 1;
			}
			((MyUI) UI.getCurrent()).setAuctionPage(auctionPage);
			getUI().getNavigator().navigateTo(MyUI.AUCTION);
		}
	});
	Button buttonNextPage = new Button("Następna strona", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			auctionPage++;
			((MyUI) UI.getCurrent()).setAuctionPage(auctionPage);
			getUI().getNavigator().navigateTo(MyUI.AUCTION);
		}
	});
	Button buttonSearch = new Button("Szukaj", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.AUCTION_SEARCH);
		}
	});

	String tmpString = null;
	int auctionPage = 1;

	public AllAuctions() {
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

		this.auctionPage = ((MyUI) UI.getCurrent()).getAuctionPage();
		if (this.auctionPage < 1) {
			this.auctionPage = 1;
			((MyUI) UI.getCurrent()).setAuctionPage(this.auctionPage);
		}

		try {
			Http_Get get = new Http_Get(this.httpGetURL + this.auctionPage);
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

		this.table = new Table("Aukcje:");
		this.table.addContainerProperty("Grafika", Image.class, null);
		this.table.addContainerProperty("Tytuł", String.class, null);
		this.table.addContainerProperty("Opis", String.class, null);
		this.table.addContainerProperty("Cena (zł)", Double.class, null);
		this.table.addContainerProperty("Koniec o", String.class, null);
		this.table.addContainerProperty("Więcej", Button.class, null);

		if (this.responseString == null) {
		} else {
			JSONArray jsonArray = new JSONArray(responseString);
			int counter = 1;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject objects = jsonArray.optJSONObject(i);
				Image tmpImage = new Image();
				if (objects.getInt("imageId") > 0) {
					tmpImage.setSource(new ExternalResource(
							BaseInformation.serverURL + "images/" + String.valueOf(objects.getInt("imageId"))));
				} else {
					tmpImage.setSource(ImageNoImage.getImageSource());
				}
				tmpImage.setWidth("100px");
				tmpImage.setHeight("100px");
				try {
					this.date = DateUtils.parseDateStrictly(objects.getString("endDate"),
							new String[] { "yyyy-MM-dd HH:mm:ss.S" });
				} catch (JSONException e) {
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				} catch (ParseException e) {
					System.err.println("[ERROR] " + new Date() + ": " + e.getMessage());
				}

				tmpString = String.valueOf(objects.getInt("aucitonId"));
				Button tmpButton = new Button("Więcej", new Button.ClickListener() {
					String id = tmpString;

					@Override
					public void buttonClick(ClickEvent event) {
						((MyUI) UI.getCurrent()).setAuctionId(id);
						getUI().getNavigator().navigateTo(MyUI.AUCTION_INFO);
					}
				});
				tmpButton.setDescription("Kliknij po więcej!");
				String auctionTitleTmp = objects.getString("title");
				if (auctionTitleTmp.length() > 20) {
					auctionTitleTmp = auctionTitleTmp.substring(0, 20) + "...";
				}
				String auctionDesctiprionTmp = objects.getString("description");
				if (auctionDesctiprionTmp.length() > 20) {
					auctionDesctiprionTmp = auctionDesctiprionTmp.substring(0, 20) + "...";
				}

				this.table
						.addItem(
								new Object[] { tmpImage, auctionTitleTmp, auctionDesctiprionTmp,
										objects.getDouble("price"), this.dateFormat.format(this.date), tmpButton },
								counter++);
			}
		}

		this.panelPage = new HorizontalLayout();
		this.panelPage.setSpacing(true);
		this.panelPage.addComponent(this.buttonPreviousPage);
		this.buttoncurrentPage.setCaption(String.valueOf(this.auctionPage));
		this.panelPage.addComponent(this.buttoncurrentPage);
		this.panelPage.addComponent(this.buttonNextPage);
		this.panelPage.addComponent(this.buttonSearch);
		this.layout.addComponent(this.panelPage);
		this.table.setHeight("100%");
		this.table.setWidth("95%");
		this.table.setColumnCollapsingAllowed(true);
		this.layout.addComponent(this.table);
		if (this.table.size() < 1) {
			this.notification = new Notification("Error!", "Brak danych!\nWróć do poprzedniej strony!",
					Notification.Type.ERROR_MESSAGE);
			this.notification.setDelayMsec(5000);
			this.notification.show(Page.getCurrent());
			this.responseString = null;
		}
	}
}
