
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
import acme.entities.legs.LegStatus;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int id;
		FlightAssignment assignment;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(id);

		status = assignment != null && assignment.isDraftMode() && assignment.getCrewRole().equals(CrewRole.LEADATTENDANT) && super.getRequest().getPrincipal().hasRealm(assignment.getFlightCrewMember());

		super.getResponse().setAuthorised(status);

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
		CrewRole role = assignment.getCrewRole();

		// 1. Solo un piloto y un copiloto por etapa
		if (role == CrewRole.PILOT || role == CrewRole.COPILOT) {
			boolean roleAlreadyAssigned = this.repository.existsAssignmentForLegWithRole(leg.getId(), role);
			super.state(!roleAlreadyAssigned, "crewRole", "crewMember.assignment.error.duplicate-role");
		}

		// El resto de validaciones no son necesarias porque la etapa y el miembro asociado en la asignación no se puede modificar
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

		dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "assignmentStatus",//
			"leg.flightNumber", "leg.departureAirport.name", "leg.arrivalAirport.name", "leg.scheduledDeparture", "leg.scheduledArrival",//
			"flightCrewMember.employeeCode");

		// Tenemos que obtener las opciones de selección del rol de la asignación para el show

		choicesCrewRol = SelectChoices.from(CrewRole.class, assignment.getCrewRole());
		choicesAssignmentStatus = SelectChoices.from(AssignmentStatus.class, assignment.getAssignmentStatus());

		dataset.put("crewRoles", choicesCrewRol);
		dataset.put("assignmentStatuses", choicesAssignmentStatus);
		dataset.put("masterId", assignment.getId());

		boolean isLeadAttendant = assignment.getCrewRole() != null ? assignment.getCrewRole().equals(CrewRole.LEADATTENDANT) : true;
		boolean legNotOccurred = !assignment.getLeg().getLegStatus().equals(LegStatus.LANDED) && !assignment.getLeg().getLegStatus().equals(LegStatus.CANCELLED);

		dataset.put("canCreate", isLeadAttendant && legNotOccurred);
		dataset.put("draftMode", assignment.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
