
package acme.features.assistantAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("""
		SELECT t
		FROM TrackingLog t
		WHERE t.claim.id = :claimId
		""")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("""
		SELECT t
		FROM TrackingLog t
		WHERE t.id = :id
		""")
	TrackingLog findTrackingLogById(int id);

	@Query("""
		SELECT c
		FROM Claim c
		WHERE c.id = :id
		""")
	Claim findClaimById(int id);

	@Query("""
		    SELECT tl
		    FROM TrackingLog tl
		    WHERE tl.claim.id = :claimId
		      AND tl.draftMode = false
		      AND tl.resolutionPercentage = (
		          SELECT MAX(t2.resolutionPercentage)
		          FROM TrackingLog t2
		          WHERE t2.claim.id = :claimId AND t2.draftMode = false
		      )
		""")
	TrackingLog findOneWithHighestResolutionByClaimId(int claimId);

	@Query("""
		    SELECT tl
		    FROM TrackingLog tl
		    WHERE tl.claim.id = :claimId
		      AND tl.draftMode = false
		      AND tl.resolutionPercentage = (
		          SELECT MAX(t2.resolutionPercentage)
		          FROM TrackingLog t2
		          WHERE t2.claim.id = :claimId AND t2.draftMode = false
		      )
		""")
	Collection<TrackingLog> findHighestResolutionLogsByClaimId(int claimId);

}
