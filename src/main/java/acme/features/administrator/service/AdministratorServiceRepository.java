
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;
import acme.entities.services.Service;

@Repository
public interface AdministratorServiceRepository extends AbstractRepository {

	@Query("SELECT s FROM Service s ORDER BY s.name ASC")
	Collection<Service> findAllServices();

	@Query("SELECT s FROM Service s WHERE s.id = :id")
	Service findServiceById(int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Airport a WHERE a.id = :id")
	Airport findAirportById(int id);

	@Query("SELECT s FROM Service s WHERE s.promotionCode = :code")
	Service findServiceByPromotionCode(String code);

}
