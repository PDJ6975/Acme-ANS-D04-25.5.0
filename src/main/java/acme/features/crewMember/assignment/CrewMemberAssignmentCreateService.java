
package acme.features.crewMember.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.CrewRole;
import acme.entities.assignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		FlightAssignment assignment;
		FlightCrewMember crewMember;
		CrewRole role;

		masterId = super.getRequest().getData("masterId", int.class);

		// Sacamos el miembro y su rol en la etapa

		assignment = this.repository.findAssignmentById(masterId);
		crewMember = assignment.getFlightCrewMember();
		role = assignment.getCrewRole();

		// El miembro debe ser el LEAD_ATTENDANT de la etapa de la que se quiere crear la asignación

		status = role.equals(CrewRole.LEADATTENDANT) && super.getRequest().getPrincipal().hasRealm(crewMember);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		int masterId;
		FlightAssignment assignment;
		Leg leg;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findAssignmentById(masterId);

		// Obtenemos la etapa a asociar a la asignación

		leg = assignment.getLeg();

		// Creamos la nueva asignación a la que le asociamos la etapa

		FlightAssignment newAssignment = new FlightAssignment();
		newAssignment.setLeg(leg);

	}

	@Override
	public void bind(final FlightAssignment assignment) {

		String employeeCode;
		FlightCrewMember crewMember;

		employeeCode = super.getRequest().getData("flightCrewMember", String.class);
		crewMember = this.repository.findOneByEmployeeCode(employeeCode);

		super.bindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "assignmentStatus");

		assignment.setFlightCrewMember(crewMember);

	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {

		Collection<FlightCrewMember> availableMembers;
		SelectChoices choicesMembers;
		SelectChoices choicesCrewRol;
		SelectChoices choicesAssignmentStatus;
		Dataset dataset;

		availableMembers = this.repository.findAllAvailableCrewMembers();

		choicesMembers = SelectChoices.from(availableMembers, "employeeCode", assignment.getFlightCrewMember());

		choicesCrewRol = SelectChoices.from(CrewRole.class, assignment.getCrewRole());
		choicesAssignmentStatus = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "assignmentStatus");

		dataset.put("crewMembers", choicesMembers);
		dataset.put("crewMember", choicesMembers.getSelected().getKey());
		dataset.put("crewRoles", choicesCrewRol);
		dataset.put("assignmentStatuses", choicesAssignmentStatus);

		super.getResponse().addData(dataset);
	}

}
