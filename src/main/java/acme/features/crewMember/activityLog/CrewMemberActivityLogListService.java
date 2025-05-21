
package acme.features.crewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private CrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = false;

		if (super.getRequest().hasData("masterId", int.class)) {
			int assignmentId = super.getRequest().getData("masterId", int.class);
			FlightAssignment assignment = this.repository.findAssignmentById(assignmentId);

			if (assignment != null) {
				// El vuelo debe haber comenzado
				boolean legStarted = assignment.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());

				// Entendemos que una asignación solo puede tener logs si: ella y la etapa son públicas y si la asignación está confirmada (para evitar incongruencias)

				authorised = super.getRequest().getPrincipal().hasRealm(assignment.getFlightCrewMember()) && assignment.getAssignmentStatus() == AssignmentStatus.CONFIRMED && !assignment.isDraftMode() && !assignment.getLeg().isDraftMode() && legStarted;
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {

		Collection<ActivityLog> logs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		logs = this.repository.findLogsByMasterId(masterId);

		super.getBuffer().addData(logs);

	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "flightAssignment.leg.flightNumber", "flightAssignment.crewRole", "typeOfIncident", "severityLevel");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ActivityLog> logs) {
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		FlightAssignment assignment = this.repository.findAssignmentById(masterId);

		boolean canCreate = assignment.getAssignmentStatus() == AssignmentStatus.CONFIRMED && !assignment.isDraftMode() && !assignment.getLeg().isDraftMode() && !assignment.getLeg().getScheduledDeparture().after(MomentHelper.getCurrentMoment());

		super.getResponse().addGlobal("canCreate", canCreate);
		super.getResponse().addGlobal("masterId", masterId);
	}

}
