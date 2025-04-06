
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("SELECT ap FROM Airport ap")
	Collection<Airport> findAllAirports();

	@Query("SELECT ap FROM Airport ap WHERE ap.id = :id")
	Airport findAirportById(int id);

	@Query("SELECT COUNT(ap) > 0 FROM Airport ap WHERE ap.iataCode = :iataCode")
	boolean existsByIataCode(String iataCode);

	@Query("SELECT ap FROM Airport ap WHERE ap.iataCode = :iataCode")
	Airport findAirportByIataCode(String iataCode);

}
