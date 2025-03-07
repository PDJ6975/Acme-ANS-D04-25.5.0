
package acme.entities.weathers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
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
	private Long				weatherId;

	@Mandatory
	private Date				timestamp;

	@Optional
	private Double				temperature;

	@Optional
	private Double				humidity;

	@Optional
	private Double				windSpeed;

	@Optional
	@ValidString(max = 100, message = "La descripción de la predicción meteorológica debe tener máximo 100 caractéres")
	private String				weatherDescription;

	@Optional
	private String				location;

	@Optional
	private String				source;

}
