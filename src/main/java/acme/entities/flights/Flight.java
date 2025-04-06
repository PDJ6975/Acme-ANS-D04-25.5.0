
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
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
import acme.entities.airlines.Airline;
import acme.realms.managers.Manager;
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
	@ValidString(min = 1, max = 50, message = "El tag no puede superar los 50 caracteres")
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
	@ValidString(min = 1, max = 255, message = "La descripción no puede superar los 255 caracteres")
	@Automapped
	private String				description;

	// Derived attributes from legs

	@ValidMoment
	@Transient
	private Date				scheduledDeparture; // Derivado del primer leg

	@ValidMoment
	@Transient
	private Date				scheduledArrival; // Derivado del último leg

	@ValidString(min = 1, max = 50, message = "El nombre de la ciudad debe tener mínimo 1 y máximo 50 caracteres")
	@Transient
	private String				originCity; // Derivado del primer leg

	@ValidString(min = 1, max = 50, message = "El nombre de la ciudad debe tener mínimo 1 y máximo 50 caracteres")
	@Transient
	private String				destinationCity; // Derivado del último leg

	@ValidNumber(min = 0, max = 20, message = "El número de escalas no puede ser negativo y debe ser máximo de 20")
	@Transient
	private Integer				layovers; // Derivado de los tramos

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;
}
