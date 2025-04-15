
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
import acme.realms.members.AvailabilityStatus;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected CrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean isAuthorised = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		if (super.getRequest().hasData("leg", int.class)) {
			int legId = super.getRequest().getData("leg", int.class);
			Collection<Leg> availableLegs = this.repository.findAllLegsAvailableForAssignment(MomentHelper.getCurrentMoment());
			boolean legIsAvailable = availableLegs.stream().anyMatch(l -> l.getId() == legId);

			isAuthorised = isAuthorised && legIsAvailable;
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {

		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember crewMember = this.repository.findCrewMemberById(crewMemberId);

		FlightAssignment assignment = new FlightAssignment();
		assignment.setDraftMode(true);
		assignment.setAssignmentStatus(AssignmentStatus.PENDING);
		assignment.setLastUpdated(MomentHelper.getCurrentMoment());
		assignment.setFlightCrewMember(crewMember);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		super.bindObject(assignment, "crewRole", "comments");
		assignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Leg leg = assignment.getLeg();
		FlightCrewMember member = assignment.getFlightCrewMember();
		CrewRole role = assignment.getCrewRole();

		super.state(leg != null, "leg", "crewMember.assignment.error.missing-leg");
		super.state(member != null, "*", "crewMember.assignment.error.missing-member");

		if (leg != null && member != null) {

			// 1. Evitar duplicados
			boolean duplicateAssignment = this.repository.existsPublishedAssignmentForLegAndCrewMember(leg.getId(), member.getId());
			super.state(!duplicateAssignment, "leg", "crewMember.assignment.error.duplicate-assignment");

			// 2. Solo un piloto y copiloto por etapa
			if (role == CrewRole.PILOT || role == CrewRole.COPILOT || role == CrewRole.LEADATTENDANT) {
				boolean roleAlreadyAssigned = this.repository.existsPublishedAssignmentForLegWithRole(leg.getId(), role);
				super.state(!roleAlreadyAssigned, "crewRole", "crewMember.assignment.error.duplicate-role");
			}

			// 3. El miembro debe estar disponible
			boolean isAvailable = member.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE;
			super.state(isAvailable, "*", "crewMember.assignment.error.not-available");

			// 4. La etapa debe estar publicada y no haber ocurrido aún
			boolean legNotOccurred = !leg.isDraftMode() && leg.getScheduledArrival().after(MomentHelper.getCurrentMoment());
			super.state(legNotOccurred, "leg", "crewMember.assignment.error.leg-occurred");

			// 5. No debe haber solapamiento
			boolean isOverlapping = this.repository.existsOverlappingAssignment(member.getId(), leg.getScheduledDeparture(), leg.getScheduledArrival());
			super.state(!isOverlapping, "*", "crewMember.assignment.error.overlapping");
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> availableLegs = this.repository.findAllLegsAvailableForAssignment(MomentHelper.getCurrentMoment());
		SelectChoices choicesLegs = SelectChoices.from(availableLegs, "flightNumber", assignment.getLeg());

		SelectChoices choicesCrewRol = SelectChoices.from(CrewRole.class, assignment.getCrewRole());

		Dataset dataset = super.unbindObject(assignment, "crewRole", "lastUpdated", "assignmentStatus", "comments", "flightCrewMember.employeeCode");

		dataset.put("legs", choicesLegs);
		dataset.put("leg", choicesLegs.getSelected().getKey());
		dataset.put("crewRoles", choicesCrewRol);

		super.getResponse().addData(dataset);
	}

}
