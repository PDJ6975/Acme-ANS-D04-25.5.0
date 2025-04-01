
package acme.features.assistantAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	protected AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int claimId;
		Claim claim;
		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		auth = claim.getDraftMode() && claim.getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		Claim claim;
		int id;
		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		;
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> logs;

		logs = this.repository.findTrackingLogsByClaimId(claim.getId());
		this.repository.deleteAll(logs);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		;
	}
}
