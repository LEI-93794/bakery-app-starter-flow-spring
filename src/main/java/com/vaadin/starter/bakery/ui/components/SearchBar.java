package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DebounceSettings;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;

/**
 * Custom search bar component for filtering data in the bakery application.
 * 
 * <p>This component provides a comprehensive search interface with:
 * <ul>
 *   <li>Text input field with eager value change mode for instant feedback</li>
 *   <li>Clear button for resetting the search</li>
 *   <li>Action button for additional search operations</li>
 *   <li>Checkbox functionality for advanced filtering options</li>
 *   <li>Event-driven architecture with custom events for filter changes</li>
 * </ul>
 * </p>
 * 
 * <p>The component is implemented as a LitTemplate with a corresponding
 * JavaScript template for enhanced performance and user experience.</p>
 * 
 * <p>Events fired by this component:
 * <ul>
 *   <li>{@code FilterChanged} - When search criteria change</li>
 *   <li>{@code SearchValueChanged} - When text input value changes</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@Tag("search-bar")
@JsModule("./src/components/search-bar.js")
public class SearchBar extends LitTemplate {

	/**
	 * Text input field for search queries.
	 * Mapped to the "field" element in the template.
	 */
	@Id("field")
	private TextField textField;

	/**
	 * Button for clearing the search input.
	 * Mapped to the "clear" element in the template.
	 */
	@Id("clear")
	private Button clearButton;

	/**
	 * Action button for additional search operations.
	 * Mapped to the "action" element in the template.
	 */
	@Id("action")
	private Button actionButton;

	/**
	 * Constructs a new SearchBar component with default behavior.
	 * Sets up event listeners and value change modes for responsive searching.
	 */
	public SearchBar() {
		textField.setValueChangeMode(ValueChangeMode.EAGER);

		ComponentUtil.addListener(textField, SearchValueChanged.class,
				e -> fireEvent(new FilterChanged(this, false)));

		clearButton.addClickListener(e -> {
			textField.clear();
			getElement().setProperty("checkboxChecked", false);
		});

		getElement().addPropertyChangeListener("checkboxChecked", e -> fireEvent(new FilterChanged(this, false)));
	}

	public String getFilter() {
		return textField.getValue();
	}

	@Synchronize("checkbox-checked-changed")
	public boolean isCheckboxChecked() {
		return getElement().getProperty("checkboxChecked", false);
	}

	public void setPlaceHolder(String placeHolder) {
		textField.setPlaceholder(placeHolder);
	}

	public void setActionText(String actionText) {
		getElement().setProperty("buttonText", actionText);
	}

	public void setCheckboxText(String checkboxText) {
		getElement().setProperty("checkboxText", checkboxText);
	}

	public void addFilterChangeListener(ComponentEventListener<FilterChanged> listener) {
		this.addListener(FilterChanged.class, listener);
	}

	public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		actionButton.addClickListener(listener);
	}

	public Button getActionButton() {
		return actionButton;
	}

	@DomEvent(value = "value-changed", debounce = @DebounceSettings(timeout = 300, phases = DebouncePhase.TRAILING))
	public static class SearchValueChanged extends ComponentEvent<TextField> {
		public SearchValueChanged(TextField source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public static class FilterChanged extends ComponentEvent<SearchBar> {
		public FilterChanged(SearchBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}
}
