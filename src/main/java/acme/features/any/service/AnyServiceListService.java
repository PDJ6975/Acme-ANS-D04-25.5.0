
package acme.features.any.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AnyServiceListService extends AbstractGuiService<Any, Service> {

	@Autowired
	protected AnyServiceRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Service> service;

		service = this.repository.findAllPublishedServices();
		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "link", "averageDwellTime", "airport.name");

		super.getResponse().addData(dataset);
	}
}
