package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;

/**
 * Helper class for creating LitRenderer-based order card components for display in grids.
 * 
 * <p>This class optimizes rendering performance by using LitRenderer instead of ComponentRenderer,
 * which reduces CPU and memory consumption when displaying large lists of orders.</p>
 * 
 * <p>The order card supports:
 * <ul>
 *   <li>Conditional header display for visual grouping</li>
 *   <li>Time-sensitive formatting (recent, this week, older)</li>
 *   <li>Responsive content based on order due date</li>
 *   <li>Click event handling for order selection</li>
 * </ul>
 * </p>
 * 
 * <p>Visual grouping: Order cards can include an optional header above the card content.
 * This is used to visually separate orders into logical groups (e.g., by date, status).
 * While all order cards are functionally equivalent, those with visible headers create
 * visual separation in the UI.</p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public class OrderCard {

	/**
	 * Creates and returns a LitRenderer template for order cards.
	 * The template includes header and card content binding with click event support.
	 * 
	 * @return a configured LitRenderer for Order entities
	 */
	public static LitRenderer<Order> getTemplate() {
		return LitRenderer.of(
				  "<order-card"
				+ "  .header='${item.header}'"
				+ "  .orderCard='${item.orderCard}'"
				+ "  @card-click='${cardClick}'>"
				+ "</order-card>");
	}
	
	/**
	 * Factory method for creating OrderCard instances from OrderSummary objects.
	 * 
	 * @param order the order summary to create a card for
	 * @return a new OrderCard instance
	 */
	public static OrderCard create(OrderSummary order) {
		return new OrderCard(order);
	}

	/**
	 * Flag indicating if the order is recent (today or yesterday).
	 */
	private boolean recent;
	
	/**
	 * Flag indicating if the order is due within the current week.
	 */
	private boolean inWeek;

	/**
	 * The order summary data for this card.
	 */
	private final OrderSummary order;
	
	/**
	 * Constructs an OrderCard for the given order summary.
	 * Automatically calculates time-based flags (recent, inWeek) based on the order's due date.
	 * 
	 * @param order the order summary to display in this card
	 */
	public OrderCard(OrderSummary order) {
		this.order = order;
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		recent = date.equals(now) || date.equals(now.minusDays(1));
		inWeek = !recent && now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD);
	}

	/**
	 * Returns the pickup location name for display if the order is recent or within the current week.
	 * 
	 * @return the pickup location name, or null if the order is not recent/current week
	 */
	public String getPlace() {
		return recent || inWeek ? order.getPickupLocation().getName() : null;
	}

	/**
	 * Returns the formatted due time for recent orders.
	 * 
	 * @return the formatted time string for recent orders, or null for older orders
	 */
	public String getTime() {
		return recent ? HOUR_FORMATTER.format(order.getDueTime()) : null;
	}

	/**
	 * Returns the short day format for orders within the current week.
	 * 
	 * @return the short day string for current week orders, or null for other orders
	 */
	public String getShortDay() {
		return inWeek ? SHORT_DAY_FORMATTER.format(order.getDueDate()) : null;
	}

	/**
	 * Returns the formatted due time for orders within the current week.
	 * 
	 * @return the formatted time string for current week orders, or null for other orders
	 */
	public String getSecondaryTime() {
		return inWeek ? HOUR_FORMATTER.format(order.getDueTime()) : null;
	}

	/**
	 * Returns the month and day format for older orders (not recent or current week).
	 * 
	 * @return the formatted month and day string for older orders, or null for recent/current week orders
	 */
	public String getMonth() {
		return recent || inWeek ? null : MONTH_AND_DAY_FORMATTER.format(order.getDueDate());
	}

	/**
	 * Returns the full day name for older orders (not recent or current week).
	 * 
	 * @return the full weekday name for older orders, or null for recent/current week orders
	 */
	public String getFullDay() {
		return recent || inWeek ? null : WEEKDAY_FULLNAME_FORMATTER.format(order.getDueDate());
	}

	/**
	 * Returns the string representation of the order state.
	 * 
	 * @return the order state as a string
	 */
	public String getState() {
		return order.getState().toString();
	}

	/**
	 * Returns the full name of the customer who placed the order.
	 * 
	 * @return the customer's full name
	 */
	public String getFullName() {
		return order.getCustomer().getFullName();
	}

	/**
	 * Returns the list of items in this order.
	 * 
	 * @return the order items list
	 */
	public List<OrderItem> getItems() {
		return order.getItems();
	}
}
