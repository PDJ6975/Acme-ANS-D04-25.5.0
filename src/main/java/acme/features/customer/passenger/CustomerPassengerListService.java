
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	protected CustomerPassengerRepository repo;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int masterId = super.getRequest().getData("masterId", int.class);

		Booking booking = this.repo.findBookingById(masterId);

		boolean draftMode = booking != null && booking.isDraftMode();

		super.getResponse().addGlobal("draftMode", draftMode);

		Collection<Passenger> passengers = this.repo.findPassengersByBookingId(masterId);
		super.getBuffer().addData(passengers);
		super.getResponse().addGlobal("masterId", masterId);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		int masterId = super.getRequest().getData("masterId", int.class);
		dataset.put("masterId", masterId);
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", masterId);
	}
}
