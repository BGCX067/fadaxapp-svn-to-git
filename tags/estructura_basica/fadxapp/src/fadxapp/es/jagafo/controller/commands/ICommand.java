package fadxapp.es.jagafo.controller.commands;


/**
 * Define los metodos comunes de todos los comandos de la aplicacion 
 * 
 * @author luisjgf
 */
public interface ICommand<T> {
	
	void preprocess(Object value);
	

}
