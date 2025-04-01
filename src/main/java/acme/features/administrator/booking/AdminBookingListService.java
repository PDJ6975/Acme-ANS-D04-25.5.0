
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;

@GuiService
public class AdminBookingListService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdminBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;

		bookings = this.repository.findPublishedBookings();

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "draftMode");
		dataset.put("customerFullName", booking.getCustomer().getUserAccount().getIdentity().getFullName());
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", booking.getId());
	}
}
