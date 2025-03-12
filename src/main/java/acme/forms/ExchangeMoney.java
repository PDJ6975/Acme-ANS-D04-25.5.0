
package acme.forms;

import java.util.Date;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidCurrency;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeMoney extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Query attributes -------------------------------------------------------

	@Mandatory
	@ValidMoney
	public Money				source;

	@Mandatory
	@ValidCurrency
	public String				targetCurrency;

	// Response attributes ----------------------------------------------------

	@Optional
	@ValidMoment(past = true)
	public Date					moment;

	@Optional
	@ValidMoney
	public Money				target;

	@Optional
	@Valid
	public Throwable			oops;

}
