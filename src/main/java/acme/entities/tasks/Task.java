
package acme.entities.tasks;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.technicians.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@Automapped
	private TypeTask			type;

	@Mandatory
	@ValidString(max = 255, message = "Debe contener 255 caracteres has superado el limite.")
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, message = "Debe estar comprendido entre 0 y 10")
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 1000, integer = 4, fraction = 1, message = "Debe estar expresado en horas. Maximo 1000 horas.")
	@Automapped
	private Double				estimatedDuration;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

	@Mandatory
	@Automapped
	private boolean				draftMode;

}
