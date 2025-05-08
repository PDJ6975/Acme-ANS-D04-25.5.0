
package acme.features.crewMember.assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.CrewRole;
import acme.entities.assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findAssignmentById(id);

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
		Leg leg = assignment.getLeg();
		FlightCrewMember member = assignment.getFlightCrewMember();
		CrewRole role = assignment.getCrewRole();

		super.state(assignment.isDraftMode(), "*", "crewMember.assignment.error.not-editable");
		super.state(leg != null, "leg", "crewMember.assignment.error.missing-leg");
		super.state(member != null, "*", "crewMember.assignment.error.missing-member");

		if (leg != null && member != null)

			if (role == CrewRole.PILOT || role == CrewRole.COPILOT || role == CrewRole.LEADATTENDANT) {
				boolean roleAlreadyAssigned = this.repository.existsPublishedAssignmentForLegWithRole(leg.getId(), role);
				boolean isSame = roleAlreadyAssigned && this.repository.findAssignmentById(assignment.getId()).getCrewRole() == role;
				super.state(!roleAlreadyAssigned || isSame, "crewRole", "crewMember.assignment.error.duplicate-role");
			}

	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setLastUpdated(MomentHelper.getCurrentMoment());
		this.repository.save(assignment);
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

		dataset.put("draftMode", assignment.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
