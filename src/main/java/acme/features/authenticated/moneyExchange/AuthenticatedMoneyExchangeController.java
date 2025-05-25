
package acme.features.authenticated.moneyExchange;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.ExchangeMoney;

@GuiController
public class AuthenticatedMoneyExchangeController extends AbstractGuiController<Authenticated, ExchangeMoney> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedMoneyExchangePerformService exchangeService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("perform", this.exchangeService);
	}

}
