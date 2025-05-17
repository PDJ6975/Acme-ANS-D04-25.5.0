
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.entities.airports.OperationalScope;

@GuiService
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	protected AdministratorAirportRepository	repository;

	@Autowired
	private SpamDetector						spamDetector;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport = new Airport();

		airport.setName(null);
		airport.setIataCode(null);
		airport.setOperationalScope(null);
		airport.setCity(null);
		airport.setCountry(null);
		airport.setWebsite(null);
		airport.setEmailAddress(null);
		airport.setContactPhone(null);
		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "emailAddress", "contactPhone");

	}

	@Override
	public void validate(final Airport airport) {
		if (airport.getIataCode() != null) {
			boolean exists = this.repository.existsByIataCode(airport.getIataCode());
			super.state(!exists, "iataCode", "administrator.airport.error.duplicated-iataCode");

		}

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "administrator.airport.error.confirmation-required");

		if (!super.getBuffer().getErrors().hasErrors("name")) {
			boolean isSpamFn = this.spamDetector.isSpam(airport.getName());
			super.state(!isSpamFn, "name", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("city")) {
			boolean isSpamFn = this.spamDetector.isSpam(airport.getCity());
			super.state(!isSpamFn, "city", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("country")) {
			boolean isSpamFn = this.spamDetector.isSpam(airport.getCountry());
			super.state(!isSpamFn, "country", "customer.passenger.error.spam");
		}
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices operationalScopes = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		Dataset dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "emailAddress", "contactPhone");
		dataset.put("operationalScopes", operationalScopes);

		super.getResponse().addData(dataset);
	}
}
