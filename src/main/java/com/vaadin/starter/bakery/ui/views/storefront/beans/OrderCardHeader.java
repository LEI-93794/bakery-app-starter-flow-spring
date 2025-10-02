package com.vaadin.starter.bakery.ui.views.storefront.beans;

/**
 * Data transfer object representing header information for order card groupings.
 * 
 * <p>This class encapsulates the display data needed for order card headers
 * that provide visual separation and grouping in the storefront view.
 * The header typically contains primary and secondary text elements.</p>
 * 
 * <p>Common usage patterns:
 * <ul>
 *   <li>Date-based grouping (e.g., "Today", "This Week", "March 2025")</li>
 *   <li>Status-based grouping (e.g., "New Orders", "In Progress")</li>
 *   <li>Customer-based grouping for administrative views</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public class OrderCardHeader {

	/**
	 * Primary header text (typically larger/bold display).
	 */
	private String main;
	
	/**
	 * Secondary header text (typically smaller/subtitle display).
	 */
	private String secondary;

	/**
	 * Constructs a new OrderCardHeader with the specified text elements.
	 * 
	 * @param main the primary header text
	 * @param secondary the secondary header text
	 */
	public OrderCardHeader(String main, String secondary) {
		this.main = main;
		this.secondary = secondary;
	}

	/**
	 * Returns the primary header text.
	 * 
	 * @return the main header text
	 */
	public String getMain() {
		return main;
	}

	/**
	 * Sets the primary header text.
	 * 
	 * @param main the main header text to set
	 */
	public void setMain(String main) {
		this.main = main;
	}

	/**
	 * Returns the secondary header text.
	 * 
	 * @return the secondary header text
	 */
	public String getSecondary() {
		return secondary;
	}

	/**
	 * Sets the secondary header text.
	 * 
	 * @param secondary the secondary header text to set
	 */
	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}
}
