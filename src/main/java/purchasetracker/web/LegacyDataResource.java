package purchasetracker.web;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import purchasetracker.domain.Summary;
import purchasetracker.service.CSVDataService;

@Path("legacy")
public class LegacyDataResource {
	
	private final CSVDataService csvDataService;
	
	@Inject
	public LegacyDataResource(CSVDataService csvDataService) {
		this.csvDataService = csvDataService;
	}

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Summary processLegacyData(@FormDataParam("file") InputStream data, @FormDataParam("file") FormDataContentDisposition formDataContentDisposition){
    	return csvDataService.addFileToDatabaseAndSummarize(data);
    }
	
}
