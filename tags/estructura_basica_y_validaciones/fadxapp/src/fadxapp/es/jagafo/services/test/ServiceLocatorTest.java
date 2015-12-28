package fadxapp.es.jagafo.services.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fadxapp.es.jagafo.services.ConfigurationPaths;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.AccessResourcesService;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.logservice.ILogService;

public class ServiceLocatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void resourceServiceTest(){
		try{
			IAccessResourcesService resourceService = (IAccessResourcesService)
					ServiceLocator.getService(AccessResourcesService.class);
			InputStream input = resourceService.getResource(ConfigurationPaths.FICH_CONFIG_LOGS);
			assertNotNull(input);
		}catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void loggerServiceTest(){
		try{
			ILogService logService = (ILogService)
					ServiceLocator.getService(ILogService.class);
			System.out.println("Iniciando escritura en el log...");
			logService.debug(this.getClass(), "Mensaje de debug");
			logService.info(this.getClass(), "Mensaje de informacion");
			logService.warn(this.getClass(), "Mensaje de Advertencia");
			System.out.println("Fin escritura en el log...");
		}catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void databaseServiceTest(){
		try{
			
		}catch (Exception e){
			e.printStackTrace();
			fail();
		}
	}

}
