
package acme.features.crewMember.assignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.CrewRole;
import acme.entities.assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.members.FlightCrewMember;

@Repository
public interface CrewMemberAssignmentRepository extends AbstractRepository {

	// El compañero tiene los enumerados de forma ordinal (incluido el LegStatus) -> 0: ON_TIME, 1: DELAYED, 2: CANCELLED, 3: LANDED

	@Query("""
			SELECT fa
			FROM FlightAssignment fa
			WHERE fa.flightCrewMember.id = :memberId
			  AND fa.assignmentStatus = 'CONFIRMED'
			  AND fa.draftMode = false
			  AND fa.leg.draftMode = false
			ORDER BY fa.leg.scheduledArrival DESC
		""")
	Collection<FlightAssignment> findAssignmentsCompletedByMemberId(int memberId);

	@Query("""
			SELECT fa
			FROM FlightAssignment fa
			WHERE fa.flightCrewMember.id = :memberId
			  AND fa.leg.draftMode = false
			ORDER BY fa.leg.scheduledArrival ASC
		""")
	Collection<FlightAssignment> findAssignmentsPlannedByMemberId(int memberId);

	@Query("""
		    SELECT fa
		    FROM FlightAssignment fa
		    WHERE fa.id = :assignmentId
		""")
	FlightAssignment findAssignmentById(int assignmentId);

	@Query("""
		    SELECT log
		    FROM ActivityLog log
		    WHERE log.flightAssignment.id = :assignmentId
		""")
	Collection<ActivityLog> findLogsByAssignmentId(int assignmentId);

	@Query("""
		    SELECT f
		    FROM FlightCrewMember f
		    WHERE f.availabilityStatus = 'AVAILABLE'
		""")
	Collection<FlightCrewMember> findAllAvailableCrewMembers();

	@Query("SELECT cm FROM FlightCrewMember cm WHERE cm.id = :id")
	FlightCrewMember findCrewMemberById(int id);

	@Query("""
			SELECT COUNT(a) > 0
			FROM FlightAssignment a
			WHERE a.leg.id = :legId
			  AND a.flightCrewMember.id = :memberId
			  AND a.draftMode = false
			  AND a.leg.draftMode = false
		""")
	boolean existsPublishedAssignmentForLegAndCrewMember(int legId, int memberId);

	@Query("""
			SELECT COUNT(a) > 0
			FROM FlightAssignment a
			WHERE a.leg.id = :legId
			  AND a.crewRole = :role
			  AND a.draftMode = false
			  AND a.leg.draftMode = false
		""")
	boolean existsPublishedAssignmentForLegWithRole(int legId, CrewRole role);

	@Query("""
			SELECT COUNT(a) > 0
			FROM FlightAssignment a
			WHERE a.flightCrewMember.id = :memberId
			  AND a.draftMode = false
			  AND a.leg.draftMode = false
			  AND (
			      a.leg.scheduledDeparture <= :end AND a.leg.scheduledArrival >= :start
			  )
		""")
	boolean existsOverlappingAssignment(int memberId, Date start, Date end);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("""
			SELECT l
			FROM Leg l
			WHERE l.draftMode = false
			  AND l.scheduledArrival > :now
			ORDER BY l.scheduledDeparture ASC
		""")
	Collection<Leg> findAllLegsAvailableForAssignment(Date now);

	@Query("""
			SELECT a
			FROM FlightAssignment a
			WHERE a.leg.id = :legId AND a.crewRole = :role
		""")
	FlightAssignment findAssignmentByLegIdAndRole(int legId, CrewRole role);

}
