
package acme.entities.tasks;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.maintenanceRecords.MaintenanceRecord;
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
	@ValidNumber(min = 0, integer = 2, fraction = 1, message = "Debe estar expresado en horas, maximo un número fraccionario")
	@Automapped
	private Double				estimatedDuration;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

}
