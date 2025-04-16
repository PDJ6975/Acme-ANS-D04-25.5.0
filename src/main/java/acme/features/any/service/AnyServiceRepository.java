
package acme.features.any.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.services.Service;

@Repository
public interface AnyServiceRepository extends AbstractRepository {

	@Query("SELECT s FROM Service s")
	Collection<Service> findAllPublishedServices();

	@Query("SELECT s FROM Service s WHERE s.id = :id")
	Service findAllPublishedServices(int id);

}
