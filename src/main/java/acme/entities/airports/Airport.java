
package acme.entities.airports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "El nombre puede tener máximo 50 caractéres")
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{3}$", message = "El código IATA debe seguir el siguiente patrón: ^[A-Z]{3}$")
	@Column(unique = true)
	private String				iataCode;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Valid
	@Automapped
	private OperationalScope	operationalScope;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "La ciudad puede tener máximo 50 caractéres")
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(min = 1, max = 50, message = "El país puede tener máximo 50 caractéres")
	@Automapped
	private String				country;

	@Optional
	@ValidUrl(message = "Formato de dirección web incorrecto", remote = false)
	@Automapped
	private String				website;

	@Optional
	@ValidEmail(message = "Formato de dirección email incorrecto")
	@Automapped
	private String				emailAddress;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "El número de teléfono debe seguir el siguiente formato: ^\\\\+?\\\\d{6,15}$")
	@Automapped
	private String				contactPhone;

}
