
package acme.features.crewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;

@Repository
public interface CrewMemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :id")
	ActivityLog findOneById(int id);

	@Query("""
		    SELECT log
		    FROM ActivityLog log
		    WHERE log.flightAssignment.id = :masterId
		""")
	Collection<ActivityLog> findLogsByMasterId(int masterId);

	@Query("SELECT a FROM FlightAssignment a WHERE a.id = :id")
	FlightAssignment findAssignmentById(int id);

}
