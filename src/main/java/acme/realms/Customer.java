
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidString(min = 6, max = 15, pattern = "^\\+?\\d{6,15}$", message = "Número de teléfono inválido: Debe contener entre 6 y 15 dígitos y puede incluir un '+' opcional.")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				address;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				city;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				country;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 500000)
	private Integer				earnedPoints;
}
