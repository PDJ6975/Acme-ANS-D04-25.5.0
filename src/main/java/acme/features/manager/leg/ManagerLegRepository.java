
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.assignments.FlightAssignment;
import acme.entities.claims.Claim;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("""
			SELECT l
			FROM Leg l
			WHERE l.flight.id = :masterId
			ORDER BY l.scheduledDeparture ASC
		""")
	List<Leg> findLegsSortedMomentByFlightId(int masterId);

	@Query("""
			SELECT l
			FROM Leg l
			WHERE l.id = :id
		""")
	Leg findLegById(int id);

	@Query("SELECT ap FROM Airport ap")
	Collection<Airport> findAirports();

	@Query("SELECT acf FROM Aircraft acf")
	Collection<Aircraft> findAircrafts();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.id = :legId")
	List<FlightAssignment> findFlightAssignmentsByLegId(int legId);

	@Query("SELECT clm FROM Claim clm WHERE clm.leg.id = :legId")
	List<Claim> findClaimsByLegId(int legId);

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT COUNT(l) > 0 FROM Leg l WHERE l.flightNumber = :flightNumber")
	boolean existsLegByFlightNumber(String flightNumber);

}
