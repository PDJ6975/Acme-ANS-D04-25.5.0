
package acme.features.assistantAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.Indicator;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogServiceShow extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int trackingLogId;
		TrackingLog trackingLog;
		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		auth = trackingLog != null && trackingLog.getClaim().getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;
		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		dataset = super.unbindObject(trackingLog, "lastUpdatedMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		SelectChoices logIndicator = SelectChoices.from(Indicator.class, trackingLog.getIndicator());
		dataset.put("logIndicator", logIndicator);
		super.getResponse().addData(dataset);
	}

}
