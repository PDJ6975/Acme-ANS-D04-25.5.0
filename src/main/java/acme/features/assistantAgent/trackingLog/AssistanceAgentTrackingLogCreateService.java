
package acme.features.assistantAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.Indicator;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	protected AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		auth = claim.getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
		;
	}

	@Override
	public void load() {
		TrackingLog log;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		log = new TrackingLog();
		log.setLastUpdatedMoment(MomentHelper.getCurrentMoment());
		log.setStep("");
		log.setResolutionPercentage(null);
		log.setIndicator(Indicator.ONGOING);
		log.setResolution("");
		log.setClaim(claim);
		log.setDraftMode(true);
		super.getBuffer().addData(log);

	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog log) {
		;
	}

	@Override
	public void perform(final TrackingLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		SelectChoices logIndicator = SelectChoices.from(Indicator.class, log.getIndicator());
		dataset.put("logIndicator", logIndicator);
		super.getResponse().addData(dataset);
	}
}
