
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.managers.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	protected ManagerLegRepository repository;


	@Override
	public void authorise() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);

		boolean status = leg != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg);
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
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
