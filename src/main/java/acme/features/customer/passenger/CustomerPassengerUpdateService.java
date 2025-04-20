
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	protected CustomerPassengerRepository	repository;

	@Autowired
	private SpamDetector					spamDetector;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		Collection<BookingRecord> records = this.repository.findBookingRecordsByPassengerId(passengerId);

		boolean status = passenger != null && passenger.isDraftMode() && !records.isEmpty() && super.getRequest().getPrincipal().hasRealm(records.iterator().next().getBooking().getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		if (!super.getBuffer().getErrors().hasErrors("passportNumber"))
			super.state(passenger.getPassportNumber().matches("^[A-Z0-9]{6,9}$"), "passportNumber", "customer.passenger.error.invalid-passport");

		if (!super.getBuffer().getErrors().hasErrors("fullName")) {
			boolean isSpamFn = this.spamDetector.isSpam(passenger.getFullName());
			super.state(!isSpamFn, "fullName", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("specialNeeds")) {
			boolean isSpamSn = this.spamDetector.isSpam(passenger.getSpecialNeeds());
			super.state(!isSpamSn, "specialNeeds", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("email")) {
			boolean isSpamEmail = this.spamDetector.isSpam(passenger.getEmail());
			super.state(!isSpamEmail, "email", "customer.passenger.error.spam");
		}
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		int passengerId = passenger.getId();
		Collection<BookingRecord> records = this.repository.findBookingRecordsByPassengerId(passengerId);
		if (!records.isEmpty()) {
			BookingRecord record = records.iterator().next();
			dataset.put("masterId", record.getBooking().getId());
		}

		super.getResponse().addData(dataset);
	}
}
