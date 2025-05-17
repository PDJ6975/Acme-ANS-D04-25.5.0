
package acme.features.administrator.airline;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.airlines.Type;

@GuiService
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	protected AdministratorAirlineRepository	repository;

	@Autowired
	private SpamDetector						spamDetector;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airline = new Airline();
		Date moment = MomentHelper.getCurrentMoment();

		airline.setType(Type.STANDARD);
		airline.setName("");
		airline.setEmailAddress("");
		airline.setIataCode("");
		airline.setWebsite("");
		airline.setFoundationMoment(moment);
		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		//Confirmación
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "administrator.airline.form.error.confirm");

		// Validación de unicidad del IATA code
		if (!super.getBuffer().getErrors().hasErrors("iataCode")) {
			Airline existing = this.repository.findByIataCode(airline.getIataCode());
			super.state(existing == null, "iataCode", "administrator.airline.form.error.duplicated-iata");
		}

		if (!super.getBuffer().getErrors().hasErrors("name")) {
			boolean isSpamFn = this.spamDetector.isSpam(airline.getName());
			super.state(!isSpamFn, "name", "airline.error.spam");
		}
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
		SelectChoices types = SelectChoices.from(Type.class, airline.getType());
		dataset.put("types", types);
		dataset.put("type", types.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
