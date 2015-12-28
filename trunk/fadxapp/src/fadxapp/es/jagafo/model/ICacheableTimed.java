package fadxapp.es.jagafo.model;

/**
 *	Cualquier elemento que siendo cacheable nos interesa guardar
 *	un sello de tiempo que indica cuando fue cacheado 
 */
public interface ICacheableTimed extends ICacheable{
	/**
	 * Establece el tiempo en el que el objeto se cacheo
	 * @param timeInMillis
	 */
	void setCachedTime(long timeInMillis);
	
	/**
	 * Devuelve en milisegundos la fecha en la que se cacheo el objeto 
	 * @return Long de time in millis
	 */
	long getCachedTime();
}
