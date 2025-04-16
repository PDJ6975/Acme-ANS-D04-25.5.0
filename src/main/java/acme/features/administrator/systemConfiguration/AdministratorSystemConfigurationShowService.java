
package acme.features.administrator.systemConfiguration;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfigurations.SystemConfiguration;

@GuiService
public class AdministratorSystemConfigurationShowService extends AbstractGuiService<Administrator, SystemConfiguration> {

	@Autowired
	protected AdministratorSystemConfigurationRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SystemConfiguration systemCurrency = this.repository.findSystemConfiguration();
		super.getBuffer().addData(systemCurrency);
	}

	@Override
	public void unbind(final SystemConfiguration systemCurrency) {
		Dataset dataset = super.unbindObject(systemCurrency, "actualCurrency", "validCurrencies");
		dataset.put("masterId", systemCurrency.getId());
		super.getResponse().addData(dataset);
	}
}
