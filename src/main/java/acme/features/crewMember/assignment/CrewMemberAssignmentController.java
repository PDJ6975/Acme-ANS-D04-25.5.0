
package acme.features.crewMember.assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiController
public class CrewMemberAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewMemberAssignmentListCompletedService	listCompletedService;

	@Autowired
	private CrewMemberAssignmentListPlannedService		listPlannedService;

	@Autowired
	private CrewMemberAssignmentShowService				showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
	}
}
