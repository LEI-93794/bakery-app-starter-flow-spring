package com.vaadin.starter.bakery.ui.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import static com.vaadin.flow.i18n.I18NProvider.translate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

/**
 * Login view component for user authentication in the bakery application.
 * 
 * <p>This view provides the user authentication interface using Vaadin's {@link LoginOverlay}
 * component. It handles user login, automatic redirection for authenticated users,
 * and displays authentication errors.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Internationalized login form with customizable text</li>
 *   <li>Automatic redirection for already authenticated users</li>
 *   <li>Error handling and display for failed authentication attempts</li>
 *   <li>Integration with Spring Security authentication</li>
 *   <li>Demo credentials display for development/testing</li>
 * </ul>
 * </p>
 * 
 * <p>The view displays demo credentials for testing:
 * <ul>
 *   <li>admin@vaadin.com / admin - Administrator access</li>
 *   <li>barista@vaadin.com / barista - Barista access</li>
 * </ul>
 * </p>
 * 
 * <p>Security behavior:
 * <ul>
 *   <li>If user is already authenticated, redirects to {@link StorefrontView}</li>
 *   <li>Shows login form overlay for unauthenticated users</li>
 *   <li>Displays error message if authentication fails</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@Route
@PageTitle("###Bakery###")
public class LoginView extends LoginOverlay
	implements AfterNavigationObserver, BeforeEnterObserver {

	/**
	 * Constructs the login view with internationalized content and demo credentials.
	 * Sets up the login form with translated labels and demo account information.
	 */
	public LoginView() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle(translate("app.title"));
		i18n.getHeader().setDescription(
			"admin@vaadin.com + admin\n" + "barista@vaadin.com + barista");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit(translate("signin"));
		i18n.getForm().setTitle(translate("login"));
		i18n.getForm().setUsername(translate("email"));
		i18n.getForm().setPassword(translate("password"));
		setI18n(i18n);
		setForgotPasswordButtonVisible(false);
		setAction("login");
	}
	
	/**
	 * Navigation guard that handles authentication state before entering the view.
	 * 
	 * <p>This method is called before the user navigates to the login view.
	 * If the user is already authenticated, they are automatically redirected
	 * to the main storefront. Otherwise, the login overlay is displayed.</p>
	 * 
	 * @param event the navigation event containing route and state information
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(StorefrontView.class);
		} else {
			setOpened(true);
		}
	}

	/**
	 * Post-navigation handler that displays authentication errors if present.
	 * 
	 * <p>This method is called after navigation to the login view is complete.
	 * It checks for an "error" query parameter in the URL and displays the
	 * login error state if authentication failed.</p>
	 * 
	 * @param event the navigation event containing location and query parameters
	 */
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		setError(
			event.getLocation().getQueryParameters().getParameters().containsKey(
				"error"));
	}

}
