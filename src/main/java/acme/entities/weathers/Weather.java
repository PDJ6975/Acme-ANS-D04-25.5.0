
package acme.entities.weathers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
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
	@ValidString(pattern = "^WH\\d{4}$", message = "El identificador debe seguir el patrón ^[A-Z]{1}\\d{4}$")
	@Column(unique = true)
	private String				weatherId;

	@Mandatory
	//La validación inferior se ha comentado ya que está pendiente de pensarlo
	//@ValidMoment(past = false, message = "El momento de obtención de la predicción meteorológica no puede estar en el pasado")  
	private Date				timestamp;

	@Mandatory
	@ValidNumber(min = -100, max = 100, message = "La temperatura debe estar entre -100 y 100 grados")
	private Double				temperature;

	@Mandatory
	@ValidNumber(min = 0, max = 100, message = "La humedad debe estar entre 0 y 100")
	private Double				humidity;

	@Mandatory
	@ValidNumber(min = 0, max = 200, message = "La velocidad del viento no puede ser negativa y puede ser de máximo 200 km/h")
	private Double				windSpeed;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "La localización debe tener entre 1 y 50 caracteres")
	private String				location;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "La fuente debe tener entre 1 y 50 caracteres")
	private String				source;

	@Mandatory
	@Valid
	@OneToOne(optional = false) // Se guarda la referencia en Weather
	private Flight				flight;

}
