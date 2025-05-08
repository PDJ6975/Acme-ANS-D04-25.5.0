
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.features.manager.flight.ManagerFlightRepository;
import acme.realms.managers.Manager;

@GuiService
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	protected ManagerLegRepository		repository;

	@Autowired
	protected ManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int flightId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.flightRepository.findOneById(flightId);

		Leg leg = new Leg();
		leg.setFlightNumber("");
		leg.setDuration(null);
		leg.setLegStatus(LegStatus.ON_TIME);
		leg.setDescription("");
		leg.setScheduledDeparture(null);
		leg.setScheduledArrival(null);
		leg.setDraftMode(true);
		leg.setDepartureAirport(null);
		leg.setArrivalAirport(null);
		leg.setAircraft(null);
		leg.setFlight(flight);

		super.getBuffer().addData("leg", leg);
	}

	@Override
	public void bind(final Leg leg) {

		super.bindObject(leg, "flightNumber", "duration", "legStatus", "description", "scheduledDeparture", "scheduledArrival", "draftMode", "departureAirport", "arrivalAirport", "aircraft");
		int flightId = super.getRequest().getData("masterId", int.class);
		Flight flight = this.flightRepository.findOneById(flightId);
		leg.setFlight(flight);
	}

	@Override
	public void validate(final Leg leg) {
		if (leg.getFlightNumber() != null) {
			boolean exists = this.repository.existsLegByFlightNumber(leg.getFlightNumber());
			super.state(!exists, "flightNumber", "manager.leg.error.duplicated-flightNumber");

		}

		super.state(leg.getDepartureAirport() != null, "departureAirport", "manager.leg.error.null-departureAirport");
		super.state(leg.getArrivalAirport() != null, "arrivalAirport", "manager.leg.error.null-arrivalAirport");
		super.state(leg.getAircraft() != null, "aircraft", "manager.leg.error.null-aircraft");
		super.state(leg.getFlight() != null, "flight", "manager.leg.error.null-flight");

		Flight flight = leg.getFlight();

		List<Leg> legs = this.repository.findLegsSortedMomentByFlightId(flight.getId());

		Leg firstLeg = legs.getFirst();
		Leg lastLeg = legs.getLast();

		if (legs.size() > 0) {
			boolean dateAfter = lastLeg.getScheduledArrival().before(leg.getScheduledDeparture());
			super.state(dateAfter, "scheduledDeparture", "manager.leg.error.scheduledDeparture");
			boolean airportConexion = lastLeg.getArrivalAirport().equals(leg.getDepartureAirport());
			super.state(!airportConexion, "departureAirport", "manager.leg.error.airportConexion");
		}

		boolean departureBeforeArrival = leg.getScheduledDeparture().before(leg.getScheduledArrival());
		super.state(departureBeforeArrival, "scheduledDeparture", "manager.leg.error.departureBeforeArrival");

		boolean arrivalAfterDeparture = leg.getScheduledArrival().after(leg.getScheduledDeparture());
		super.state(arrivalAfterDeparture, "scheduledArrival", "manager.leg.error.arrivalAfterDeparture");

		boolean equalAirport1 = leg.getDepartureAirport().equals(leg.getArrivalAirport());
		super.state(!equalAirport1, "departureAirport", "manager.leg.error.equalAirport");

		boolean equalAirport2 = leg.getDepartureAirport().equals(leg.getArrivalAirport());
		super.state(!equalAirport2, "arrivalAirport", "manager.leg.error.equalAirport");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		dataset = super.unbindObject(leg, "flightNumber", "duration", "legStatus", "description", "scheduledDeparture", "scheduledArrival", "draftMode", "departureAirport", "arrivalAirport", "aircraft");

		SelectChoices legStatuses = SelectChoices.from(LegStatus.class, leg.getLegStatus());
		dataset.put("legStatuses", legStatuses);

		Flight flight = leg.getFlight();
		dataset.put("masterId", flight.getId());

		Collection<Airport> availableAirports = this.repository.findAirports();
		SelectChoices departureAirports = SelectChoices.from(availableAirports, "name", leg.getDepartureAirport());
		dataset.put("departureAirports", departureAirports);

		SelectChoices arrivalAirports = SelectChoices.from(availableAirports, "name", leg.getArrivalAirport());
		dataset.put("arrivalAirports", arrivalAirports);

		Collection<Aircraft> availableAircrafts = this.repository.findAircrafts();
		SelectChoices aircrafts = SelectChoices.from(availableAircrafts, "registrationNumber", leg.getAircraft());
		dataset.put("aircrafts", aircrafts);

		super.getResponse().addData(dataset);
	}
}
