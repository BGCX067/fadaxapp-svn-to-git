package fadxapp.es.jagafo.view.validation.length;

import fadxapp.es.jagafo.view.validation.ValueValidation;

/**
 * Se encarga de validar un valor a partir de una expresion regular
 * @author jagafo
 *
 */
public class LengthValueValidation extends ValueValidation {
	private Integer minValue;
	private Integer maxValue;
	
	public LengthValueValidation(Integer minValue, Integer maxValue,String errorMessage){
		super(errorMessage);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public String validation(Object value) {
		if (getStringValue(value).length() < minValue || getStringValue(value).length() > maxValue)  
			return errorMessage;
		return EMPTY; 
	}
}
