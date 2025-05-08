
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository	repository;

	@Autowired
	private SpamDetector					spamDetector;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
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

		if (!super.getBuffer().getErrors().hasErrors("identifier")) {
			super.state(object.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"), "identifier", "authenticated.customer.form.error.invalid-identifier");

			boolean isUnique = !this.repository.existsByIdentifierAndNotId(object.getIdentifier(), object.getId());
			super.state(isUnique, "identifier", "authenticated.customer.form.error.duplicate-identifier");
		}

		if (!super.getBuffer().getErrors().hasErrors("phoneNumber"))
			super.state(object.getPhoneNumber().matches("^\\+?\\d{6,15}$"), "phoneNumber", "authenticated.customer.form.error.invalid-phone");

		if (!super.getBuffer().getErrors().hasErrors("address")) {
			boolean spamAddr = this.spamDetector.isSpam(object.getAddress());
			super.state(!spamAddr, "address", "authenticated.customer.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("city")) {
			boolean spamCity = this.spamDetector.isSpam(object.getCity());
			super.state(!spamCity, "city", "authenticated.customer.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("country")) {
			boolean spamCountry = this.spamDetector.isSpam(object.getCountry());
			super.state(!spamCountry, "country", "authenticated.customer.form.error.spam");
		}
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
