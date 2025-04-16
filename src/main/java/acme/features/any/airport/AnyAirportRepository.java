
package acme.features.any.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

@Repository
public interface AnyAirportRepository extends AbstractRepository {

	@Query("SELECT ap FROM Airport ap")
	Collection<Airport> findAllAirports();

	@Query("SELECT ap FROM Airport ap WHERE ap.id = :id")
	Airport findAirportById(int id);
}
