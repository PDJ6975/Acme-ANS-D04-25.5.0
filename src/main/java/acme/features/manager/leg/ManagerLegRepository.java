
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("""
			SELECT l
			FROM Leg l
			WHERE l.flight.id = :masterId
			ORDER BY l.scheduledDeparture ASC
		""")
	Collection<Leg> findLegsSortedMomentByFlightId(int masterId);

	@Query("""
			SELECT l
			FROM Leg l
			WHERE l.id = :id
		""")
	Leg findLegById(int id);

}
