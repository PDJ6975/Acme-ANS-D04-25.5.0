
package acme.features.customer.booking;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.systemConfigurations.SystemCurrency;
import acme.features.administrator.systemConfiguration.AdministratorSystemConfigurationRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	protected CustomerBookingRepository					repository;

	@Autowired
	private AdministratorSystemConfigurationRepository	systemConfigRepository;


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
		super.bindObject(booking, "locatorCode", "travelClass", "price", "creditCardNibble", "flight");
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		booking.setCustomer(customer);
	}

	@Override
	public void validate(final Booking booking) {

		Booking existing = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
		super.state(existing == null, "locatorCode", "customer.booking.form.error.duplicate-locator");
		super.state(booking.getFlight() != null, "flight", "customer.booking.form.error.flight-required");

		if (!super.getBuffer().getErrors().hasErrors("price") && booking.getPrice() != null) {
			String currency = booking.getPrice().getCurrency();
			SystemCurrency systemConfig = this.systemConfigRepository.findSystemConfiguration();

			if (systemConfig != null) {
				boolean validCurrency = systemConfig.getValidCurrencies().contains(currency);
				super.state(validCurrency, "price", "customer.booking.form.error.invalid-currency");
			}
		}

		if (booking.getFlight() != null) {
			Flight flight = booking.getFlight();

			super.state(!flight.isDraftMode(), "flight", "customer.booking.form.error.flight-draft");

			Date currentDate = MomentHelper.getCurrentMoment();
			Collection<Flight> availableFlights = this.repository.findAvailableFlights(currentDate);

			super.state(availableFlights.contains(flight), "flight", "customer.booking.form.error.flight-not-available");
		}

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

		Date currentDate = MomentHelper.getCurrentMoment();
		Collection<Flight> availableFlights = this.repository.findAvailableFlights(currentDate);

		SelectChoices flights = new SelectChoices();
		flights.add("0", "----", booking.getFlight() == null);

		for (Flight flight : availableFlights) {
			String tag = flight.getTag();
			String shortTag = tag.length() > 20 ? tag.substring(0, 17) + "..." : tag;

			Date scheduledDeparture = this.calculateScheduledDeparture(flight);
			String destinationCity = this.calculateDestinationCity(flight);

			String departureDateStr = "";
			if (scheduledDeparture != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				departureDateStr = sdf.format(scheduledDeparture);
			}

			String destination = destinationCity != null ? destinationCity : "N/A";
			String label = String.format("%s | %s | %s", shortTag, departureDateStr, destination);

			flights.add(Integer.toString(flight.getId()), label, flight.equals(booking.getFlight()));
		}

		dataset.put("flights", flights);
		super.getResponse().addData(dataset);
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

}
