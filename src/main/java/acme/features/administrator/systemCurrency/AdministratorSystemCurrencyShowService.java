
package acme.features.administrator.systemCurrency;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfigurations.SystemCurrency;

@GuiService
public class AdministratorSystemCurrencyShowService extends AbstractGuiService<Administrator, SystemCurrency> {

	@Autowired
	protected AdministratorSystemCurrencyRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SystemCurrency systemCurrency = this.repository.findSystemConfiguration();
		super.getBuffer().addData(systemCurrency);
	}

	@Override
	public void unbind(final SystemCurrency systemCurrency) {
		Dataset dataset = super.unbindObject(systemCurrency, "actualCurrency", "validCurrencies");
		dataset.put("masterId", systemCurrency.getId());
		super.getResponse().addData(dataset);
	}
}
