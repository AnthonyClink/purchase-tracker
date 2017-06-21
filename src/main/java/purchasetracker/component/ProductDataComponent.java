package purchasetracker.component;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import purchasetracker.domain.Product;

@Singleton
public class ProductDataComponent {

	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persist-unit");

	public Product retrieve(Long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Product product = null;

		try {
			product = entityManager.find(Product.class, id);
		} finally {
			entityManager.close();
		}

		return product;
	}

	public void delete(Long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();

			Product product = entityManager.find(Product.class, id);

			if (product == null) {
				System.out.println("Error Deleting Customer: Customer not found");
			} else {
				entityManager.remove(product);
			}

			transaction.commit();
		} catch (Exception e) {
			System.out.println("Error Deleting Customer: " + e.getMessage());

			transaction.rollback();
		} finally {
			entityManager.close();
		}
	}

	public void save(Product product) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		try {
			
			transaction.begin();
			entityManager.merge(product);
			transaction.commit();
			
		} catch (Exception e) {
			transaction.rollback();
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			entityManager.close();
		}
	}

	public Product retrieveByName(String productName) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query query = entityManager.createQuery("Select p FROM Product p WHERE p.name = :name");
		query.setParameter("name", productName);

		try {
			return (Product) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<Product> retrieveAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		 
		TypedQuery<Product> query = entityManager.createQuery("Select p FROM Product p", Product.class);
		
		return query.getResultList();
	}
}
