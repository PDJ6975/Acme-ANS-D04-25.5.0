
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	protected CustomerPassengerRepository	repository;

	@Autowired
	private SpamDetector					spamDetector;


	@Override
	public void authorise() {
		int masterId = super.getRequest().getData("masterId", int.class);
		Booking booking = this.repository.findBookingById(masterId);

		boolean status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Passenger passenger = new Passenger();
		passenger.setFullName("");
		passenger.setEmail("");
		passenger.setPassportNumber("");
		passenger.setDateOfBirth(null);
		passenger.setSpecialNeeds("");
		passenger.setDraftMode(true);

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

		int bookingId = super.getRequest().getData("masterId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		BookingRecord record = new BookingRecord();
		record.setBooking(booking);
		record.setPassenger(passenger);
		this.repository.save(record);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		int masterId = super.getRequest().getData("masterId", int.class);
		dataset.put("masterId", masterId);

		super.getResponse().addData(dataset);
	}
}
