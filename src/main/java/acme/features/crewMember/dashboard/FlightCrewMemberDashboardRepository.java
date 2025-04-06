
package acme.features.crewMember.dashboard;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.members.FlightCrewMember;

@Repository
public interface FlightCrewMemberDashboardRepository extends AbstractRepository {

	@Query("""
		    SELECT DISTINCT a.leg.arrivalAirport.city
		    FROM FlightAssignment a
		    WHERE a.flightCrewMember.id = :id
		          AND a.draftMode = false
		          AND a.assignmentStatus = 'CONFIRMED'
		          AND a.leg.draftMode = false
		          AND a.leg.legStatus IN ('ON_TIME','DELAYED','LANDED')
		    ORDER BY a.leg.scheduledArrival DESC
		""")
	Page<String> findDestinations(int id, Pageable pageable);

	@Query("""
		    SELECT
		        CASE
		            WHEN l.severityLevel <= 3 THEN '0-3'
		            WHEN l.severityLevel <= 7 THEN '4-7'
		            ELSE '8-10'
		        END AS range,
		        COUNT(l)
		    FROM ActivityLog l
		    WHERE l.flightAssignment.flightCrewMember.id = :id
		          AND l.draftMode = false
		          AND l.flightAssignment.draftMode = false
		          AND l.flightAssignment.assignmentStatus = 'CONFIRMED'
		          AND l.flightAssignment.leg.draftMode = false
		          AND l.flightAssignment.leg.legStatus = 'LANDED'
		          AND l.flightAssignment.leg.scheduledArrival < CURRENT_TIMESTAMP
		    GROUP BY
		        CASE
		            WHEN l.severityLevel <= 3 THEN '0-3'
		            WHEN l.severityLevel <= 7 THEN '4-7'
		            ELSE '8-10'
		        END
		""")
	List<Object[]> countLogsBySeverityRanges(int id);

	@Query("""
		    SELECT a.leg.id
		    FROM FlightAssignment a
		    WHERE a.flightCrewMember.id = :id
		      AND a.draftMode = false
		      AND a.assignmentStatus = 'CONFIRMED'
		      AND a.leg.draftMode = false
		      AND a.leg.legStatus = 'LANDED'
		      AND a.leg.scheduledArrival < CURRENT_TIMESTAMP
		    ORDER BY a.leg.scheduledArrival DESC
		""")
	List<Integer> findLastLegIdByCrewMember(int id);

	@Query("""
		    SELECT DISTINCT fa.flightCrewMember
		    FROM FlightAssignment fa
		    WHERE fa.leg.id = :legId
		      AND fa.flightCrewMember.id <> :crewMemberId
		      AND fa.draftMode = false
		      AND fa.assignmentStatus = 'CONFIRMED'
		      AND fa.leg.draftMode = false
		      AND fa.leg.legStatus = 'LANDED'
		      AND fa.leg.scheduledArrival < CURRENT_TIMESTAMP
		""")
	List<FlightCrewMember> findColleaguesByLegId(int legId, int crewMemberId);

	@Query("""
		    SELECT a.assignmentStatus, a
		    FROM FlightAssignment a
		    WHERE a.flightCrewMember.id = :id
		      AND a.draftMode = false
		      AND a.leg.draftMode = false
		      AND a.leg.legStatus != 'CANCELLED'
		      AND a.leg.scheduledArrival < CURRENT_TIMESTAMP
		""")
	List<Object[]> findAssignmentsByStatus(int id);

	@Query("""
		    SELECT FUNCTION('DATE', a.lastUpdated) AS day, COUNT(a)
		    FROM FlightAssignment a
		    WHERE a.flightCrewMember.id = :id
		      AND a.draftMode = false
		      AND a.assignmentStatus = 'CONFIRMED'
		      AND a.lastUpdated >= CURRENT_DATE - 30
		      AND a.leg.draftMode = false
		    GROUP BY FUNCTION('DATE', a.lastUpdated)
		    ORDER BY FUNCTION('DATE', a.lastUpdated)
		""")
	List<Object[]> findAssignmentsCountPerDayInLastMonth(int id);

}
