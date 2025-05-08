
package acme.features.any.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.entities.airports.OperationalScope;

@GuiService
public class AnyAirportShowService extends AbstractGuiService<Any, Airport> {

	@Autowired
	protected AnyAirportRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Airport airport;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);

		status = airport != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Airport airport = this.repository.findAirportById(id);
		super.getBuffer().addData(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices operationalScopes;
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "emailAddress", "contactPhone");
		operationalScopes = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());
		dataset.put("operationalScopes", operationalScopes);
		dataset.put("masterId", airport.getId());
		super.getResponse().addData(dataset);
	}
}
