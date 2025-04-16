
package acme.features.administrator.bannedPassanger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passengers.BannedPassenger;

@GuiController
public class AdministratorBannedPassengerController extends AbstractGuiController<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerCurrentListService	listCurrentService;

	@Autowired
	private AdministratorBannedPassengerShowService			showService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("current-list", "list", this.listCurrentService);
		super.addBasicCommand("show", this.showService);

	}

}
