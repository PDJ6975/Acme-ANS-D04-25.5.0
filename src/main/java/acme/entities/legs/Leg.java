
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@Optional
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$", message = "El número de vuelo debe tener el siguiente patrón ^[A-Z]{2}\\\\d{4}$")
	private String				flightNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 19, message = "La duración del vuelo debe ser mayor que 0 horas")
	@Automapped
	private Double				duration;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private LegStatus			legStatus;

	@Optional
	@ValidString(min = 1, max = 255, message = "La descripción debe tener mínimio 1 y máximo 255 caracteres.")
	@Automapped
	private String				description;

	@Mandatory
	@ValidMoment(message = "La salida programada debe ser en el futuro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(message = "La llegada programada debe ser en el futuro")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Optional
	@ManyToOne
	private Airport				departureAirport;

	@Optional
	@ManyToOne
	private Airport				arrivalAirport;

	@Optional
	@ManyToOne
	private Aircraft			aircraft;

	@Optional
	@ManyToOne
	private Flight				flight;

}
