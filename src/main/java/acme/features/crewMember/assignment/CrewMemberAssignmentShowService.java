
package acme.features.crewMember.assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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

		Dataset dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments");

		super.addPayload(dataset, assignment,
			// Campos del Leg a mostrar
			"leg.flightNumber", "leg.scheduledDeparture", "leg.scheduledArrival", "leg.legStatus", "leg.departureAirport", "leg.arrivalAirport",
			// Campos de flightCrewMember a mostrar
			"flightCrewMember.employeeCode", "flightCrewMember.phoneNumber", "flightCrewMember.languageSkills");

		super.getResponse().addData(dataset);
	}
}
