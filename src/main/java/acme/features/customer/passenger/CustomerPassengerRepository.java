
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT p from Passenger p where p.booking.id = :masterId")
	Collection<Passenger> findPassengersByBookingId(int masterId);

	@Query("SELECT p from Passenger p where p.id = :id")
	Passenger findPassengerById(int id);

	@Query("SELECT b from Booking b where b.id = :id")
	Booking findBookingById(int id);
}
