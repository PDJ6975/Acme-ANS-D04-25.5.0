
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

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
	private AuthenticatedCustomerRepository	repository;

	@Autowired
	private SpamDetector					spamDetector;


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
		object.setEarnedPoints(0);

		// Generar identificador con las iniciales del username
		String username = userAccount.getUsername();
		String initials = this.generateInitials(username);
		// Generar 6 dígitos aleatorios
		String digits = String.format("%06d", (int) (Math.random() * 1000000));

		// Combinar para crear el identificador
		String identifier = initials + digits;
		object.setIdentifier(identifier);

		super.getBuffer().addData(object);
	}

	// Método auxiliar para extraer las iniciales
	private String generateInitials(String username) {
		if (username == null || username.isEmpty())
			return "XX"; // Valor por defecto

		// Convertir a mayúsculas
		username = username.toUpperCase();

		// Extraer las primeras 2-3 letras, asegurando que sean solo letras (A-Z)
		StringBuilder initials = new StringBuilder();
		for (char c : username.toCharArray()) {
			if (Character.isLetter(c) && initials.length() < 3)
				initials.append(c);
			if (initials.length() >= 2)
				break; // Mínimo 2 letras
		}

		// Si no se encontraron suficientes letras, añadir X hasta tener 2
		while (initials.length() < 2)
			initials.append('X');

		return initials.toString();
	}

	@Override
	public void bind(final Customer object) {
		assert object != null;

		// Eliminamos "identifier" de aquí para que no sea modificable
		super.bindObject(object, "phoneNumber", "address", "city", "country");
	}

	@Override
	public void validate(final Customer object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("identifier")) {
			super.state(object.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$"), "identifier", "authenticated.customer.form.error.invalid-identifier");

			if (!super.getBuffer().getErrors().hasErrors("identifier")) {
				boolean isUnique = !this.repository.existsByIdentifier(object.getIdentifier());
				super.state(isUnique, "identifier", "authenticated.customer.form.error.duplicate-identifier");
			}
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

		// Seguimos incluyendo "identifier" para que se muestre en la vista
		dataset = super.unbindObject(object, "identifier", "phoneNumber", "address", "city", "country");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		PrincipalHelper.handleUpdate();
	}
}
