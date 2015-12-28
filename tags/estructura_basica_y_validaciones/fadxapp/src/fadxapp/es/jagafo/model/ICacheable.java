package fadxapp.es.jagafo.model;

import java.io.Serializable;

/**
 * Todo elemento del modelo que se pueda almacenar en cache debe implementar la
 * siguiente interfaz
 */
public interface ICacheable extends Serializable {
	/**
	 * Devuelve la id con la que se almacenara el objeto
	 * 
	 * @return Identificado del objeto
	 */
	String getCacheKeyId();

	/**
	 * Cada objeto debe ser capaz, por si mismo, de indicar si esta o no
	 * actualizado cuando se le pregunta
	 * 
	 * @return True si el objeto de cache esta actualizado, false en otro caso
	 */
	boolean isUpdate();

	/**
	 * Un objeto puede querer o no guardarse en determinado momento
	 * 
	 * @return True si podemos almacenar el objeto en cache, false en otroc aso
	 */
	boolean isCacheable();

	/**
	 * Establece cuando un objeto se puede guardar o no en cache
	 */
	void setCacheable(boolean b);
}
