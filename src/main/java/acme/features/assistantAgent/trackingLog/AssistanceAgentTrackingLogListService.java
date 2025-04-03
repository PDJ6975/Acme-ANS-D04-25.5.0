
package acme.features.assistantAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		auth = claim.getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		Collection<TrackingLog> logs;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		logs = this.repository.findTrackingLogsByClaimId(masterId);
		super.getResponse().addGlobal("masterId", masterId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "step", "resolutionPercentage", "indicator");

		super.getResponse().addData(dataset);
	}

}
