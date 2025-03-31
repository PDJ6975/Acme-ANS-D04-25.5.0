
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.realms.managers.Manager;

@GuiService
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	protected ManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		Flight flight = new Flight();
		flight.setTag("");
		flight.setSelfTransfer(false);
		flight.setCost(null);
		flight.setDescription(null);
		flight.setScheduledDeparture(null); //esto hay que cambiarlo para que lo coja de los legs
		flight.setScheduledArrival(null); //esto hay que cambiarlo para que lo coja de los legs
		flight.setOriginCity(null); //esto hay que cambiarlo para que lo coja de los legs
		flight.setDestinationCity(null); //esto hay que cambiarlo para que lo coja de los legs
		flight.setLayovers(null); //esto hay que cambiarlo para que lo coja de los legs
		flight.setDraftMode(true); // cuando se crea un Flight se pone el draftMode por defecto a true
		flight.setAirline(null);
		flight.setManager(manager);

		super.getBuffer().addData("flight", flight);  // Pasar el vuelo al buffer
	}

	@Override
	public void bind(final Flight flight) {

		String airlineName;
		Airline airline;

		airlineName = super.getRequest().getData("airline,name", String.class);
		airline = this.repository.findAirlineByName(airlineName);

		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers");
		flight.setAirline(airline);
		flight.setManager(manager);
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {

		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers", "draftMode", "manager.id");

		dataset.put("manager.id", flight.getManager().getId());
		dataset.put("masterId", flight.getId());

		super.getResponse().addData(dataset);  // Pasa solo los datos relevantes del vuelo
	}

}
