package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider.OrderFilter;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrderCardHeader;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_STOREFRONT_ORDER_EDIT;

/**
 * Presenter class that handles the business logic and UI interactions for the StorefrontView.
 * 
 * <p>This presenter follows the MVP (Model-View-Presenter) pattern and is responsible for:
 * <ul>
 *   <li>Managing data flow between the view and backend services</li>
 *   <li>Handling user interactions and view state changes</li>
 *   <li>Coordinating order card header generation for visual grouping</li>
 *   <li>Managing navigation and URL routing</li>
 *   <li>Providing data provider configuration for the order grid</li>
 * </ul>
 * </p>
 * 
 * <p>The presenter is scoped as prototype, meaning a new instance is created for each
 * view instance, ensuring proper isolation between different view sessions.</p>
 * 
 * <p>This class integrates with:
 * <ul>
 *   <li>{@link OrderService} - For order business operations</li>
 *   <li>{@link OrdersGridDataProvider} - For data binding and pagination</li>
 *   <li>{@link EntityPresenter} - For generic CRUD operations</li>
 *   <li>{@link CurrentUser} - For user context and security</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderPresenter {

	/**
	 * Generator for creating order card headers for visual grouping.
	 */
	private OrderCardHeaderGenerator headersGenerator;
	
	/**
	 * Reference to the associated view component.
	 */
	private StorefrontView view;

	/**
	 * Generic entity presenter for handling CRUD operations.
	 */
	private final EntityPresenter<Order, StorefrontView> entityPresenter;
	
	/**
	 * Data provider for the orders grid with filtering capabilities.
	 */
	private final OrdersGridDataProvider dataProvider;
	
	/**
	 * Current user context for security and personalization.
	 */
	private final CurrentUser currentUser;
	
	/**
	 * Service for order-related business operations.
	 */
	private final OrderService orderService;

	/**
	 * Constructs a new OrderPresenter with the required dependencies.
	 * 
	 * @param orderService the service for order operations
	 * @param dataProvider the data provider for order grid
	 * @param entityPresenter the generic entity presenter for CRUD operations
	 * @param currentUser the current user context
	 */
	@Autowired
	OrderPresenter(OrderService orderService, OrdersGridDataProvider dataProvider,
			EntityPresenter<Order, StorefrontView> entityPresenter, CurrentUser currentUser) {
		this.orderService = orderService;
		this.entityPresenter = entityPresenter;
		this.dataProvider = dataProvider;
		this.currentUser = currentUser;
		headersGenerator = new OrderCardHeaderGenerator();
		headersGenerator.resetHeaderChain(false);
		dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
	}

	void init(StorefrontView view) {
		this.entityPresenter.setView(view);
		this.view = view;
		view.getGrid().setDataProvider(dataProvider);
		view.getOpenedOrderEditor().setCurrentUser(currentUser.getUser());
		view.getOpenedOrderEditor().addCancelListener(e -> cancel());
		view.getOpenedOrderEditor().addReviewListener(e -> review());
		view.getOpenedOrderDetails().addSaveListenter(e -> save());
		view.getOpenedOrderDetails().addCancelListener(e -> cancel());
		view.getOpenedOrderDetails().addBackListener(e -> back());
		view.getOpenedOrderDetails().addEditListener(e -> edit());
		view.getOpenedOrderDetails().addCommentListener(e -> addComment(e.getMessage()));
	}

	OrderCardHeader getHeaderByOrderId(Long id) {
		return headersGenerator.get(id);
	}

	public void filterChanged(String filter, boolean showPrevious) {
		headersGenerator.resetHeaderChain(showPrevious);
		dataProvider.setFilter(new OrderFilter(filter, showPrevious));
	}

	void onNavigation(Long id, boolean edit) {
		entityPresenter.loadEntity(id, e -> open(e, edit));
	}

	void createNewOrder() {
		open(entityPresenter.createNew(), true);
	}

	void cancel() {
            entityPresenter.cancel(this::close, () -> view.setOpened(true));
	}

	void closeSilently() {
		entityPresenter.close();
		view.setOpened(false);
	}

	void edit() {
        UI.getCurrent()
                .navigate(String.format(PAGE_STOREFRONT_ORDER_EDIT,
                        entityPresenter.getEntity().getId()));
	}

	void back() {
		view.setDialogElementsVisibility(true);
	}

	void review() {
		// Using collect instead of findFirst to assure all streams are
		// traversed, and every validation updates its view
		List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
		if (fields.isEmpty()) {
			if (entityPresenter.writeEntity()) {
				view.setDialogElementsVisibility(false);
				view.getOpenedOrderDetails().display(entityPresenter.getEntity(), true);
			}
		} else if (fields.get(0) instanceof Focusable) {
			((Focusable<?>) fields.get(0)).focus();
		}
	}

	void save() {
		entityPresenter.save(e -> {
			if (entityPresenter.isNew()) {
				view.showCreatedNotification();
				dataProvider.refreshAll();
			} else {
				view.showUpdatedNotification();
				dataProvider.refreshItem(e);
			}
			close();
		});

	}

	void addComment(String comment) {
		if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser.getUser(), e, comment))) {
			// You can only add comments when in view mode, so reopening in that state.
			open(entityPresenter.getEntity(), false);
		}
	}

	private void open(Order order, boolean edit) {
		view.setDialogElementsVisibility(edit);
		view.setOpened(true);

		if (edit) {
			view.getOpenedOrderEditor().read(order, entityPresenter.isNew());
		} else {
			view.getOpenedOrderDetails().display(order, false);
		}
	}

	private void close() {
		view.getOpenedOrderEditor().close();
		view.setOpened(false);
		view.navigateToMainView();
		entityPresenter.close();
	}
}
