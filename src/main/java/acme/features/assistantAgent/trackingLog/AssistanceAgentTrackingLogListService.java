
package acme.features.assistantAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<TrackingLog> logs;
		int id;
		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		logs = this.repository.findTrackingLogsByAssistanceAgentId(id);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "id", "step", "resolutionPercentage");

		super.getResponse().addData(dataset);
	}

}
