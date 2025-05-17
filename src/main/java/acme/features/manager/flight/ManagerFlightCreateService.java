
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

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
	protected ManagerFlightRepository	repository;

	@Autowired
	private SpamDetector				spamDetector;


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
			Leg firstLeg = legs.getFirst();
			Leg lastLeg = legs.getLast();

			flight.setScheduledDeparture(firstLeg.getScheduledDeparture());
			flight.setScheduledArrival(lastLeg.getScheduledArrival());
			flight.setOriginCity(firstLeg.getDepartureAirport().getCity());
			flight.setDestinationCity(lastLeg.getArrivalAirport().getCity());

			flight.setLayovers(legs.size() - 1);

		} else {
			flight.setScheduledDeparture(null);
			flight.setScheduledArrival(null);
			flight.setOriginCity("Este vuelo no tiene etapas");
			flight.setDestinationCity("Este vuelo no tiene etapas");
			flight.setLayovers(null);
		}

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode", "airline");
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		flight.setManager(manager);
	}

	@Override
	public void validate(final Flight flight) {
		super.state(flight.getAirline() != null, "iataCode", "administrator.flight.error.null-airline");

		if (!super.getBuffer().getErrors().hasErrors("description")) {
			boolean isSpamFn = this.spamDetector.isSpam(flight.getDescription());
			super.state(!isSpamFn, "description", "customer.passenger.error.spam");
		}
	}

	@Override
	public void perform(final Flight flight) {

		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("manager.id", flight.getManager().getId());
		dataset.put("masterId", flight.getId());

		Collection<Airline> availableAirlines = this.repository.findAllAirlines();
		SelectChoices airlines = SelectChoices.from(availableAirlines, "iataCode", flight.getAirline());
		dataset.put("airlines", airlines);
		dataset.put("airline", airlines.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
