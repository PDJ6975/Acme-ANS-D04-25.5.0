
package acme.entities.technicians;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "Número de licencia inválido: Debe seguir el patrón ^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "Número de teléfono inválido: Debe contener entre 6 y 15 dígitos y puede incluir un '+' opcional.")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 50, message = "El nombre de la especialización es demasiado largo: Debe tener maximo 50 caracteres")
	@Automapped
	private String				specialisation;

	@Mandatory
	@Automapped
	private Boolean				annualHealthTest;

	@Mandatory
	@ValidNumber(min = 0, integer = 2, fraction = 0)
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@ValidString(max = 250, message = "La longitud maxima de este espacio es de 255 caracteres.")
	@Automapped
	private String				certifications;

}
