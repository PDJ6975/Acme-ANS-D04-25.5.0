
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
import acme.entities.bookings.BookingRecord;
import acme.entities.claims.Claim;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.passengers.Passenger;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.weathers.Weather;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	// --------------------
	// Lectura principal
	// --------------------
	Flight findOneById(int id);

	@Query("select f from Flight f where f.manager.id = :managerId")
	Collection<Flight> findManyByManagerId(int managerId);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	// --------------------
	// Entidades relacionadas
	// --------------------
	@Query("select b from Booking b where b.flight.id = :flightId")
	List<Booking> findBookingsByFlightId(int flightId);

	@Query("select br from BookingRecord br where br.booking in :bookings")
	List<BookingRecord> findBookingRecordsByBookings(List<Booking> bookings);

	@Query("select distinct br.passenger from BookingRecord br where br.booking in :bookings")
	List<Passenger> findDistinctPassengersByBookings(List<Booking> bookings);

	@Query("select w from Weather w where w.flight.id = :flightId")
	Weather findWeatherByFlightId(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> findLegsByFlightId(int flightId);

	@Query("select fa from FlightAssignment fa where fa.leg in :legs")
	List<FlightAssignment> findFlightAssignmentsByLegs(List<Leg> legs);

	@Query("select al from ActivityLog al where al.flightAssignment in :assignments")
	List<ActivityLog> findActivityLogsByFlightAssignments(List<FlightAssignment> assignments);

	@Query("select c from Claim c where c.leg in :legs")
	List<Claim> findClaimByLegs(List<Leg> legs);

	@Query("select tl from TrackingLog tl where tl.claim in :claims")
	List<TrackingLog> findTrackingLogsByClaim(List<Claim> claims);
}
