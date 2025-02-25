
package acme.entities.flightcrew;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(min = 8, max = 9, pattern = "^[A-Z]{2,3}\\d{6}$", message = "Código de empleado inválido: Debe seguir el patrón ^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidString(min = 6, max = 15, pattern = "^\\+?\\d{6,15}$", message = "Número de teléfono inválido: Debe contener entre 6 y 15 dígitos y puede incluir un '+' opcional.")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Automapped
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@ValidString
	@Automapped
	private String				airline;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

}
