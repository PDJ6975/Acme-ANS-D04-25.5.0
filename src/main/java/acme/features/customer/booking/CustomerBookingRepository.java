
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(final int bookingId);

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(final int customerId);

	@Query("SELECT c FROM Customer c WHERE c.id = :id")
	Customer findCustomerById(int id);

	@Query("SELECT b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordsByBookingId(int bookingId);

	@Query("SELECT DISTINCT f FROM Flight f JOIN Leg l ON l.flight = f " + "WHERE f.draftMode = false " + "AND l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.flight = f) " + "AND l.scheduledDeparture > :currentDate")
	Collection<Flight> findAvailableFlights(Date currentDate);

	@Query("SELECT COUNT(p) FROM BookingRecord br JOIN br.passenger p WHERE br.booking.id = :bookingId AND p.draftMode = false")
	int countPublishedPassengersByBookingId(int bookingId);

	@Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.locatorCode = :locatorCode AND b.id != :id")
	boolean existsByLocatorCodeAndNotId(String locatorCode, int id);
}
