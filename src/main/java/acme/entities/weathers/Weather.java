
package acme.entities.weathers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Weather extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@Column(unique = true)
	private String				weatherId;

	@Mandatory
	@ValidMoment(past = false, message = "El momento de obtención de la predicción meteorológica no puede estar en el pasado")
	private Date				timestamp;

	@Mandatory
	@ValidNumber
	private Double				temperature;

	@Mandatory
	@ValidNumber(min = 0, max = 100, message = "La humedad debe estar entre 0 y 100")
	private Double				humidity;

	@Mandatory
	@ValidNumber(min = 0, message = "La velocidad del viento no puede ser negativa")
	private Double				windSpeed;

	@Mandatory
	@ValidString
	private String				location;

	@Mandatory
	@ValidString
	private String				source;

	@Optional
	//@Mandatory
	//@JoinColumn(name = "flight_id", unique = true)
	@Valid
	//@OneToOne(optional = false)
	Flight						flight;

}
