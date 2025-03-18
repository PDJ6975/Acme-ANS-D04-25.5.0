
package acme.features.crewMember.assignment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.flightCrewMember.id = :memberId
		          AND fa.leg.legStatus = acme.entities.LegStatus.LANDED
		          AND fa.assignmentStatus = acme.entities.AssignmentStatus.CONFIRMED
		    ORDER BY fa.leg.scheduledArrival DESC
		""")
	List<FlightAssignment> findAssignmentsCompletedByMemberId(int memberId);

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.flightCrewMember.id = :memberId
		          AND (fa.leg.legStatus = acme.entities.LegStatus.ON_TIME
		          OR fa.leg.legStatus = acme.entities.LegStatus.DELAYED)
		          AND fa.assignmentStatus != acme.entities.AssignmentStatus.CANCELLED
		    ORDER BY fa.leg.scheduledArrival ASC
		""")
	List<FlightAssignment> findAssignmentsPlannedByMemberId(int memberId);

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.id = :assignmentId
		          AND fa.flightCrewMember.id = :memberId
		""")
	FlightAssignment findAssignmentByIdAndMemberId(int assignmentId, int memberId);

}
