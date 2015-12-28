package fadxapp.es.jagafo.model.appproperty;

import org.jdom2.Element;

/**
 * Objeto con el que se manejan las propiedades de la aplicacion a la hora de
 * mostrarlas graficamente mediante el {@link AppPropertiesEditor}
 */
public class PropertyItem implements IPropertyItem {
	private String key;
	private String label;
	private String value;
	private String type;

	public PropertyItem(Object element) {
		this(element, false);
	}

	public PropertyItem(Object object, boolean loadDebug) {
		if (object instanceof Element) {
			Element element = (Element) object;
			if (loadDebug)
				setKey(element.getAttributeValue(PROPERTY_KEY) + DEBUG_END_KEY);
			else
				setKey(element.getAttributeValue(PROPERTY_KEY));
			if (loadDebug)
				setLabel(element.getAttributeValue(LABEL) + DEBUG_END_KEY);
			else
				setLabel(element.getAttributeValue(LABEL));
			String value;
			if (loadDebug)
				value = element.getAttributeValue(DEBUG_KEY_VALUE);
			else
				value = element.getValue();
			setValue(value);
			setType(element.getAttributeValue(TYPE));
		}
	}

	public PropertyItem(String key, String value, String type) {
		setKey(key);
		setValue(value);
		setType(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.hercules.repository.modellayer.properties.IPropertyItem2#getKey()
	 */
	public String getKey() {
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#setKey(java
	 * .lang.String)
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#setLabel(
	 * java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#getValue()
	 */
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#setValue(
	 * java.lang.String)
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#getType()
	 */
	public String getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#setType(java
	 * .lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#isDirty(java
	 * .lang.String)
	 */
	public boolean isDirty(String newValue) {
		return !value.equalsIgnoreCase(newValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IPropertyItem2#toString()
	 */

	public String toString() {
		return getValue();
	}
}
