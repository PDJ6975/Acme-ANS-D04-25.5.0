
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	protected AdministratorAircraftRepository	repository;

	@Autowired
	private SpamDetector						spamDetector;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft = new Aircraft();

		aircraft.setAircraftStatus(AircraftStatus.ACTIVE);
		aircraft.setModel("");
		aircraft.setRegistrationNumber("");
		aircraft.setCapacity(1);
		aircraft.setCargoWeight(2000.00);
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "details");

		int airlineId = super.getRequest().getData("iataCode", int.class);
		Airline airline = this.repository.findAirlineById(airlineId);
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {

		super.state(aircraft.getAirline() != null, "iataCode", "administrator.aircraft.error.null-airline");

		if (aircraft.getRegistrationNumber() != null) {
			boolean exists = this.repository.existsByRegistrationNumber(aircraft.getRegistrationNumber());
			super.state(!exists, "registrationNumber", "administrator.aircraft.error.duplicated-registration-number");
		}

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "administrator.aircraft.error.confirmation-required");

		if (!super.getBuffer().getErrors().hasErrors("model")) {
			boolean isSpamFn = this.spamDetector.isSpam(aircraft.getModel());
			super.state(!isSpamFn, "model", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("registrationNumber")) {
			boolean isSpamFn = this.spamDetector.isSpam(aircraft.getRegistrationNumber());
			super.state(!isSpamFn, "registrationNumber", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("details")) {
			boolean isSpamFn = this.spamDetector.isSpam(aircraft.getDetails());
			super.state(!isSpamFn, "details", "customer.passenger.error.spam");
		}
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Collection<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices choicesAirlines = SelectChoices.from(airlines, "iataCode", aircraft.getAirline());
		SelectChoices choicesStatus = SelectChoices.from(AircraftStatus.class, aircraft.getAircraftStatus());

		Dataset dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "details");
		dataset.put("airlines", choicesAirlines);
		dataset.put("airlineId", choicesAirlines.getSelected().getKey());
		dataset.put("aircraftStatuses", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
