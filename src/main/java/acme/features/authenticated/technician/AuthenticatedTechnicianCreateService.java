
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.technicians.Technician;

@GuiService
public class AuthenticatedTechnicianCreateService extends AbstractGuiService<Authenticated, Technician> {

	@Autowired
	private AuthenticatedTechnicianRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Technician();
		object.setUserAccount(userAccount);
		object.setCertifications("");

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Technician object) {
		assert object != null;

		super.bindObject(object, "licenseNumber", "phoneNumber", "specialisation", "annualHealthTest", "yearsOfExperience", "certifications");
	}

	@Override
	public void validate(final Technician object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("licenseNumber"))
			super.state(object.getLicenseNumber().matches("^[A-Z]{2,3}\\d{6}$"), "licenseNumber", "authenticated.technician.form.error.invalid-licenseNumber");

		if (!super.getBuffer().getErrors().hasErrors("phoneNumber"))
			super.state(object.getPhoneNumber().matches("^\\+?\\d{6,15}$"), "phoneNumber", "authenticated.technician.form.error.invalid-phone");
	}

	@Override
	public void perform(final Technician object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Technician object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "licenseNumber", "phoneNumber", "specialisation", "annualHealthTest", "yearsOfExperience", "certifications");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		PrincipalHelper.handleUpdate();
	}
}
