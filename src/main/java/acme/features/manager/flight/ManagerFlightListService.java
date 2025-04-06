
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.managers.Manager;

@GuiService
public class ManagerFlightListService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Collection<Flight> flights;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findManyByManagerId(managerId);

		for (Flight flight : flights) {
			List<Leg> legs = this.repository.findLegsByFlightId(flight.getId());

			if (!legs.isEmpty()) {
				flight.setScheduledDeparture(legs.getFirst().getScheduledDeparture());
				flight.setScheduledArrival(legs.getLast().getScheduledArrival());
				flight.setOriginCity(legs.getFirst().getDepartureAirport().getCity());
				flight.setDestinationCity(legs.getLast().getArrivalAirport().getCity());
				flight.setLayovers(legs.size() - 1);
			} else {
				flight.setScheduledDeparture(null);
				flight.setScheduledArrival(null);
				flight.setOriginCity("Este vuelo no tiene etapas");
				flight.setDestinationCity("Este vuelo no tiene etapas");
				flight.setLayovers(0);
			}
		}

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "cost", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity");

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", flight.getManager().getId());
	}
}
