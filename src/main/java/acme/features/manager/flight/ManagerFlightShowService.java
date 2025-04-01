
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.realms.managers.Manager;

@GuiService
public class ManagerFlightShowService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findOneById(flightId);

		status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int flightId;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findOneById(flightId);

		super.getBuffer().addData("flight", flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers", "draftMode", "airline");
		dataset.put("masterId", flight.getId());

		Collection<Airline> availableAirlines = this.repository.findAllAirlines();
		SelectChoices airlines = SelectChoices.from(availableAirlines, "name", flight.getAirline());
		dataset.put("airlines", airlines);

		super.getResponse().addData(dataset);
	}

}
