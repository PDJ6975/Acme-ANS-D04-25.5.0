
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;

@Repository
public interface AdminBookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.draftMode = false")
	Collection<Booking> findPublishedBookings();

	@Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);
}
