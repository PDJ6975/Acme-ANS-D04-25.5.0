
package acme.features.administrator.systemCurrency;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.systemConfigurations.SystemCurrency;

@GuiController
public class AdministratorSystemCurrencyController extends AbstractGuiController<Administrator, SystemCurrency> {

	@Autowired
	private AdministratorSystemCurrencyShowService		showService;

	@Autowired
	private AdministratorSystemCurrencyUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
	}
}
