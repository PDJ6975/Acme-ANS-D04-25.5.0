
package acme.features.customer.booking;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	protected CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCardNibble", "draftMode");
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		if (booking.getFlight() != null) {
			Flight flight = booking.getFlight();
			String tag = flight.getTag();
			String shortTag = tag.length() > 20 ? tag.substring(0, 17) + "..." : tag;

			String departureDateStr = "";
			if (flight.getScheduledDeparture() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				departureDateStr = sdf.format(flight.getScheduledDeparture());
			}

			String destination = flight.getDestinationCity() != null ? flight.getDestinationCity() : "N/A";
			String flightInfo = String.format("%s | %s | %s", shortTag, departureDateStr, destination);

			dataset.put("flightInfo", flightInfo);
		} else
			dataset.put("flightInfo", "---");
		dataset.put("travelClasses", travelClasses);
		dataset.put("masterId", booking.getId());

		super.getResponse().addData(dataset);
	}

}
