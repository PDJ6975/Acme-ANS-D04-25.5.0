
package acme.features.any.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AnyServiceShowService extends AbstractGuiService<Any, Service> {

	@Autowired
	private AnyServiceRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Service service = this.repository.findAllPublishedServices(id);
		super.getBuffer().addData(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "link", "averageDwellTime", "promotionCode", "discountMoney", "airport.name");

		super.getResponse().addData(dataset);
	}

}
