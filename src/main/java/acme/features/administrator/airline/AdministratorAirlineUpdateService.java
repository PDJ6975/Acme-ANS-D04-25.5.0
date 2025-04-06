
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	protected AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Airline airline;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

		status = airline != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airline airline = this.repository.findAirlineById(id);
		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		// Confirmación de actualización
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "administrator.airline.form.error.confirm");

		// Validar que no haya otro con mismo IATA (excepto este mismo)
		if (!super.getBuffer().getErrors().hasErrors("iataCode")) {
			Airline existing = this.repository.findByIataCode(airline.getIataCode());
			super.state(existing == null || existing.getId() == airline.getId(), //
				"iataCode", "administrator.airline.form.error.duplicated-iata");
		}
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");

		super.getResponse().addData(dataset);
	}
}
