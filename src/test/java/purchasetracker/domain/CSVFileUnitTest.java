package purchasetracker.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import junit.framework.Assert;
import purchasetracker.domain.CSVFile;
import purchasetracker.domain.CSVFile.Field;
import purchasetracker.domain.CSVFile.Row;

public class CSVFileUnitTest {

	private static final String HEADER_TEST_DATA = "purchaser name,item description,item price,purchase count,merchant address,merchant name";
	private static final String FIRST_LINE = "Snake Plissken,$10 off $20 of food,10.0,2,987 Fake St,Bob's Pizza";

	@Test
	public void csvFileFieldsCanRetrievePrimitiveJavaTypes() {

		String file = HEADER_TEST_DATA + "\n" + FIRST_LINE;

		InputStream inputStream = new ByteArrayInputStream(file.getBytes());

		CSVFile csvFile = new CSVFile(inputStream);
		
		Row row1 = csvFile.getRows().get(0);
		
		Field itemPrice = row1.get("item price");
		Field purchaseCount = row1.get("purchase count");
		
		Assert.assertTrue(itemPrice.isDouble());
		Assert.assertEquals(10.0d, itemPrice.getAsDouble());
		Assert.assertTrue(!itemPrice.isInteger());
		
		Assert.assertTrue(purchaseCount.isInteger());

	}

	@Test
	public void csvFileTreatsFirstLineAsHeader() {

		InputStream inputStream = new ByteArrayInputStream(HEADER_TEST_DATA.getBytes());

		CSVFile csvFile = new CSVFile(inputStream);

		Assert.assertEquals("purchase count", csvFile.getHeaders().get(3));

	}

}
