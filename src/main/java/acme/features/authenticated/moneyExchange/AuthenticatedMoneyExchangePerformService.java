
package acme.features.authenticated.moneyExchange;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.ExchangeRate;
import acme.forms.ExchangeMoney;

@GuiService
public class AuthenticatedMoneyExchangePerformService extends AbstractGuiService<Authenticated, ExchangeMoney> {

	// AbstractGuiService interface -------------------------------------------

	@Value("${myapp.exchange.key}")
	private String apiKey;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ExchangeMoney exchange;

		exchange = new ExchangeMoney();

		super.getBuffer().addData(exchange);
	}

	@Override
	public void bind(final ExchangeMoney exchange) {
		super.bindObject(exchange, "source", "targetCurrency");
	}

	@Override
	public void validate(final ExchangeMoney exchange) {
		;
	}

	@Override
	public void perform(final ExchangeMoney exchange) {
		Money source;
		String targetCurrency;
		ExchangeMoney current;

		source = super.getRequest().getData("source", Money.class);
		targetCurrency = super.getRequest().getData("targetCurrency", String.class);
		current = this.computeMoneyExchange(source, targetCurrency);
		super.state(current.getOops() == null, "*", "authenticated.money-exchange.form.label.api-error");
		if (current.getOops() != null) {
			exchange.setTarget(null);
			exchange.setMoment(null);
		} else {
			exchange.setMoment(current.getMoment());
			exchange.setTarget(current.getTarget());
		}
	}

	@Override
	public void unbind(final ExchangeMoney exchange) {
		Dataset dataset;

		dataset = super.unbindObject(exchange, "source", "targetCurrency", "moment", "target");

		super.getResponse().addData(dataset);
	}

	// Ancillary methods ------------------------------------------------------

	protected ExchangeMoney computeMoneyExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		ExchangeMoney result;

		if (SpringHelper.isRunningOn("development"))
			result = this.computeLiveExchange(source, targetCurrency);
		else
			result = this.computeMockedExchange(source, targetCurrency);

		return result;
	}

	protected ExchangeMoney computeMockedExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		ExchangeMoney result;
		Date currentMoment;
		Money target;

		currentMoment = MomentHelper.getCurrentMoment();
		target = new Money();
		target.setAmount(source.getAmount());
		target.setCurrency(targetCurrency);

		result = new ExchangeMoney();
		result.setMoment(currentMoment);
		result.setSource(source);
		result.setTarget(target);
		result.setTargetCurrency(targetCurrency);

		return result;
	}

	protected ExchangeMoney computeLiveExchange(final Money source, final String targetCurrency) {
		assert source != null;
		assert !StringHelper.isBlank(targetCurrency);

		ExchangeMoney result;
		RestTemplate api;
		HttpHeaders headers;
		HttpEntity<String> parameters;
		ResponseEntity<ExchangeRate> response;
		ExchangeRate record;
		String sourceCurrency;
		Double sourceAmount, targetAmount, rate;
		Money target;
		Date moment;

		try {
			sourceCurrency = source.getCurrency();
			sourceAmount = source.getAmount();

			headers = new HttpHeaders();
			headers.add("apikey", this.apiKey);
			parameters = new HttpEntity<String>("parameters", headers);
			api = new RestTemplate();
			response = api.exchange( //				
				"https://api.apilayer.com/exchangerates_data/latest?base={0}&symbols={1}", //
				HttpMethod.GET, //
				parameters, //
				ExchangeRate.class, //
				sourceCurrency, //
				targetCurrency //
			);
			assert response != null && response.getBody() != null;
			record = response.getBody();
			assert record != null && record.getRates().containsKey(targetCurrency);
			rate = record.getRates().get(targetCurrency);

			targetAmount = rate * sourceAmount;

			target = new Money();
			target.setAmount(targetAmount);
			target.setCurrency(targetCurrency);

			moment = record.getDate();

			result = new ExchangeMoney();
			result.setSource(source);
			result.setTargetCurrency(targetCurrency);
			result.setMoment(moment);
			result.setTarget(target);

			MomentHelper.sleep(1000); // HINT: need to pause the requests to the API to prevent banning!
		} catch (final Throwable oops) {
			result = new ExchangeMoney();
			result.setOops(oops);
		}

		return result;
	}

}
