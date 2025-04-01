
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.passengers.Passenger;

@GuiService
public class AdminBookingShowService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdminBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Booking booking;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		status = booking != null && !booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findBookingById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Booking booking) {
		assert booking != null;

		Dataset dataset;
		Collection<Passenger> passengers;
		SelectChoices travelClasses;

		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		passengers = this.repository.findPassengersByBookingId(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCardNibble", "draftMode");
		dataset.put("travelClasses", travelClasses);
		dataset.put("customerFullName", booking.getCustomer().getUserAccount().getIdentity().getFullName());
		dataset.put("passengers", passengers);
		dataset.put("passengerCount", passengers.size());
		dataset.put("masterId", booking.getId());

		super.getResponse().addData(dataset);
	}
}
