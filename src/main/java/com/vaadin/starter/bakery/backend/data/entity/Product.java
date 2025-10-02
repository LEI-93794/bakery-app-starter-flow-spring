package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * Entity representing a product in the bakery application.
 * 
 * <p>This entity stores information about bakery products including their name and price.
 * Products have unique names and prices are stored as integers (representing cents) to avoid
 * floating-point rounding errors.</p>
 * 
 * <p>Validation constraints ensure data integrity:
 * <ul>
 *   <li>Product names are required and must be unique</li>
 *   <li>Product names cannot exceed 255 characters</li>
 *   <li>Prices must be between 0 and 100000 (representing $0.00 to $1000.00)</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@Entity
public class Product extends AbstractEntity {

	/**
	 * The name of the product.
	 * This field is required, must be unique, and cannot exceed 255 characters.
	 */
	@NotBlank(message = "{bakery.name.required}")
	@Size(max = 255)
	@Column(unique = true)
	private String name;

	/**
	 * The price of the product in cents.
	 * Stored as an integer to avoid floating-point rounding errors.
	 * Valid range is 0 to 100000 (representing $0.00 to $1000.00).
	 */
	// Real price * 100 as an int to avoid rounding errors
	@Min(value = 0, message = "{bakery.price.limits}")
	@Max(value = 100000, message = "{bakery.price.limits}")
	private Integer price;

	/**
	 * Returns the name of the product.
	 * 
	 * @return the product name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the product.
	 * 
	 * @param name the product name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the price of the product in cents.
	 * 
	 * @return the product price in cents
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * Sets the price of the product in cents.
	 * 
	 * @param price the product price in cents to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}

	/**
	 * Returns a string representation of this product.
	 * 
	 * @return the product name
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Indicates whether some other object is "equal to" this product.
	 * Two products are considered equal if they have the same name, price, and inherited properties.
	 * 
	 * @param o the reference object with which to compare
	 * @return true if this product is the same as the obj argument; false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Product that = (Product) o;
		return Objects.equals(name, that.name) &&
				Objects.equals(price, that.price);
	}

	/**
	 * Returns a hash code value for this product based on its name, price, and inherited properties.
	 * 
	 * @return a hash code value for this product
	 */
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, price);
	}
}
