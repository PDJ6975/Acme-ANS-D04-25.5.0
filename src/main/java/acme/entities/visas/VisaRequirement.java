
package acme.entities.visas;

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
public class VisaRequirement extends AbstractEntity {

	/*
	 * Para el desarrollo de esta tarea emplearemos Travel Buddy API (https://travel-buddy.ai/api/), con 600 peticiones mensuales gratuitas
	 */

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString
	@Automapped
	private String				passportCountry; // País del pasaporte

	@Mandatory
	@ValidString
	@Automapped
	private String				destinationCountry; // País de destino

	@Mandatory
	@ValidString
	@Automapped
	private String				continent;

	@Mandatory
	@ValidString
	@Automapped
	private String				capital;

	@Mandatory
	@ValidString
	@Automapped
	private String				currency;

	@Optional
	@ValidString(min = 2, max = 6, pattern = "^\\+\\d{1,5}$", message = "Código de teléfono inválido: Debe comenzar con '+' seguido de 1 a 5 dígitos.")
	@Automapped
	private String				phoneCode; // por ejemplo: "+971"

	@Optional
	@ValidString(min = 3, max = 6, pattern = "^[+-]\\d{2}:\\d{2}$", message = "Zona horaria inválida: Debe seguir el formato ±HH:MM.")
	@Automapped
	private String				timezone; // por ejemplo: "+04:00"

	@Mandatory
	@ValidString
	@Automapped
	private String				visaType; // por ejemplo: "Visa required"

	@Optional
	@ValidString
	@Automapped
	private String				stayDuration; // duración de estancia permitida

	@Optional
	@ValidString
	@Automapped
	private String				passportValidity; // por ejemplo: "6 months"

	@Optional
	@ValidString
	@Automapped
	private String				additionalInfo; // Texto adicional de excepciones sobre los requisitos de visa

	@Optional
	@ValidString
	@Automapped
	private String				officialLink; // Enlace oficial para más detalles
}
