
package acme.features.customer.profile;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface CustomerProfileRepository extends AbstractRepository {

	@Query("select c from Customer c where c.userAccount.id = :id")
	Customer findCustomerByUserAccountId(int id);
}
