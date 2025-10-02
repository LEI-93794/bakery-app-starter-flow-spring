package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.VIEWPORT;

/**
 * Application shell configuration for the bakery application.
 * 
 * <p>This class serves as the entry point for configuring application-wide settings
 * including Progressive Web App (PWA) configuration, viewport settings, theming,
 * and offline capabilities.</p>
 * 
 * <p>Key configurations:
 * <ul>
 *   <li>Responsive viewport settings for mobile compatibility</li>
 *   <li>Dark theme variant for modern UI appearance</li>
 *   <li>PWA manifest configuration for app-like experience</li>
 *   <li>Offline support with custom offline page and resources</li>
 *   <li>Application branding and icon settings</li>
 * </ul>
 * </p>
 * 
 * <p>PWA features:
 * <ul>
 *   <li>Installable web application with custom app name and icon</li>
 *   <li>Offline functionality with fallback resources</li>
 *   <li>Custom start path directing to login page</li>
 *   <li>Consistent brand colors (blue theme: #227aef)</li>
 * </ul>
 * </p>
 * 
 * <p>This configuration is applied globally to all views and components
 * within the application, providing consistent theming and behavior
 * across the entire user experience.</p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 * @see AppShellConfigurator
 * @see PWA
 * @see Theme
 * @see Viewport
 */
@Viewport(VIEWPORT)
@Theme(value = "bakery", variant = "dark")
@PWA(name = "Bakery App Starter", shortName = "###Bakery###",
		startPath = "login",
		backgroundColor = "#227aef", themeColor = "#227aef",
		offlinePath = "offline-page.html",
		offlineResources = {"images/offline-login-banner.jpg"})
public class AppShell implements AppShellConfigurator {
}