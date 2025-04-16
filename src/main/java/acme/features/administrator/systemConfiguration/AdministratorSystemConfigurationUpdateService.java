
package acme.features.administrator.systemConfiguration;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfigurations.SystemConfiguration;

@GuiService
public class AdministratorSystemConfigurationUpdateService extends AbstractGuiService<Administrator, SystemConfiguration> {

	@Autowired
	protected AdministratorSystemConfigurationRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SystemConfiguration systemConfiguration = this.repository.findSystemConfiguration();
		super.getBuffer().addData(systemConfiguration);
	}

	@Override
	public void bind(final SystemConfiguration systemConfiguration) {
		super.bindObject(systemConfiguration, "actualCurrency", "validCurrencies");
	}

	@Override
	public void validate(final SystemConfiguration systemConfiguration) {

		String actualCurrency = systemConfiguration.getActualCurrency();

		if (actualCurrency != null) {
			String validCurrencies = systemConfiguration.getValidCurrencies();
			boolean validCurrency = validCurrencies.contains(actualCurrency);
			super.state(validCurrency, "actualCurrency", "administrator.system-configuration.error.invalid-currency");
		}
	}

	@Override
	public void perform(final SystemConfiguration systemConfiguration) {
		this.repository.save(systemConfiguration);
	}

	@Override
	public void unbind(final SystemConfiguration systemConfiguration) {
		Dataset dataset = super.unbindObject(systemConfiguration, "actualCurrency", "validCurrencies");

		super.getResponse().addData(dataset);
	}
}
