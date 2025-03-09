
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2}\\\\d{4}$", message = "El número de vuelo debe tener el siguiente patrón ^[A-Z]{2}\\\\d{4}$")
	private String				flightNumber;

	@Mandatory
	@ValidNumber(min = 0, message = "La duración del vuelo debe ser mayor que 0 horas")
	private Double				duration;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			legStatus;

	@Optional
	@ValidString(max = 255)
	private String				description;

	@Mandatory
	@ValidMoment(past = false, message = "La salida programada debe ser en el futuro")
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(past = false, message = "La llegada programada debe ser en el futuro")
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

}
