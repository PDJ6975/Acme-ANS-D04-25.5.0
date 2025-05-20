
package acme.features.administrator.systemCurrency;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfigurations.SystemCurrency;

@GuiService
public class AdministratorSystemCurrencyUpdateService extends AbstractGuiService<Administrator, SystemCurrency> {

	@Autowired
	protected AdministratorSystemCurrencyRepository	repository;

	@Autowired
	private SpamDetector							spamDetector;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SystemCurrency systemConfiguration = this.repository.findSystemConfiguration();
		super.getBuffer().addData(systemConfiguration);
	}

	@Override
	public void bind(final SystemCurrency systemConfiguration) {
		super.bindObject(systemConfiguration, "actualCurrency", "validCurrencies");
	}

	@Override
	public void validate(final SystemCurrency systemConfiguration) {

		String actualCurrency = systemConfiguration.getActualCurrency();

		if (actualCurrency != null) {
			String validCurrencies = systemConfiguration.getValidCurrencies();
			boolean validCurrency = validCurrencies.contains(actualCurrency);
			super.state(validCurrency, "actualCurrency", "administrator.system-configuration.error.invalid-currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("actualCurrency")) {
			boolean isSpamFn = this.spamDetector.isSpam(systemConfiguration.getActualCurrency());
			super.state(!isSpamFn, "actualCurrency", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("validCurrencies")) {
			boolean isSpamFn = this.spamDetector.isSpam(systemConfiguration.getValidCurrencies());
			super.state(!isSpamFn, "validCurrencies", "customer.passenger.error.spam");
		}
	}

	@Override
	public void perform(final SystemCurrency systemConfiguration) {
		this.repository.save(systemConfiguration);
	}

	@Override
	public void unbind(final SystemCurrency systemConfiguration) {
		Dataset dataset = super.unbindObject(systemConfiguration, "actualCurrency", "validCurrencies");

		super.getResponse().addData(dataset);
	}
}
