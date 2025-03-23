
package acme.features.crewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;

@Repository
public interface CrewMemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :id")
	ActivityLog findOneById(int id);

	@Query("""
		    SELECT al
		    FROM ActivityLog al
		    WHERE al.flightAssignment.flightCrewMember.id = :crewMemberId
		    ORDER BY al.registrationMoment DESC
		""")
	Collection<ActivityLog> findManyByCrewMemberId(int crewMemberId);

}
