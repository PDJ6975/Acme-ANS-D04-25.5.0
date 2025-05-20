
package acme.features.customer.booking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
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

	private Date calculateScheduledDeparture(final Flight flight) {
		List<Leg> legs = this.repository.findLegsByFlightIdOrderedByDeparture(flight.getId());

		if (legs == null || legs.isEmpty())
			return null;

		Leg firstLeg = legs.get(0);
		return firstLeg.getScheduledDeparture();
	}

	private String calculateDestinationCity(final Flight flight) {
		List<Leg> legs = this.repository.findLegsByFlightIdOrderedByArrival(flight.getId());

		if (legs == null || legs.isEmpty())
			return "No legs available";

		Leg lastLeg = legs.get(legs.size() - 1);

		if (lastLeg.getArrivalAirport() != null)
			return lastLeg.getArrivalAirport().getCity();
		else
			return "No arrival airport defined";
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "flight", "price", "creditCardNibble", "draftMode");
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		if (booking.getFlight() != null) {
			Flight flight = booking.getFlight();

			Date scheduledDeparture = this.calculateScheduledDeparture(flight);
			String destinationCity = this.calculateDestinationCity(flight);

			String tag = flight.getTag();
			String shortTag = tag.length() > 20 ? tag.substring(0, 17) + "..." : tag;

			String departureDateStr = "";
			if (scheduledDeparture != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				departureDateStr = sdf.format(scheduledDeparture);
			}

			String destination = destinationCity != null ? destinationCity : "N/A";
			String flightInfo = String.format("%s | %s | %s", shortTag, departureDateStr, destination);

			dataset.put("flightInfo", flightInfo);
		} else
			dataset.put("flightInfo", "---");
		dataset.put("travelClasses", travelClasses);
		dataset.put("masterId", booking.getId());

		super.getResponse().addData(dataset);
	}

}
