package purchasetracker.service;

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import purchasetracker.component.CompanyDataComponent;
import purchasetracker.component.ProductDataComponent;
import purchasetracker.component.PurchaserDataComponent;
import purchasetracker.domain.CSVFile;
import purchasetracker.domain.CSVFile.Row;
import purchasetracker.domain.Company;
import purchasetracker.domain.Product;
import purchasetracker.domain.Purchaser;
import purchasetracker.domain.Summary;

@Singleton
public class CSVDataService {

	private final PurchaserDataComponent purchaserDataComponent;
	private final CompanyDataComponent companyDataComponent;
	private final ProductDataComponent productDataComponent;

	@Inject
	public CSVDataService(PurchaserDataComponent purchaserDataComponent, CompanyDataComponent companyDataComponent,
			ProductDataComponent productDataComponent) {
		this.purchaserDataComponent = purchaserDataComponent;
		this.companyDataComponent = companyDataComponent;
		this.productDataComponent = productDataComponent;
	}
	
	public List<Product> getProducts(){
		return productDataComponent.retrieveAll();
	}
	
	public Summary addFileToDatabaseAndSummarize(InputStream data) {
		
		CSVFile csvFile = new CSVFile(data);
		Summary summary = new Summary();
		
		//yeah I could query this.. but im way out of time
		List<Product> savedProducts = productDataComponent.retrieveAll();
		
		for(Product savedProduct : savedProducts){
			summary.addTotalItemsConverted(savedProduct.getCount());
			summary.addTotalRevenueConverted(savedProduct.getPrice() * savedProduct.getCount());
		}
				
		
		for (Row row : csvFile.getRows()) {

			Purchaser purchaser = new Purchaser();
			purchaser.setName(row.get("purchaser name").getAsString());

			Company company = new Company();
			company.setName(row.get("merchant name").getAsString());
			company.setAddress(row.get("merchant address").getAsString());
			
			purchaserDataComponent.save(purchaser);
			companyDataComponent.save(company);
			
			Product product = new Product();
			product.setDescription(row.get("item description").getAsString());
			product.setCount(row.get("purchase count").getAsInteger());
			product.setPrice(row.get("item price").getAsDouble());
			product.setPurchaser(purchaserDataComponent.retrieveByName(purchaser.getName()));
			product.setCompany(companyDataComponent.retrieveByName(company.getName()));
		
			productDataComponent.save(product);
			
			summary.addTotalItemsConverted(product.getCount());
			summary.addTotalRevenueAddedThisUpload(product.getPrice() * product.getCount());
			summary.addTotalRevenueConverted(product.getPrice() * product.getCount());
			
		}
	
		return summary;

	}

}
