
package acme.entities.weathers;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
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

	// QUITADO EL weatherID ya que según el profesor no es necesario
	/*
	 * @Mandatory
	 * 
	 * @Automapped
	 * 
	 * @ValidString(pattern = "^WH\\d{4}$", message = "El identificador debe seguir el patrón ^[A-Z]{1}\\d{4}$")
	 * 
	 * @Column(unique = true)
	 * private String weatherId;
	 */

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestamp;

	@Mandatory
	@ValidNumber(min = -100, max = 100, message = "La temperatura debe estar entre -100 y 100 grados")
	@Automapped
	private Double				temperature;

	@Mandatory
	@ValidNumber(min = 0, max = 100, message = "La humedad debe estar entre 0 y 100")
	@Automapped
	private Double				humidity;

	@Mandatory
	@ValidNumber(min = 0, max = 200, message = "La velocidad del viento no puede ser negativa y puede ser de máximo 200 km/h")
	@Automapped
	private Double				windSpeed;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "La localización debe tener entre 1 y 50 caracteres")
	@Automapped
	private String				location;

	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Flight				flight;

}
