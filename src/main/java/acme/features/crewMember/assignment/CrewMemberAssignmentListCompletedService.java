
package acme.features.crewMember.assignment;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberAssignmentListCompletedService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewMemberAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Collection<FlightAssignment> assignments;
		int flightCrewMemberId;

		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assignments = this.repository.findAssignmentsCompletedByMemberId(flightCrewMemberId);

		Collection<FlightAssignment> completed = assignments.stream().filter(fa -> fa.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment())).collect(Collectors.toList());

		super.getBuffer().addData(completed);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "leg.flightNumber", "crewRole", "leg.departureAirport.name", "leg.arrivalAirport.name");

		super.getResponse().addData(dataset);
	}
}
