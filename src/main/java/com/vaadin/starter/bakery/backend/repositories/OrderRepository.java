package com.vaadin.starter.bakery.backend.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;

/**
 * Repository interface for managing {@link Order} entities.
 * 
 * <p>This repository provides specialized query methods for order management including:
 * <ul>
 *   <li>Filtering orders by due date and customer information</li>
 *   <li>Counting orders by various criteria</li>
 *   <li>Generating reports and analytics data</li>
 *   <li>Optimized entity loading using EntityGraphs</li>
 * </ul>
 * </p>
 * 
 * <p>Most query methods use EntityGraphs to optimize database fetching:
 * <ul>
 *   <li>{@code ENTITY_GRAPTH_BRIEF} - Loads basic order information</li>
 *   <li>{@code ENTITY_GRAPTH_FULL} - Loads complete order details including items</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * Finds orders with due dates after the specified date with pagination support.
	 * Uses the brief entity graph for optimized loading.
	 * 
	 * @param filterDate the minimum due date for filtering orders
	 * @param pageable pagination information
	 * @return a page of orders with due dates after the filter date
	 */
	@EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
	Page<Order> findByDueDateAfter(LocalDate filterDate, Pageable pageable);

	/**
	 * Finds orders by customer full name containing the search query (case-insensitive).
	 * Uses the brief entity graph for optimized loading.
	 * 
	 * @param searchQuery the search term to match against customer full names
	 * @param pageable pagination information
	 * @return a page of orders matching the search criteria
	 */
	@EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
	Page<Order> findByCustomerFullNameContainingIgnoreCase(String searchQuery, Pageable pageable);

	/**
	 * Finds orders by customer full name and due date criteria.
	 * Combines customer name search with due date filtering.
	 * 
	 * @param searchQuery the search term to match against customer full names
	 * @param dueDate the minimum due date for filtering orders
	 * @param pageable pagination information
	 * @return a page of orders matching both criteria
	 */
	@EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
	Page<Order> findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate, Pageable pageable);

	/**
	 * Retrieves all orders with brief entity graph loading.
	 * Overrides the default findAll to use optimized entity loading.
	 * 
	 * @return a list of all orders
	 */
	@Override
	@EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
	List<Order> findAll();

	/**
	 * Retrieves all orders with pagination and brief entity graph loading.
	 * Overrides the default findAll to use optimized entity loading.
	 * 
	 * @param pageable pagination information
	 * @return a page of all orders
	 */
	@Override
	@EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
	Page<Order> findAll(Pageable pageable);

	/**
	 * Finds order summaries with due dates greater than or equal to the specified date.
	 * Returns simplified order summary projections for performance.
	 * 
	 * @param dueDate the minimum due date for filtering
	 * @return a list of order summaries matching the criteria
	 */
	@EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraphType.LOAD)
	List<OrderSummary> findByDueDateGreaterThanEqual(LocalDate dueDate);

	/**
	 * Finds an order by its ID with full entity graph loading.
	 * Overrides the default findById to load complete order details including items.
	 * 
	 * @param id the order ID
	 * @return an optional containing the order if found, empty otherwise
	 */
	@Override
	@EntityGraph(value = Order.ENTITY_GRAPTH_FULL, type = EntityGraphType.LOAD)
	Optional<Order> findById(Long id);

	/**
	 * Counts orders with due dates after the specified date.
	 * 
	 * @param dueDate the minimum due date for counting
	 * @return the number of orders with due dates after the specified date
	 */
	long countByDueDateAfter(LocalDate dueDate);

	/**
	 * Counts orders by customer full name containing the search query (case-insensitive).
	 * 
	 * @param searchQuery the search term to match against customer full names
	 * @return the number of orders matching the search criteria
	 */
	long countByCustomerFullNameContainingIgnoreCase(String searchQuery);

	/**
	 * Counts orders by customer full name and due date criteria.
	 * 
	 * @param searchQuery the search term to match against customer full names
	 * @param dueDate the minimum due date for counting
	 * @return the number of orders matching both criteria
	 */
	long countByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate);

	/**
	 * Counts orders with the specified due date.
	 * 
	 * @param dueDate the exact due date to match
	 * @return the number of orders with the specified due date
	 */
	long countByDueDate(LocalDate dueDate);

	/**
	 * Counts orders with the specified due date and states.
	 * 
	 * @param dueDate the exact due date to match
	 * @param state collection of order states to match
	 * @return the number of orders matching both criteria
	 */
	long countByDueDateAndStateIn(LocalDate dueDate, Collection<OrderState> state);

	/**
	 * Counts orders with the specified state.
	 * 
	 * @param state the order state to match
	 * @return the number of orders with the specified state
	 */
	long countByState(OrderState state);

	/**
	 * Counts orders per month for a specific order state and year.
	 * Returns aggregated data showing monthly delivery counts.
	 * 
	 * @param orderState the order state to filter by
	 * @param year the year to filter by
	 * @return list of Object arrays containing [month, count] pairs
	 */
	@Query("SELECT month(dueDate) as month, count(*) as deliveries FROM OrderInfo o where o.state=?1 and year(dueDate)=?2 group by month(dueDate)")
	List<Object[]> countPerMonth(OrderState orderState, int year);

	/**
	 * Calculates total revenue per month for the last three years.
	 * Returns aggregated financial data for reporting and analytics.
	 * 
	 * @param orderState the order state to filter by
	 * @param year the current year (used to calculate the 3-year range)
	 * @return list of Object arrays containing [year, month, total_revenue] tuples
	 */
	@Query("SELECT year(o.dueDate) as y, month(o.dueDate) as m, sum(oi.quantity*p.price) as deliveries FROM OrderInfo o JOIN o.items oi JOIN oi.product p where o.state=?1 and year(o.dueDate)<=?2 AND year(o.dueDate)>=(?2-3) group by year(o.dueDate), month(o.dueDate) order by y desc, month(o.dueDate)")
	List<Object[]> sumPerMonthLastThreeYears(OrderState orderState, int year);

	/**
	 * Counts orders per day for a specific month and year.
	 * Provides detailed daily breakdown of order deliveries.
	 * 
	 * @param orderState the order state to filter by
	 * @param year the year to filter by
	 * @param month the month to filter by
	 * @return list of Object arrays containing [day, count] pairs
	 */
	@Query("SELECT day(dueDate) as day, count(*) as deliveries FROM OrderInfo o where o.state=?1 and year(dueDate)=?2 and month(dueDate)=?3 group by day(dueDate)")
	List<Object[]> countPerDay(OrderState orderState, int year, int month);

	/**
	 * Counts total quantity per product for a specific month and year.
	 * Provides product-level analytics for inventory and demand planning.
	 * 
	 * @param orderState the order state to filter by
	 * @param year the year to filter by
	 * @param month the month to filter by
	 * @return list of Object arrays containing [total_quantity, product] pairs
	 */
	@Query("SELECT sum(oi.quantity), p FROM OrderInfo o JOIN o.items oi JOIN oi.product p WHERE o.state=?1 AND year(o.dueDate)=?2 AND month(o.dueDate)=?3 GROUP BY p.id ORDER BY p.id")
	List<Object[]> countPerProduct(OrderState orderState, int year, int month);

}
