
package acme.features.crewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogUpdateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected CrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int logId;
		ActivityLog log;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findOneById(logId);

		status = log.isDraftMode() && super.getRequest().getPrincipal().hasRealm(log.getFlightAssignment().getFlightCrewMember());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		ActivityLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findOneById(id);

		super.getBuffer().addData(log);

	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		dataset.put("draftMode", log.getFlightAssignment().isDraftMode());
		dataset.put("draftLeg", log.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
