
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerCreateService extends AbstractGuiService<Authenticated, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Customer();
		object.setUserAccount(userAccount);
		object.setEarnedPoints(0); // Valor inicial de puntos

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
			super.state(object.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"), "identifier", "authenticated.customer.form.error.invalid-identifier");

		if (!super.getBuffer().getErrors().hasErrors("phoneNumber"))
			super.state(object.getPhoneNumber().matches("^\\+?\\d{6,15}$"), "phoneNumber", "authenticated.customer.form.error.invalid-phone");
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

	@Override
	public void onSuccess() {
		PrincipalHelper.handleUpdate();
	}
}
