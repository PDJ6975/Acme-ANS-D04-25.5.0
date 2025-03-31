
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	protected CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);

		boolean status = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(passenger.getBooking().getCustomer());

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
		if (!super.getBuffer().getErrors().hasErrors("fullName"))
			super.state(!passenger.getFullName().isEmpty(), "fullName", "customer.passenger.error.empty-fullname");

		if (!super.getBuffer().getErrors().hasErrors("email"))
			super.state(!passenger.getEmail().isEmpty(), "email", "customer.passenger.error.empty-email");

		if (!super.getBuffer().getErrors().hasErrors("passportNumber"))
			super.state(passenger.getPassportNumber().matches("^[A-Z0-9]{6,9}$"), "passportNumber", "customer.passenger.error.invalid-passport");
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		dataset.put("masterId", passenger.getBooking().getId());

		super.getResponse().addData(dataset);
	}
}
