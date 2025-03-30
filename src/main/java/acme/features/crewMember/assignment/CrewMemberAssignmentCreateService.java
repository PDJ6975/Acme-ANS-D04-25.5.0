
package acme.features.crewMember.assignment;

import java.util.Collection;

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
import acme.realms.members.AvailabilityStatus;
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
		newAssignment.setDraftMode(true); // por defecto establecemos la asignación como borrador
		newAssignment.setLeg(leg);

		// Ajustamos status y actualización por defecto

		newAssignment.setAssignmentStatus(AssignmentStatus.PENDING);
		newAssignment.setLastUpdated(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(newAssignment);

	}

	@Override
	public void bind(final FlightAssignment assignment) {

		int crewMemberId;
		FlightCrewMember crewMember;

		crewMemberId = super.getRequest().getData("employeeCode", int.class);
		crewMember = this.repository.findCrewMemberById(crewMemberId);

		super.bindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments");

		assignment.setFlightCrewMember(crewMember);

	}

	@Override
	public void validate(final FlightAssignment assignment) {

		Leg leg = assignment.getLeg();
		FlightCrewMember member = assignment.getFlightCrewMember();
		CrewRole role = assignment.getCrewRole();

		super.state(member != null, "employeeCode", "crewMember.assignment.error.missing-member");

		if (member != null) {

			// 1. No se puede asignar a un miembro que ya tenga asignación en esa etapa -> ya se comprueba en la entidad, pero para reforzar
			boolean duplicateAssignment = this.repository.existsAssignmentForLegAndCrewMember(leg.getId(), member.getId());
			super.state(!duplicateAssignment, "employeeCode", "crewMember.assignment.error.duplicate-assignment");

			// 2. Solo un piloto y un copiloto por etapa
			if (role == CrewRole.PILOT || role == CrewRole.COPILOT) {
				boolean roleAlreadyAssigned = this.repository.existsAssignmentForLegWithRole(leg.getId(), role);
				super.state(!roleAlreadyAssigned, "crewRole", "crewMember.assignment.error.duplicate-role");
			}

			// 3. El miembro debe estar disponible
			boolean isAvailable = member.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE;
			super.state(isAvailable, "employeeCode", "crewMember.assignment.error.not-available");

			// 4. No se puede crear asignación en una etapa que ya ha ocurrido
			boolean legNotOccurred = leg.getLegStatus() != LegStatus.LANDED && leg.getLegStatus() != LegStatus.CANCELLED;
			super.state(legNotOccurred, "*", "crewMember.assignment.error.leg-occurred");

			// 5. El miembro no puede estar asignado a otra etapa simultáneamente
			boolean isOverlapping = this.repository.existsOverlappingAssignment(member.getId(), leg.getScheduledDeparture(), leg.getScheduledArrival());
			super.state(!isOverlapping, "employeeCode", "crewMember.assignment.error.overlapping");
		}
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

		dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "leg.flightNumber", "leg.departureAirport.name", "leg.arrivalAirport.name", "leg.scheduledDeparture", "leg.scheduledArrival");

		dataset.put("crewMembers", choicesMembers);
		dataset.put("employeeCode", choicesMembers.getSelected().getKey());
		dataset.put("crewRoles", choicesCrewRol);
		dataset.put("assignmentStatuses", choicesAssignmentStatus);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
