
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.airlines.Airline;
import acme.entities.assignments.FlightAssignment;
import acme.entities.bookings.Booking;
import acme.entities.claims.Claim;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.passengers.Passenger;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.weathers.Weather;
import acme.realms.managers.Manager;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findOneById(int id);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	Collection<Flight> findManyByManagerId(int managerId);

	@Query("SELECT m FROM Manager m WHERE m.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirlines();

	//Querys para el delete de Flight

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduledDeparture ASC")
	List<Leg> findLegsByFlightId(int id);

	@Query("SELECT b FROM Booking b WHERE b.flight.id = :flightId")
	List<Booking> findBookingsByFlightId(int flightId);

	@Query("SELECT p FROM Passenger p WHERE p.booking IN :bookings")
	List<Passenger> findPassengersByBookings(List<Booking> bookings);

	@Query("SELECT w FROM Weather w WHERE w.flight.id = :flightId")
	Weather findWeatherByFlightId(int flightId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg IN :legs")
	List<FlightAssignment> findFlightAssignmentsByLegs(List<Leg> legs);

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment IN :flightAssignments")
	List<ActivityLog> findActivityLogsByFlightAssignments(List<FlightAssignment> flightAssignments);

	@Query("SELECT clm FROM Claim clm WHERE clm.leg IN :legs")
	List<Claim> findClaimByLegs(List<Leg> legs);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim IN :claims")
	List<TrackingLog> findTrackingLogsByClaim(List<Claim> claims);
}
