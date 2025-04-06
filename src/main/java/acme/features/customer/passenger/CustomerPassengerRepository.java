
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p JOIN BookingRecord br ON br.passenger.id = p.id WHERE br.booking.id = :masterId")
	Collection<Passenger> findPassengersByBookingId(int masterId);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(int id);

	@Query("SELECT b FROM Booking b WHERE b.id = :id")
	Booking findBookingById(int id);

	@Query("SELECT br FROM BookingRecord br WHERE br.passenger.id = :passengerId AND br.booking.id = :bookingId")
	BookingRecord findBookingRecordByPassengerAndBookingId(int passengerId, int bookingId);

	@Query("SELECT count(br) > 0 FROM BookingRecord br WHERE br.passenger.id = :passengerId AND br.booking.id = :bookingId")
	boolean existsBookingRecordByPassengerAndBookingId(int passengerId, int bookingId);

	@Query("SELECT br FROM BookingRecord br WHERE br.passenger.id = :passengerId")
	Collection<BookingRecord> findBookingRecordsByPassengerId(int passengerId);
}
