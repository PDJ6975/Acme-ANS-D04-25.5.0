
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	protected CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		Collection<BookingRecord> records = this.repository.findBookingRecordsByPassengerId(passengerId);

		boolean status = passenger != null && passenger.isDraftMode() && !records.isEmpty() && super.getRequest().getPrincipal().hasRealm(records.iterator().next().getBooking().getCustomer());

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger);
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {

		int passengerId = passenger.getId();
		Collection<BookingRecord> records = this.repository.findBookingRecordsByPassengerId(passengerId);
		this.repository.deleteAll(records);

		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		;
	}
}
