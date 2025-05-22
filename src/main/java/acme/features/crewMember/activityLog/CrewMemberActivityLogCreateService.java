
package acme.features.crewMember.activityLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected CrewMemberActivityLogRepository	repository;

	@Autowired
	private SpamDetector						spamDetector;


	@Override
	public void authorise() {
		boolean authorised = false;

		if (super.getRequest().hasData("masterId", int.class)) {
			int assignmentId = super.getRequest().getData("masterId", int.class);
			FlightAssignment assignment = this.repository.findAssignmentById(assignmentId);

			if (assignment != null) {
				// El vuelo debe haber comenzado
				boolean legStarted = !assignment.getLeg().getScheduledDeparture().after(MomentHelper.getCurrentMoment());

				// Entendemos que una asignación solo puede tener logs si: ella y la etapa son públicas y si la asignación está confirmada (para evitar incongruencias)

				authorised = super.getRequest().getPrincipal().hasRealm(assignment.getFlightCrewMember()) && assignment.getAssignmentStatus() == AssignmentStatus.CONFIRMED && !assignment.isDraftMode() && !assignment.getLeg().isDraftMode() && legStarted;
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {

		int masterId;
		FlightAssignment assignment;
		ActivityLog log;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findAssignmentById(masterId);

		log = new ActivityLog();
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setDraftMode(true);
		log.setFlightAssignment(assignment);

		super.getBuffer().addData(log);

	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog log) {

		Date now = MomentHelper.getCurrentMoment();

		// Protección adicional contra inconsistencias (duplicado defensivo con authorise)
		super.state(log.getFlightAssignment().getAssignmentStatus() == AssignmentStatus.CONFIRMED, "*", "crewMember.log.error.assignment.not-confirmed");
		super.state(!log.getFlightAssignment().isDraftMode(), "*", "crewMember.log.error.assignment.not-published");
		super.state(!log.getFlightAssignment().getLeg().isDraftMode(), "*", "crewMember.log.error.leg.not-published");

		if (!super.getBuffer().getErrors().hasErrors("typeOfIncident")) {
			boolean isSpamFn = this.spamDetector.isSpam(log.getTypeOfIncident());
			super.state(!isSpamFn, "typeOfIncident", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("description")) {
			boolean isSpamFn = this.spamDetector.isSpam(log.getDescription());
			super.state(!isSpamFn, "description", "customer.passenger.error.spam");
		}
		Date departure = log.getFlightAssignment().getLeg().getScheduledDeparture();
		super.state(!departure.after(now), "*", "crewMember.log.error.leg.not-started");
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		dataset.put("validDraft", !log.getFlightAssignment().isDraftMode() && !log.getFlightAssignment().getLeg().isDraftMode());
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
