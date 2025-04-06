
package acme.features.assistantAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	protected AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int id;
		TrackingLog log;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLogById(id);

		auth = log.isDraftMode() && log.getClaim().getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
		;
	}

	@Override
	public void load() {
		TrackingLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		;
	}

	@Override
	public void validate(final TrackingLog log) {
		;
	}

	@Override
	public void perform(final TrackingLog log) {
		this.repository.delete(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		;
	}

}
