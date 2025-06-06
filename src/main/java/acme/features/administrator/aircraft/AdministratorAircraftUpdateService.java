
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	protected AdministratorAircraftRepository	repository;

	@Autowired
	private SpamDetector						spamDetector;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Aircraft aircraft;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		status = aircraft != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Aircraft aircraft = this.repository.findAircraftById(id);
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "details");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		if (aircraft.getRegistrationNumber() != null) {
			Aircraft existing = this.repository.findAircraftByRegistrationNumber(aircraft.getRegistrationNumber());
			boolean isDuplicate = existing != null && existing.getId() != aircraft.getId();

			super.state(!isDuplicate, "registrationNumber", "administrator.aircraft.error.duplicated-registration-number");
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
		Dataset dataset;

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "details", "airline.iataCode");
		dataset.put("disable", aircraft.getAircraftStatus().equals(AircraftStatus.MAINTENANCE));

		super.getResponse().addData(dataset);
	}
}
