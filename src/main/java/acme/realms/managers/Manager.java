
package acme.realms.managers;

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
import acme.client.components.validation.ValidUrl;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$", message = "El identificador debe seguir el patrón ^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	private String				managerId;

	@Mandatory
	@ValidNumber(min = 0, message = "Los años de experiencia no pueden ser negativos")
	@Automapped
	private Integer				yearsOfExperience;

	@Mandatory
	@ValidMoment(past = true, message = "La fecha de nacimiento debe estar en el pasado")
	private Date				birth;

	@Optional
	@ValidUrl(message = "La URL de la imagen no es válida")
	private String				pictureUrl;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

}
