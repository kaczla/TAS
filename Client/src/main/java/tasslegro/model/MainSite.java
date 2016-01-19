package tasslegro.model;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.ImageTasslegro;

public class MainSite extends CustomComponent implements View {
	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout panel = new HorizontalLayout();
	Button buttonMainSite = new Button("Strona główna", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.MAIN);
		}
	});
	Button buttonAuction = new Button("Wszystkie aukcjie", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.AUCTION);
		}
	});
	Button buttonAuctionAdd = new Button("Dodaj aukcjię", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.AUCTION_ADD);
		}
	});
	Button buttonUser = new Button("Użytkownicy", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.USER);
		}
	});
	Button buttonUserAdd = new Button("Dodaj użytkownika", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.REGISTER);
		}
	});
	Button buttonAuctionInfo = new Button("Informację o aukcji", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.AUCTION_INFO);
		}
	});
	Button buttonAuctionEdit = new Button("Edytuj aukcjię", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.AUCTION_EDIT);
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

	public MainSite() {
	}

	@Override
	public void enter(ViewChangeEvent event) {
		this.layout = new VerticalLayout();
		this.layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
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
		// this.layout.setComponentAlignment(this.panel, Alignment.TOP_CENTER);

		HorizontalLayout level1 = new HorizontalLayout();
		level1.setSpacing(true);
		this.buttonAuction.setHeight("150px");
		this.buttonAuction.setWidth("200px");
		level1.addComponent(this.buttonAuction);
		this.buttonAuctionAdd.setHeight("150px");
		this.buttonAuctionAdd.setWidth("200px");
		level1.addComponent(this.buttonAuctionAdd);
		this.layout.addComponent(level1);

		HorizontalLayout level2 = new HorizontalLayout();
		level2.setSpacing(true);
		this.buttonUser.setHeight("150px");
		this.buttonUser.setWidth("200px");
		level2.addComponent(this.buttonUser);
		this.buttonUserAdd.setHeight("150px");
		this.buttonUserAdd.setWidth("200px");
		level2.addComponent(this.buttonUserAdd);
		this.layout.addComponent(level2);

		HorizontalLayout level3 = new HorizontalLayout();
		level3.setSpacing(true);
		this.buttonAuctionInfo.setHeight("150px");
		this.buttonAuctionInfo.setWidth("200px");
		level3.addComponent(this.buttonAuctionInfo);
		this.buttonAuctionEdit.setHeight("150px");
		this.buttonAuctionEdit.setWidth("200px");
		level3.addComponent(this.buttonAuctionEdit);
		this.layout.addComponent(level3);
	}
}
