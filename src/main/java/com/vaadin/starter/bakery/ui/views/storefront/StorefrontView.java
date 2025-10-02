package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.views.EntityView;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderDetails;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderEditor;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.EDIT_SEGMENT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ORDER_ID;

/**
 * Main storefront view component for the bakery application.
 * 
 * <p>This view serves as the primary interface for managing orders and provides:
 * <ul>
 *   <li>Order listing with search and filtering capabilities</li>
 *   <li>Order creation, editing, and deletion functionality</li>
 *   <li>Order details viewing and review</li>
 *   <li>Navigation to order-specific pages</li>
 * </ul>
 * </p>
 * 
 * <p>The view is implemented as a LitTemplate component with a corresponding
 * JavaScript template file for enhanced performance and user experience.</p>
 * 
 * <p>This view serves multiple routes:
 * <ul>
 *   <li>Root path ("/") - Main storefront view</li>
 *   <li>Order listing with templates</li>
 *   <li>Order editing with parameter templates</li>
 * </ul>
 * </p>
 * 
 * <p>Security: This view is accessible to all authenticated users (@PermitAll).</p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@Tag("storefront-view")
@JsModule("./src/views/storefront/storefront-view.js")
@Route(value = BakeryConst.PAGE_STOREFRONT_ORDER_TEMPLATE, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_STOREFRONT_ORDER_EDIT_TEMPLATE, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_ROOT, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
@PermitAll
public class StorefrontView extends LitTemplate
		implements HasLogger, BeforeEnterObserver, EntityView<Order> {

	/**
	 * Search bar component for filtering orders.
	 * Mapped to the "search" element in the template.
	 */
	@Id("search")
	private SearchBar searchBar;

	/**
	 * Grid component for displaying the list of orders.
	 * Mapped to the "grid" element in the template.
	 */
	@Id("grid")
	private Grid<Order> grid;

	/**
	 * Dialog component for displaying order details and editing forms.
	 * Mapped to the "dialog" element in the template.
	 */
	@Id("dialog")
	private Dialog dialog;

	/**
	 * Confirmation dialog for user confirmations (e.g., order deletion).
	 */
	private ConfirmDialog confirmation;

	/**
	 * Order editor component for creating and editing orders.
	 */
	private final OrderEditor orderEditor;

	/**
	 * Order details component for displaying order information in read-only mode.
	 */
	private final OrderDetails orderDetails = new OrderDetails();

	/**
	 * Presenter that handles the business logic and data binding for this view.
	 */
	private final OrderPresenter presenter;

	/**
	 * Constructs a new StorefrontView with the required dependencies.
	 * 
	 * @param presenter the order presenter for handling business logic
	 * @param orderEditor the order editor component for order management
	 */
	@Autowired
	public StorefrontView(OrderPresenter presenter, OrderEditor orderEditor) {
		this.presenter = presenter;
		this.orderEditor = orderEditor;

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		grid.setSelectionMode(Grid.SelectionMode.NONE);

		grid.addColumn(OrderCard.getTemplate()
				.withProperty("orderCard", OrderCard::create)
				.withProperty("header", order -> presenter.getHeaderByOrderId(order.getId()))
				.withFunction("cardClick",
						order -> UI.getCurrent().navigate(BakeryConst.PAGE_STOREFRONT + "/" + order.getId())));

		getSearchBar().addFilterChangeListener(
				e -> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));
		getSearchBar().addActionClickListener(e -> presenter.createNewOrder());

		presenter.init(this);

		dialog.addDialogCloseActionListener(e -> presenter.cancel());
	}

	@Override
	public ConfirmDialog getConfirmDialog() {
		return confirmation;
	}

	@Override
	public void setConfirmDialog(ConfirmDialog confirmDialog) {
		this.confirmation = confirmDialog;
	}

	void setOpened(boolean opened) {
		dialog.setOpened(opened);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Long> orderId = event.getRouteParameters().getLong(ORDER_ID);
		if (orderId.isPresent()) {
			boolean isEditView = EDIT_SEGMENT.equals(getLastSegment(event));
			presenter.onNavigation(orderId.get(), isEditView);
		} else if (dialog.isOpened()) {
			presenter.closeSilently();
		}
	}

	void navigateToMainView() {
		getUI().ifPresent(ui -> ui.navigate(BakeryConst.PAGE_STOREFRONT));
	}

	@Override
	public boolean isDirty() {
		return orderEditor.hasChanges() || orderDetails.isDirty();
	}

	@Override
	public void write(Order entity) throws ValidationException {
		orderEditor.write(entity);
	}

	public Stream<HasValue<?, ?>> validate() {
		return orderEditor.validate();
	}

	SearchBar getSearchBar() {
		return searchBar;
	}

	OrderEditor getOpenedOrderEditor() {
		return orderEditor;
	}

	OrderDetails getOpenedOrderDetails() {
		return orderDetails;
	}

	Grid<Order> getGrid() {
		return grid;
	}

	@Override
	public void clear() {
		orderDetails.setDirty(false);
		orderEditor.clear();
	}

	void setDialogElementsVisibility(boolean editing) {
		dialog.add(editing ? orderEditor : orderDetails);
		orderEditor.setVisible(editing);
		orderDetails.setVisible(!editing);
	}

	@Override
	public String getEntityName() {
		return EntityUtil.getName(Order.class);
	}

	private String getLastSegment(BeforeEnterEvent event) {
		List<String> segments = event.getLocation().getSegments();
		return segments.get(segments.size() - 1);
	}
}
