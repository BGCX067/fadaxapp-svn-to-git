package fadxapp.es.jagafo.view.validation;

/**
 * Todo elemento que se encarga de validar un valor introducido por el usuario en la aplicacion 
 * @author jagafo
 */
public interface IValueValidation {
	/**
	 * 
	 * @param value
	 * @param errorMessage
	 * @return Devuelve el valor errorMessage si value no cumple la validacion
	 */
	String validation(Object value);
}
