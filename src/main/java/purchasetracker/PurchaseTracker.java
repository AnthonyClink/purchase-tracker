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

	public static class ApplicationBinder extends AbstractBinder{

		@Override
		protected void configure() {
			
			bind(PurchaserDataComponent.class).to(PurchaserDataComponent.class);
			bind(CompanyDataComponent.class).to(CompanyDataComponent.class);
			bind(ProductDataComponent.class).to(ProductDataComponent.class);
			
			bind(CSVDataService.class).to(CSVDataService.class);
		}
		
	}
	
	public static class ApplicationConfig extends ResourceConfig{
		
		public ApplicationConfig(){
			register(new ApplicationBinder());
			register(MultiPartFeature.class);
			register(LegacyDataResource.class);
			registerInstances(new LoggingFilter(LOGGER, true));
			
		}
		
	}
	
	public static void main(String[] args) {


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