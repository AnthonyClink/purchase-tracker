package purchasetracker.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class CSVFileIntegrationTest {

	@Test
	public void canReadABlobOfTextFromExtenalSource(){
		
		URL url = this.getClass().getResource("/example.csv");
        File file = new File(url.getFile());
        
		try {
			CSVFile csvFile = new CSVFile(new FileInputStream(file));
			
			Assert.assertEquals(4, csvFile.getRows().size());
			
			Assert.assertEquals("Snake Plissken", csvFile.getRow(3).get("purchaser name").getAsString());
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
