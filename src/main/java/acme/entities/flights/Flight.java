
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version
	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(max = 50, message = "El tag no puede superar los 50 caracteres")
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(max = 255, message = "La descripción no puede superar los 255 caracteres")
	@Automapped
	private String				description;

	// Derived attributes from legs

	@Mandatory
	@ValidMoment(past = false, message = "La salida programada debe ser en el futuro")
	private Date				scheduledDeparture; // Derivado del primer leg

	@Mandatory
	@ValidMoment(past = false, message = "La llegada programada debe ser en el futuro")
	private Date				scheduledArrival; // Derivado del último leg

	@Mandatory
	@Valid
	private String				originCity; // Derivado del primer leg

	@Mandatory
	@Valid
	private String				destinationCity; // Derivado del último leg

	@Mandatory
	@ValidNumber(min = 0, integer = 2, fraction = 0, message = "El número de escalas no puede ser negativo")
	private Integer				layovers; // Derivado de los tramos
}
