package purchasetracker.component;

import java.util.Objects;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import purchasetracker.domain.Purchaser;

@Singleton
public class PurchaserDataComponent {

	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persist-unit");

	public Purchaser retrieve(Long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Purchaser purchaser = null;

		try {
			purchaser = entityManager.find(Purchaser.class, id);
		} finally {
			entityManager.close();
		}

		return purchaser;
	}

	public void delete(Long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();

			Purchaser purchaser = entityManager.find(Purchaser.class, id);

			if (purchaser == null) {
				System.out.println("Error Deleting Customer: Customer not found");
			} else {
				entityManager.remove(purchaser);
			}

			transaction.commit();
		} catch (Exception e) {
			System.out.println("Error Deleting Customer: " + e.getMessage());

			transaction.rollback();
		} finally {
			entityManager.close();
		}
	}

	public void save(Purchaser purchaser) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		Purchaser savedPurchaser = retrieveByName(purchaser.getName());

		if (!Objects.isNull(savedPurchaser)) {
			return;
		}
		
		try {
			
			transaction.begin();
			entityManager.merge(purchaser);
			transaction.commit();
			
		} catch (Exception e) {
			System.out.println("Error Saving Customer: " + e.getMessage());

			transaction.rollback();
		} finally {
			entityManager.close();
		}
	}

	public Purchaser retrieveByName(String purchaserName) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query query = entityManager.createQuery("Select p FROM Purchaser p WHERE p.name = :name");
		query.setParameter("name", purchaserName);

		try {
			return (Purchaser) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}
}
