
package acme.features.administrator.authenticated.crewMember;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.members.FlightCrewMember;

@GuiController
public class AuthenticatedFlightCrewMemberController extends AbstractGuiController<Authenticated, FlightCrewMember> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedFlightCrewMemberCreateService createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
	}
}
