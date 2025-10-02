package com.vaadin.starter.bakery.ui.utils;

import java.util.Locale;

import org.springframework.data.domain.Sort;

/**
 * Constants and configuration values for the Bakery application UI.
 * 
 * <p>This utility class centralizes all UI-related constants including:
 * <ul>
 *   <li>Application locale and internationalization settings</li>
 *   <li>URL routing paths and templates</li>
 *   <li>Page titles for different views</li>
 *   <li>Sorting and data display configurations</li>
 *   <li>UI behavior parameters (notification duration, viewport settings)</li>
 * </ul>
 * </p>
 * 
 * <p>All constants are static and final, except where explicitly noted
 * as mutable for testing purposes.</p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public class BakeryConst {

	/**
	 * Default application locale (US English).
	 */
	public static final Locale APP_LOCALE = Locale.US;

	/**
	 * URL parameter name for order ID.
	 */
	public static final String ORDER_ID = "orderID";
	
	/**
	 * URL segment for edit mode.
	 */
	public static final String EDIT_SEGMENT = "edit";

	/**
	 * Root page path (empty string for home page).
	 */
	public static final String PAGE_ROOT = "";
	
	/**
	 * Storefront page base path.
	 */
	public static final String PAGE_STOREFRONT = "storefront";
	
	/**
	 * Storefront order template path with optional order ID parameter.
	 */
	public static final String PAGE_STOREFRONT_ORDER_TEMPLATE =
			PAGE_STOREFRONT + "/:" + ORDER_ID + "?";
			
	/**
	 * Storefront order edit template path with required order ID parameter.
	 */
	public static final String PAGE_STOREFRONT_ORDER_EDIT_TEMPLATE =
			PAGE_STOREFRONT + "/:" + ORDER_ID + "/" + EDIT_SEGMENT;
			
	/**
	 * Formatted storefront order edit path template.
	 */
	public static final String PAGE_STOREFRONT_ORDER_EDIT =
			"storefront/%d/edit";
			
	/**
	 * Dashboard page path.
	 */
	public static final String PAGE_DASHBOARD = "dashboard";
	
	/**
	 * Users management page path.
	 */
	public static final String PAGE_USERS = "users";
	
	/**
	 * Products management page path.
	 */
	public static final String PAGE_PRODUCTS = "products";

	/**
	 * Page title for the storefront view.
	 */
	public static final String TITLE_STOREFRONT = "Storefront";
	
	/**
	 * Page title for the dashboard view.
	 */
	public static final String TITLE_DASHBOARD = "Dashboard";
	
	/**
	 * Page title for the users management view.
	 */
	public static final String TITLE_USERS = "Users";
	
	/**
	 * Page title for the products management view.
	 */
	public static final String TITLE_PRODUCTS = "Products";
	
	/**
	 * Title for logout action.
	 */
	public static final String TITLE_LOGOUT = "Logout";
	
	/**
	 * Page title for 404 not found pages.
	 */
	public static final String TITLE_NOT_FOUND = "Page was not found";

	/**
	 * Fields available for sorting orders in the grid.
	 */
	public static final String[] ORDER_SORT_FIELDS = {"dueDate", "dueTime", "id"};
	
	/**
	 * Default sort direction for order listings.
	 */
	public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

	/**
	 * Viewport meta tag configuration for responsive design.
	 */
	public static final String VIEWPORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover";

	/**
	 * Duration (in milliseconds) for notification display.
	 * Mutable for testing purposes.
	 */
	public static int NOTIFICATION_DURATION = 4000;

}
