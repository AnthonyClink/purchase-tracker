package purchasetracker.component;

import java.util.Objects;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import purchasetracker.domain.Company;

@Singleton
public class CompanyDataComponent {

	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persist-unit");

	public Company retrieve(Long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Company company = null;

		try {
			company = entityManager.find(Company.class, id);
		} finally {
			entityManager.close();
		}

		return company;
	}

	public void delete(Long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();

			Company company = entityManager.find(Company.class, id);

			if (company == null) {
				System.out.println("Error Deleting Customer: Customer not found");
			} else {
				entityManager.remove(company);
			}

			transaction.commit();
		} catch (Exception e) {
			System.out.println("Error Deleting Customer: " + e.getMessage());

			transaction.rollback();
		} finally {
			entityManager.close();
		}
	}

	public void save(Company company) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		Company savedCompany = retrieveByName(company.getName());

		if (!Objects.isNull(savedCompany)) {
			return;
		}

		try {

			transaction.begin();
			entityManager.merge(company);
			transaction.commit();

		} catch (Exception e) {
			System.out.println("Error Saving Customer: " + e.getMessage());

			transaction.rollback();
		} finally {
			entityManager.close();
		}
	}

	public Company retrieveByName(String CompanyName) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query query = entityManager.createQuery("Select p FROM Company p WHERE p.name = :name");
		query.setParameter("name", CompanyName);

		try {
			return (Company) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}
}
