
package acme.features.customer.profile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Customer;

@GuiController
public class CustomerProfileController extends AbstractGuiController<Customer, Customer> {

	@Autowired
	private CustomerProfileUpdateService updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("update", this.updateService);
	}
}
