
package acme.features.crewMember.visas;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.visas.VisaRequirement;
import acme.realms.members.FlightCrewMember;

@GuiController
public class CrewMemberVisaRequirementController extends AbstractGuiController<FlightCrewMember, VisaRequirement> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewMemberVisaRequirementListService	listService;

	@Autowired
	private CrewMemberVisaRequirementShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
