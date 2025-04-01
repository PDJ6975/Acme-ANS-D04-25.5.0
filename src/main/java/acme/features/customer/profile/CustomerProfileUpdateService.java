
package acme.features.customer.profile;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class CustomerProfileUpdateService extends AbstractGuiService<Customer, Customer> {

	@Autowired
	private CustomerProfileRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Customer object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Customer object) {
		assert object != null;

		super.bindObject(object, "identifier", "phoneNumber", "address", "city", "country");
	}

	@Override
	public void validate(final Customer object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("identifier"))
			super.state(object.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"), "identifier", "customer.customer.form.error.invalid-identifier");

		if (!super.getBuffer().getErrors().hasErrors("phoneNumber"))
			super.state(object.getPhoneNumber().matches("^\\+?\\d{6,15}$"), "phoneNumber", "customer.customer.form.error.invalid-phone");
	}

	@Override
	public void perform(final Customer object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Customer object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "identifier", "phoneNumber", "address", "city", "country");

		super.getResponse().addData(dataset);
	}
}
