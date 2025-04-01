
package acme.features.assistantAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("""
		SELECT c
		FROM Claim c
		WHERE c.assistanceAgent.id = :id
		AND c.state != 'ONGOING'
		""")
	Collection<Claim> findCompletedClaims(int id);

	@Query("""
		SELECT c
		FROM Claim c
		WHERE c.assistanceAgent.id = :id
		AND c.state = 'ONGOING'
		""")
	Collection<Claim> findOngoingClaims(int id);

	@Query("""
		SELECT c
		FROM Claim c
		WHERE c.id = :id
		""")
	Claim findClaimById(int id);

	@Query("""
		SELECT l
		FROM Leg l
		WHERE l.flightNumber = :flightNumber
		""")
	Leg findLegByFlightNumber(String flightNumber);

	@Query("""
		SELECT u
		FROM UserAccount u
		WHERE u.username = :username
		""")
	UserAccount findUserAccountByUsername(String username);

	@Query("""
		SELECT a
		FROM AssistanceAgent a
		WHERE a.userAccount.username = :username
		""")
	AssistanceAgent findAgentByUsername(String username);

	@Query("""
		SELECT l
		from TrackingLog l
		WHERE l.claim.id = :id
		""")
	Collection<TrackingLog> findTrackingLogsByClaimId(int id);

}
