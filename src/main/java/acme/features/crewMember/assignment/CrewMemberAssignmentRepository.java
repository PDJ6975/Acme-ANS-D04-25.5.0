
package acme.features.crewMember.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.FlightAssignment;

@Repository
public interface CrewMemberAssignmentRepository extends AbstractRepository {

	// El compañero tiene los enumerados de forma ordinal (incluido el LegStatus) -> 0: ON_TIME, 1: DELAYED, 2: CANCELLED, 3: LANDED

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.flightCrewMember.id = :memberId
		          AND fa.leg.legStatus = 'LANDED'
		          AND fa.assignmentStatus = 'CONFIRMED'
		    ORDER BY fa.leg.scheduledArrival DESC
		""")
	Collection<FlightAssignment> findAssignmentsCompletedByMemberId(int memberId);

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.flightCrewMember.id = :memberId
		          AND (fa.leg.legStatus = 'ON_TIME'
		          OR fa.leg.legStatus = 'DELAYED')
		          AND fa.assignmentStatus != 'CANCELLED'
		    ORDER BY fa.leg.scheduledArrival ASC
		""")
	Collection<FlightAssignment> findAssignmentsPlannedByMemberId(int memberId);

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.id = :assignmentId
		""")
	FlightAssignment findAssignmentById(int assignmentId);

}
