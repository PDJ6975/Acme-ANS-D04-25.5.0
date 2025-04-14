
package acme.features.customer.recommendation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.recommendations.Recommendation;

@Repository
public interface CustomerRecommendationRepository extends AbstractRepository {

	@Query("""
		    SELECT DISTINCT r
		    FROM Recommendation r
		    JOIN r.airport a
		    WHERE a.id IN (
		        SELECT l.arrivalAirport.id
		        FROM Booking b
		        JOIN b.flight f
		        JOIN Leg l ON l.flight.id = f.id
		        WHERE b.customer.id = :customerId
		        AND b.draftMode = false
		        AND NOT EXISTS (
		            SELECT l2 FROM Leg l2
		            WHERE l2.flight.id = f.id
		            AND l2.scheduledDeparture > l.scheduledDeparture
		        )
		    )
		""")
	Collection<Recommendation> findByCustomerId(int customerId);

	@Query("SELECT r FROM Recommendation r WHERE r.id = :id")
	Recommendation findRecommendationById(int id);

	@Query("SELECT COUNT(r) > 0 FROM Recommendation r JOIN r.airport a " + "WHERE a.id = :airportId AND a.id IN (" + "    SELECT l.arrivalAirport.id " + "    FROM Booking b " + "    JOIN b.flight f " + "    JOIN Leg l ON l.flight.id = f.id "
		+ "    WHERE b.customer.id = :customerId " + "    AND b.draftMode = false)")
	boolean isDestinationAirportForCustomer(int airportId, int customerId);
}
