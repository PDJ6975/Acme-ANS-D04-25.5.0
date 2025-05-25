
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

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
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	protected ManagerLegRepository		repository;

	@Autowired
	protected ManagerFlightRepository	flightRepository;

	@Autowired
	private SpamDetector				spamDetector;


	@Override
	public void authorise() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		boolean status = leg != null && super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.getBuffer().addData("leg", leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "duration", "legStatus", "description", "scheduledDeparture", "scheduledArrival", "departureAirport", "arrivalAirport", "aircraft");

		int flightId = leg.getFlight().getId(); // No hace falta volver a cargar si ya viene cargado
		Flight flight = this.flightRepository.findOneById(flightId);
		leg.setFlight(flight);
	}

	@Override
	public void validate(final Leg leg) {
		boolean draftMode = leg.isDraftMode();
		super.state(draftMode, "draftMode", "manager.leg.error.draftMode");

		if (leg.getFlightNumber() != null) {
			boolean exists = this.repository.existsOtherLegByFlightNumber(leg.getFlightNumber(), leg.getId());
			super.state(!exists, "flightNumber", "manager.leg.error.duplicated-flightNumber");
		}

		super.state(leg.getDepartureAirport() != null, "departureAirport", "manager.leg.error.null-departureAirport");
		super.state(leg.getArrivalAirport() != null, "arrivalAirport", "manager.leg.error.null-arrivalAirport");
		super.state(leg.getAircraft() != null, "aircraft", "manager.leg.error.null-aircraft");
		super.state(leg.getFlight() != null, "flight", "manager.leg.error.null-flight");
		super.state(leg.getLegStatus() != null, "legStatus", "manager.leg.error.null-legStatus");

		super.state(leg.getDuration() != null, "duration", "manager.leg.error.null-duration");
		if (leg.getDuration() != null)
			super.state(leg.getDuration() > 0, "duration", "manager.leg.error.invalid-duration");

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			boolean departureBeforeArrival = leg.getScheduledDeparture().before(leg.getScheduledArrival());
			super.state(departureBeforeArrival, "scheduledDeparture", "manager.leg.error.departureBeforeArrival");

			boolean arrivalAfterDeparture = leg.getScheduledArrival().after(leg.getScheduledDeparture());
			super.state(arrivalAfterDeparture, "scheduledArrival", "manager.leg.error.arrivalAfterDeparture");
		}

		if (leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
			boolean equalAirport = leg.getDepartureAirport().equals(leg.getArrivalAirport());
			super.state(!equalAirport, "departureAirport", "manager.leg.error.equalAirport");
			super.state(!equalAirport, "arrivalAirport", "manager.leg.error.equalAirport");
		}

		if (leg.getScheduledDeparture() != null && leg.getFlight() != null) {
			List<Leg> legs = this.repository.findLegsSortedMomentByFlightId(leg.getFlight().getId());

			int index = -1;
			for (int i = 0; i < legs.size(); i++)
				if (legs.get(i).getId() == leg.getId()) {
					index = i;
					break;
				}

			if (index != -1) {
				if (index > 0) {
					Leg prevLeg = legs.get(index - 1);

					if (prevLeg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
						boolean dateAfter = prevLeg.getScheduledArrival().before(leg.getScheduledDeparture());
						super.state(dateAfter, "scheduledDeparture", "manager.leg.error.scheduledDeparture");
					}

					if (prevLeg.getArrivalAirport() != null && leg.getDepartureAirport() != null) {
						boolean airportConexion = prevLeg.getArrivalAirport().equals(leg.getDepartureAirport());
						super.state(airportConexion, "departureAirport", "manager.leg.error.airportConexion");
					}
				}

				if (index < legs.size() - 1) {
					Leg nextLeg = legs.get(index + 1);

					if (leg.getScheduledArrival() != null && nextLeg.getScheduledDeparture() != null) {
						boolean dateBefore = leg.getScheduledArrival().before(nextLeg.getScheduledDeparture());
						super.state(dateBefore, "scheduledArrival", "manager.leg.error.scheduledArrival");
					}

					if (leg.getArrivalAirport() != null && nextLeg.getDepartureAirport() != null) {
						boolean airportConexion = leg.getArrivalAirport().equals(nextLeg.getDepartureAirport());
						super.state(airportConexion, "arrivalAirport", "manager.leg.error.airportConexion");
					}
				}
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("description")) {
			boolean isSpamFn = this.spamDetector.isSpam(leg.getDescription());
			super.state(!isSpamFn, "description", "customer.passenger.error.spam");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "duration", "legStatus", "description", "scheduledDeparture", "scheduledArrival", "draftMode", "departureAirport", "arrivalAirport", "aircraft");

		SelectChoices legStatuses = SelectChoices.from(LegStatus.class, leg.getLegStatus());
		dataset.put("legStatuses", legStatuses);

		dataset.put("masterId", leg.getFlight().getId());

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
