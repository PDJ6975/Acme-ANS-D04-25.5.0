
package acme.features.administrator.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AdministratorServiceDeleteService extends AbstractGuiService<Administrator, Service> {

	@Autowired
	protected AdministratorServiceRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Service service = this.repository.findServiceById(id);

		boolean status = service != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Service service = this.repository.findServiceById(id);
		super.getBuffer().addData(service);
	}

	@Override
	public void bind(final Service service) {
		super.bindObject(service, "name", "link", "averageDwellTime", "promotionCode", "discountMoney");
	}

	@Override
	public void validate(final Service service) {
		;
	}

	@Override
	public void perform(final Service service) {
		this.repository.delete(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset = super.unbindObject(service, "name", "link", "averageDwellTime", "promotionCode", "discountMoney");
		dataset.put("airport.iataCode", service.getAirport().getIataCode());
		dataset.put("masterId", service.getId());

		super.getResponse().addData(dataset);
	}
}
