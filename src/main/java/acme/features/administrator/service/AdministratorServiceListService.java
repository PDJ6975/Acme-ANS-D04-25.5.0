
package acme.features.administrator.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.services.Service;

@GuiService
public class AdministratorServiceListService extends AbstractGuiService<Administrator, Service> {

	@Autowired
	protected AdministratorServiceRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Service> services = this.repository.findAllServices();
		super.getBuffer().addData(services);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset = super.unbindObject(service, "name", "averageDwellTime", "airport.iataCode");
		super.getResponse().addData(dataset);
	}
}
