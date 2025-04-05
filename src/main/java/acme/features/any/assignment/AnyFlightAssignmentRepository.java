
package acme.features.any.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.FlightAssignment;

@Repository
public interface AnyFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT a FROM FlightAssignment a WHERE a.draftMode = FALSE")
	Collection<FlightAssignment> findAllPublishedAssignments();

	@Query("SELECT a FROM FlightAssignment a WHERE a.id = :id")
	FlightAssignment findAssignmentById(int id);

}
