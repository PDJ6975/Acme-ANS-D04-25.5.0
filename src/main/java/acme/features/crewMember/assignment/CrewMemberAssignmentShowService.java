
package acme.features.crewMember.assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.CrewRole;
import acme.entities.assignments.FlightAssignment;
import acme.entities.legs.LegStatus;
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

		boolean isLeadAttendant = assignment.getCrewRole().equals(CrewRole.LEADATTENDANT);
		boolean legNotOccurred = !assignment.getLeg().getLegStatus().equals(LegStatus.LANDED) && !assignment.getLeg().getLegStatus().equals(LegStatus.CANCELLED);

		dataset.put("canCreate", isLeadAttendant && legNotOccurred);
		dataset.put("draftMode", assignment.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
