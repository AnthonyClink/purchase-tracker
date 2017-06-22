package purchasetracker;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import purchasetracker.component.CompanyDataComponent;
import purchasetracker.component.ProductDataComponent;
import purchasetracker.component.PurchaserDataComponent;
import purchasetracker.service.CSVDataService;
import purchasetracker.web.LegacyDataResource;

public class PurchaseTracker {

	public static final URI BASE_URI = URI.create("http://localhost:8080/");
	public static final String ROOT_PATH = "legacy";
	private static final Logger LOGGER = Logger.getLogger(PurchaseTracker.class.getName());

	//This is the main application injector configuration for the Hk2 injector from jersey
	public static class ApplicationBinder extends AbstractBinder{

		@Override
		protected void configure() {
			// bind(IMPL) does not work like it does in guice :(
			bind(PurchaserDataComponent.class).to(PurchaserDataComponent.class);
			bind(CompanyDataComponent.class).to(CompanyDataComponent.class);
			bind(ProductDataComponent.class).to(ProductDataComponent.class);
			
			bind(CSVDataService.class).to(CSVDataService.class);
		}
		
	}
	
	// this is the main HTTP configuration for jersey resources
	public static class ApplicationConfig extends ResourceConfig{
		
		public ApplicationConfig(){
			//this is where the injector configuration is added to the http config
			// this causes the first http call to init the entire container... may cause the first call to take a while
			register(new ApplicationBinder());
			
			// add the multipart jeresy extension to the http layer
			register(MultiPartFeature.class);
			
			// add our custom http resource containing our post call
			register(LegacyDataResource.class);
			
			// set all the jersey internal loggers
			registerInstances(new LoggingFilter(LOGGER, true));
			
		}
		
	}
	
	// no required external container to start and use this application. though you can always instanciate a new appconfig
	// and pass it to any war
	public static void main(String[] args) {

                // decided to go with grizzly instead of jetty since its configuration with jersey is much easier
		// as you can see here
		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new ApplicationConfig());

		System.out.println(
				String.format("Application started.\nTry out %s%s\nHit enter to stop it...", BASE_URI, ROOT_PATH));

		try {
			System.in.read();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			server.shutdownNow();
		}
	}
}
