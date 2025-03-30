
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;

@GuiService
public class AdministratorAircraftListService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	protected AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();
		super.getBuffer().addData(aircrafts);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset = super.unbindObject(aircraft, "model", "registrationNumber", "aircraftStatus", "airline.name");
		super.getResponse().addData(dataset);
	}
}
