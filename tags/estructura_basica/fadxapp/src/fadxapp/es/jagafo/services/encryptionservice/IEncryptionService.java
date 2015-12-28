package fadxapp.es.jagafo.services.encryptionservice;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Implementa el servicio de encriptacion/desencriptacion de datos
 * @author jagafo
 *
 */
@ServiceImplAnnotation(serviceImpl=EncryptionService.class)
public interface IEncryptionService extends IService {

	public abstract String encrypt(String value);

	public abstract String decrypt(String value);

	public abstract boolean isInitialize();

}