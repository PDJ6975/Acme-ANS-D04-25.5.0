
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
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	protected ManagerFlightRepository repository;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findOneById(flightId);

		List<Leg> legs = this.repository.findLegsByFlightId(flightId);
		boolean minimumLegs = legs.size() > 0;
		boolean legsDraftMode = legs.stream().allMatch(leg -> !leg.isDraftMode());

		boolean status = flight != null && minimumLegs && legsDraftMode && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findOneById(flightId);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight);
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers", "draftMode", "airline");

		dataset.put("manager.id", flight.getManager().getId());

		Collection<Airline> availableAirlines = this.repository.findAllAirlines();
		SelectChoices airlines = SelectChoices.from(availableAirlines, "name", flight.getAirline());
		dataset.put("airlines", airlines);

		super.getResponse().addData(dataset);
	}

}
