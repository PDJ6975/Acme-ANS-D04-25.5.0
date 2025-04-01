
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	protected CustomerBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		Booking booking = new Booking();
		booking.setFlight(null);
		booking.setLocatorCode("");
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setTravelClass(TravelClass.ECONOMY);
		booking.setPrice(null);
		booking.setCreditCardNibble("");
		booking.setDraftMode(true);
		booking.setCustomer(customer);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCardNibble", "flight");
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		booking.setCustomer(customer);
	}

	@Override
	public void validate(final Booking booking) {

		Booking existing = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
		super.state(existing == null, "locatorCode", "customer.booking.form.error.duplicate-locator");
		super.state(booking.getFlight() != null, "flight", "customer.booking.form.error.flight-required");
	}

	@Override
	public void perform(final Booking booking) {

		this.repository.save(booking);

	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCardNibble", "draftMode", "flight");
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset.put("travelClasses", travelClasses);
		dataset.put("masterId", booking.getId());

		Collection<Flight> availableFlights = this.repository.findAvailableFlights();
		SelectChoices flights = SelectChoices.from(availableFlights, "tag", booking.getFlight());
		dataset.put("flights", flights);

		super.getResponse().addData(dataset);
	}

}
