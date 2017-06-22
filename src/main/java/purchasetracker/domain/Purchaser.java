package purchasetracker.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public class Purchaser {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	@Column(unique=true, name="NAME")
	private String name;

	@JoinTable
	@OneToMany
	private Set<Product> purchasedProducts;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public Set<Product> getPurchasedProducts() {
		return purchasedProducts;
	}

	public void setPurchasedProducts(Set<Product> purchasedProducts) {
		this.purchasedProducts = purchasedProducts;
	}
	
}
