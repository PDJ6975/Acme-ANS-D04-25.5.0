
package acme.features.customer.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select c from Customer c where c.userAccount.id = :userAccountId")
	Customer findCustomerByUserAccountId(int userAccountId);

	// Destinos obtenidos desde las legs asociadas al vuelo
	@Query("select l.arrivalAirport.city from Booking b join b.flight f join Leg l on l.flight.id = f.id " + "where b.customer.id = :customerId AND b.draftMode = false " + "group by l.arrivalAirport.city " + "order by max(b.purchaseMoment) desc")
	List<String> findLastFiveDestinations(int customerId);

	@Query("select sum(b.price.amount) from Booking b where b.customer.id = :customerId AND b.draftMode = false " + "and year(b.purchaseMoment) = year(current_date)-1")
	Double totalMoneySpentLastYear(int customerId);

	@Query("select b.travelClass, count(b) from Booking b where b.customer.id = :customerId AND b.draftMode = false " + "group by b.travelClass")
	List<Object[]> bookingsByTravelClass(int customerId);

	@Query("select count(b) from Booking b where b.customer.id = :customerId AND b.draftMode = false")
	Long bookingCostCount(int customerId);

	@Query("select avg(b.price.amount) from Booking b where b.customer.id = :customerId AND b.draftMode = false")
	Double bookingCostAverage(int customerId);

	@Query("select min(b.price.amount) from Booking b where b.customer.id = :customerId AND b.draftMode = false")
	Double bookingCostMinimum(int customerId);

	@Query("select max(b.price.amount) from Booking b where b.customer.id = :customerId AND b.draftMode = false")
	Double bookingCostMaximum(int customerId);

	@Query("select stddev(b.price.amount) from Booking b where b.customer.id = :customerId AND b.draftMode = false")
	Double bookingCostStandardDeviation(int customerId);

	@Query("select count(br.passenger) from BookingRecord br join br.booking b " + "where b.customer.id = :customerId AND b.draftMode = false")
	Long passengerCount(int customerId);

	@Query(value = "SELECT AVG(passenger_count) FROM (" + "SELECT COUNT(br.passenger_id) as passenger_count FROM booking_record br " + "JOIN booking b ON br.booking_id = b.id " + "WHERE b.customer_id = :customerId AND b.draft_mode = false "
		+ "GROUP BY br.booking_id) AS counts", nativeQuery = true)
	Double passengerAverage(int customerId);

	@Query(value = "SELECT MIN(passenger_count) FROM (" + "SELECT COUNT(br.passenger_id) as passenger_count FROM booking_record br " + "JOIN booking b ON br.booking_id = b.id " + "WHERE b.customer_id = :customerId AND b.draft_mode = false "
		+ "GROUP BY br.booking_id) AS counts", nativeQuery = true)
	Long passengerMinimum(int customerId);

	@Query(value = "SELECT MAX(passenger_count) FROM (" + "SELECT COUNT(br.passenger_id) as passenger_count FROM booking_record br " + "JOIN booking b ON br.booking_id = b.id " + "WHERE b.customer_id = :customerId AND b.draft_mode = false "
		+ "GROUP BY br.booking_id) AS counts", nativeQuery = true)
	Long passengerMaximum(int customerId);

	@Query(value = "SELECT STDDEV(passenger_count) FROM (" + "SELECT COUNT(br.passenger_id) as passenger_count FROM booking_record br " + "JOIN booking b ON br.booking_id = b.id " + "WHERE b.customer_id = :customerId AND b.draft_mode = false "
		+ "GROUP BY br.booking_id) AS counts", nativeQuery = true)
	Double passengerStandardDeviation(int customerId);
}
