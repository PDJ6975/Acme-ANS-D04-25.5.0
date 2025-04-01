
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAirlineShowService extends AbstractGuiService<Administrator, Airline> {

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
	public void unbind(final Airline airline) {
		Dataset dataset;
		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "emailAddress", "phoneNumber");
		dataset.put("masterId", airline.getId());
		super.getResponse().addData(dataset);
	}
}
