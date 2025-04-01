
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
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
		flight.setDraftMode(true);
		flight.setAirline(null);
		flight.setManager(manager);

		List<Leg> legs = this.repository.findLegsByFlightId(flight.getId());

		if (!legs.isEmpty()) {
			Leg firstLeg = legs.get(0);
			Leg lastLeg = legs.get(legs.size() - 1);

			flight.setScheduledDeparture(firstLeg.getScheduledDeparture());
			flight.setScheduledArrival(lastLeg.getScheduledArrival());
			flight.setOriginCity(firstLeg.getDepartureAirport().getCity());
			flight.setDestinationCity(lastLeg.getArrivalAirport().getCity());

			flight.setLayovers(legs.size() - 1);

		} else {
			flight.setScheduledDeparture(null);
			flight.setScheduledArrival(null);
			flight.setOriginCity(null);
			flight.setDestinationCity(null);
			flight.setLayovers(null);
		}

		super.getBuffer().addData("flight", flight);
	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers", "airline");
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
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
		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "airline", "layovers", "draftMode");
		dataset.put("manager.id", flight.getManager().getId());
		dataset.put("masterId", flight.getId());

		Collection<Airline> availableAirlines = this.repository.findAllAirlines();
		SelectChoices airlines = SelectChoices.from(availableAirlines, "name", flight.getAirline());
		dataset.put("airlines", airlines);

		super.getResponse().addData(dataset);
	}

}
