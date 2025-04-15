
package acme.features.crewMember.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.CrewRole;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {

		int id;
		FlightAssignment assignment;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(id);

		boolean isOwner = assignment != null && super.getRequest().getPrincipal().hasRealm(assignment.getFlightCrewMember());
		boolean isDraft = assignment != null && assignment.isDraftMode();

		super.getResponse().setAuthorised(isOwner && isDraft);

	}

	@Override
	public void load() {

		int id;
		FlightAssignment assignment;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(id);

		super.getBuffer().addData(assignment);

	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "crewRole", "assignmentStatus", "comments");

	}

	@Override
	public void validate(final FlightAssignment assignment) {
		super.state(assignment.isDraftMode(), "*", "crewMember.assignment.error.not-deletable");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> logs;

		logs = this.repository.findLogsByAssignmentId(assignment.getId());
		this.repository.deleteAll(logs);
		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {

		SelectChoices choicesCrewRol;
		SelectChoices choicesAssignmentStatus;
		Dataset dataset;

		dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "leg.flightNumber", "flightCrewMember.employeeCode");

		choicesCrewRol = SelectChoices.from(CrewRole.class, assignment.getCrewRole());
		choicesAssignmentStatus = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		dataset.put("crewRoles", choicesCrewRol);
		dataset.put("assignmentStatuses", choicesAssignmentStatus);
		dataset.put("masterId", assignment.getId());

		super.getResponse().addData(dataset);
	}

}
