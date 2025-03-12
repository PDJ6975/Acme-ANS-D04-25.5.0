
package acme.entities.systemCurrencies;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SystemCurrency extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$")
	@Automapped
	private String				actualCurrency;

	@Mandatory
	@ValidString(max = 750, pattern = "^([A-Z]{3})(,[A-Z]{3})*$")
	@Automapped
	private String				validCurrencies;

}
