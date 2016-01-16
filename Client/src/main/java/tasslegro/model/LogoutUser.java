package tasslegro.model;

import javax.servlet.http.Cookie;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import tasslegro.MyUI;
import tasslegro.base.ImageTasslegro;

public class LogoutUser extends CustomComponent implements View {
	VerticalLayout layout = new VerticalLayout();
	HorizontalLayout panel = new HorizontalLayout();
	Button buttonMainSite = new Button("Main Site", new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			getUI().getNavigator().navigateTo(MyUI.MAIN);
		}
	});
	Image imageLogo = new Image();

	public LogoutUser() {
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
		this.layout.addComponent(this.panel);

		this.imageLogo.setSource(ImageTasslegro.getImageSource());
		this.layout.addComponent(this.imageLogo);

		((MyUI) UI.getCurrent()).setLogged(false);
		((MyUI) UI.getCurrent()).setUserLogin(null);
		((MyUI) UI.getCurrent()).setUserPass(null);
		((MyUI) UI.getCurrent()).setUserId(-1);

		{
			Cookie newCookie = new Cookie("userLogin", "");
			newCookie.setComment("userLogin user");
			newCookie.setMaxAge(0);
			newCookie.setPath("/");
			VaadinService.getCurrentResponse().addCookie(newCookie);
			newCookie = new Cookie("userPass", "");
			newCookie.setComment("pass user");
			newCookie.setMaxAge(0);
			newCookie.setPath("/");
			VaadinService.getCurrentResponse().addCookie(newCookie);
			newCookie = new Cookie("userId", "");
			newCookie.setComment("id user");
			newCookie.setMaxAge(0);
			newCookie.setPath("/");
			VaadinService.getCurrentResponse().addCookie(newCookie);
		}

		getUI().getNavigator().navigateTo(MyUI.MAIN);
	}

}
