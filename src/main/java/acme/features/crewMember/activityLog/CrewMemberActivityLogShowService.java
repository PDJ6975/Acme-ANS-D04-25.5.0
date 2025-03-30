
package acme.features.crewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private CrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int logId;
		ActivityLog log;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findOneById(logId);

		status = log != null && super.getRequest().getPrincipal().hasRealm(log.getFlightAssignment().getFlightCrewMember());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		ActivityLog log;
		int logId;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findOneById(logId);

		super.getBuffer().addData(log);

	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		dataset.put("draftMode", log.getFlightAssignment().isDraftMode());
		dataset.put("draftLeg", log.isDraftMode());
		dataset.put("masterId", log.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}
}
