
package acme.features.crewMember.activityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.members.FlightCrewMember;

@GuiController
public class CrewMemberActivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewMemberActivityLogListService	listService;

	@Autowired
	private CrewMemberActivityLogShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
