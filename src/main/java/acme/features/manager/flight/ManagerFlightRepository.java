
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.realms.managers.Manager;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findOneById(int id);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	Collection<Flight> findManyByManagerId(int managerId);

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT f from Flight f WHERE f.tag = :tag")
	Flight findFlightByTag(String tag);

	@Query("SELECT a FROM Airline a WHERE a.name = :name")
	Airline findAirlineByName(String name);

}
