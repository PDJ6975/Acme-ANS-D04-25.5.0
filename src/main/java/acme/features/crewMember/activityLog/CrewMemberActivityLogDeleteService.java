
package acme.features.crewMember.activityLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.AssignmentStatus;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected CrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = false;
		int logId;
		ActivityLog log;

		if (super.getRequest().hasData("id", int.class)) {
			logId = super.getRequest().getData("id", int.class);
			log = this.repository.findOneById(logId);

			// El vuelo debe haber comenzado
			boolean legStarted = log.getFlightAssignment().getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());

			authorised = log != null && log.isDraftMode() && super.getRequest().getPrincipal().hasRealm(log.getFlightAssignment().getFlightCrewMember()) && log.getFlightAssignment().getAssignmentStatus() == AssignmentStatus.CONFIRMED
				&& !log.getFlightAssignment().isDraftMode() && !log.getFlightAssignment().getLeg().isDraftMode() && legStarted;
		}

		super.getResponse().setAuthorised(authorised);
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
		super.state(log.isDraftMode(), "*", "crewMember.log.error.already-published");

		Date now = MomentHelper.getCurrentMoment();
		Date departure = log.getFlightAssignment().getLeg().getScheduledDeparture();
		super.state(departure.before(now), "*", "crewMember.log.error.leg.not-started");
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.delete(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		dataset.put("validDraft", log.isDraftMode() && !log.getFlightAssignment().isDraftMode() && !log.getFlightAssignment().getLeg().isDraftMode());
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
