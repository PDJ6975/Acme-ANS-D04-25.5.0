
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	protected CustomerBookingRepository										repository;

	@Autowired
	protected acme.features.customer.passenger.CustomerPassengerRepository	passengerRepository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking);
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {

		Collection<Passenger> passengers = this.passengerRepository.findPassengersByBookingId(booking.getId());
		this.passengerRepository.deleteAll(passengers);

		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCardNibble", "draftMode");
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset.put("travelClasses", travelClasses);
		dataset.put("masterId", booking.getId());

		super.getResponse().addData(dataset);
	}
}
