
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.Leg;
import acme.realms.managers.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Collection<Leg> legs;
		int masterId = super.getRequest().getData("masterId", int.class);

		legs = this.repository.findLegsSortedMomentByFlightId(masterId);
		super.getBuffer().addData(legs);
		super.getResponse().addGlobal("masterId", masterId);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "duration", "legStatus", "scheduledDeparture", "scheduledArrival", "flight.tag");

		super.getResponse().addData(dataset);
	}

}
