
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
import acme.entities.systemConfigurations.SystemCurrency;
import acme.features.administrator.systemConfiguration.AdministratorSystemConfigurationRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	protected CustomerBookingRepository					repository;

	@Autowired
	private AdministratorSystemConfigurationRepository	systemConfigRepository;


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

		super.bindObject(booking, "locatorCode", "travelClass", "price", "creditCardNibble");
	}

	@Override
	public void validate(final Booking booking) {
		assert booking != null;

		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {

			super.state(booking.getLocatorCode().matches("^[A-Z0-9]{6,8}$"), "locatorCode", "customer.booking.form.error.invalid-locator");

			boolean isUnique = !this.repository.existsByLocatorCodeAndNotId(booking.getLocatorCode(), booking.getId());
			super.state(isUnique, "locatorCode", "customer.booking.form.error.duplicate-locator");
		}

		if (!super.getBuffer().getErrors().hasErrors("price") && booking.getPrice() != null) {
			String currency = booking.getPrice().getCurrency();
			SystemCurrency systemConfig = this.systemConfigRepository.findSystemConfiguration();

			if (systemConfig != null) {
				boolean validCurrency = systemConfig.getValidCurrencies().contains(currency);
				super.state(validCurrency, "price", "customer.booking.form.error.invalid-currency");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("creditCardNibble") && booking.getCreditCardNibble() != null && !booking.getCreditCardNibble().isEmpty())
			super.state(booking.getCreditCardNibble().matches("^\\d{4}$"), "creditCardNibble", "customer.booking.form.error.invalid-credit-card");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "creditCardNibble", "draftMode");
		dataset.put("masterId", booking.getId());

		if (booking.getFlight() != null) {
			Flight flight = booking.getFlight();
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
			String flightInfo = String.format("%s | %s | %s", shortTag, departureDateStr, destination);

			dataset.put("flightInfo", flightInfo);
		} else
			dataset.put("flightInfo", "---");

		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset.put("travelClasses", travelClasses);

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
