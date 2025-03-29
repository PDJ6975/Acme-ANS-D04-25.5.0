
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;

@GuiService
public class AdministratorAircraftShowService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	protected AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Aircraft aircraft;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		status = aircraft != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Aircraft aircraft = this.repository.findAircraftById(id);
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choicesStatus;
		Dataset dataset;

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "aircraftStatus", "details", "airline.name", "airline.iataCode");
		choicesStatus = SelectChoices.from(AircraftStatus.class, aircraft.getAircraftStatus());
		dataset.put("aircraftStatuses", choicesStatus);
		dataset.put("masterId", aircraft.getId());

		super.getResponse().addData(dataset);
	}
}
