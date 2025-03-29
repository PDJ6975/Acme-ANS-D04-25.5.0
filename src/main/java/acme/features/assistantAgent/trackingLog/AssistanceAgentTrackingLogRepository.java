
package acme.features.assistantAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("""
		SELECT t
		FROM TrackingLog t
		WHERE t.claim.assistanceAgent.id = :agentId
		""")
	Collection<TrackingLog> findTrackingLogsByAssistanceAgentId(int agentId);

	@Query("""
		SELECT t
		FROM TrackingLog t
		WHERE t.id = :id
		""")
	TrackingLog findTrackingLogById(int id);
}
