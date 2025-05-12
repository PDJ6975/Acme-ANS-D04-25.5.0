
package acme.features.crewMember.assignment;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.CrewRole;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewMemberAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int assignmentId;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(assignmentId);

		status = assignment != null && super.getRequest().getPrincipal().hasRealm(assignment.getFlightCrewMember());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		FlightAssignment assignment;
		int assignmentId;

		assignmentId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(assignmentId);

		super.getBuffer().addData(assignment);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		Date now = MomentHelper.getCurrentMoment();
		boolean legInProgressOrCompleted = assignment.getLeg().getScheduledDeparture().before(now);
		SelectChoices choicesCrewRole = SelectChoices.from(CrewRole.class, assignment.getCrewRole());
		SelectChoices choicesAssignmentStatus = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "leg.flightNumber", "flightCrewMember.employeeCode");

		dataset.put("crewRoles", choicesCrewRole);
		dataset.put("assignmentStatuses", choicesAssignmentStatus);
		dataset.put("masterId", assignment.getId());

		dataset.put("draftMode", assignment.isDraftMode());

		// ¿puede ver logs esta asignación?

		boolean hasLog = assignment.getAssignmentStatus() == AssignmentStatus.CONFIRMED && !assignment.isDraftMode() && !assignment.getLeg().isDraftMode() && legInProgressOrCompleted;

		dataset.put("hasLog", hasLog);

		super.getResponse().addData(dataset);
	}
}
