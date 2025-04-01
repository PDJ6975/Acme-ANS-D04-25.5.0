
package acme.features.customer.booking;

import java.util.Collection;

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

	@Query("select f from Flight f where f.scheduledDeparture > CURRENT_TIMESTAMP")
	Collection<Flight> findAvailableFlights();
}
