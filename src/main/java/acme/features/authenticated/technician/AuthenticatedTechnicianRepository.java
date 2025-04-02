
package acme.features.authenticated.technician;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.technicians.Technician;

@Repository
public interface AuthenticatedTechnicianRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select c from Technician c where c.userAccount.id = :id")
	Technician findTechnicianByUserAccountId(int id);

}
