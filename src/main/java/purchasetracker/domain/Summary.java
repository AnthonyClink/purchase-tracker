package purchasetracker.domain;

public class Summary {

	private double totalRevenueAddedThisUpload;
	private double totalRevenueConverted;
	private int totalItemsConverted;

	public double getTotalRevenueAddedThisUpload() {
		return totalRevenueAddedThisUpload;
	}

	public double getTotalRevenueConverted() {
		return totalRevenueConverted;
	}

	public int getTotalItemsConverted() {
		return totalItemsConverted;
	}

	public void addTotalRevenueAddedThisUpload(double amount){
		totalRevenueAddedThisUpload += amount;
	}
	
	public void addTotalRevenueConverted(double amount){
		totalRevenueConverted += amount;
	}
	
	public void addTotalItemsConverted(int amount){
		totalItemsConverted += amount;
	}

}
