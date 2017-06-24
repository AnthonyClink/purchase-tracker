package purchasetracker.web;

import java.io.File;
import java.net.URL;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import junit.framework.Assert;
import purchasetracker.PurchaseTracker;
import purchasetracker.domain.Summary;

public class LegacyDataResourceIntegrationTest extends JerseyTest {

	@Override
	public ResourceConfig configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		return new PurchaseTracker.ApplicationConfig();
	}

	@Override
	protected void configureClient(ClientConfig clientConfig) {
		clientConfig.register(MultiPartFeature.class);
		clientConfig.register(JacksonJsonProvider.class);
	}

	@Test
	public void canSubmitPostDataToServer() {
		String fileName = "/example.csv";

		URL url = this.getClass().getResource(fileName);
		File data = new File(url.getFile());
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);

		@SuppressWarnings("resource")
		MultiPart entity = new FormDataMultiPart().bodyPart(fileDataBodyPart);

		Summary summary = target().path(PurchaseTracker.ROOT_PATH).request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE), Summary.class);

		// String entityString = response.readEntity(String.class);

		Assert.assertEquals(12, summary.getTotalItemsConverted());
		Assert.assertEquals(95.0d, summary.getTotalRevenueConverted());
		Assert.assertEquals(95.0d, summary.getTotalRevenueAddedThisUpload());

//		System.out.println(entityString);
	}

}
