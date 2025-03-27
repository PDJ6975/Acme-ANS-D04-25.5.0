
package acme.features.assistantAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;

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
		WHERE c.id = :id
		""")
	Claim findClaimById(int id);
}
