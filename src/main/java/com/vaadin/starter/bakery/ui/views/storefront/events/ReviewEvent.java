package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderEditor;

/**
 * Event fired when a user initiates a review action in the order editor.
 * 
 * <p>This event is typically triggered when the user clicks a "Review" button
 * or similar action that indicates they want to review the current order
 * before finalizing or submitting it.</p>
 * 
 * <p>The event carries no additional data beyond the source component,
 * as the order details can be retrieved from the OrderEditor component.</p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public class ReviewEvent extends ComponentEvent<OrderEditor> {

	/**
	 * Creates a new ReviewEvent.
	 * 
	 * @param component the OrderEditor component that fired this event
	 */
	public ReviewEvent(OrderEditor component) {
		super(component, false);
	}
}