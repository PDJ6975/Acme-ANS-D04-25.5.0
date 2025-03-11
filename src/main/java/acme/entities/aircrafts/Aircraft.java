
package acme.entities.aircrafts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50, message = "El modelo puede tener máximo 50 caractéres")
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(max = 50, message = "El número de registro puede tener máximo 50 caractéres")
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber(min = 1, message = "La capacidad mínima debe ser mayor que 0")
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000, message = "La capacidad debe estar entre 2000 y 50000 kilos")
	@Automapped
	private Double				cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		aircraftStatus;

	@Optional
	@ValidString(max = 255, message = "La descripción debe tener máximo 255 caractéres")
	@Automapped
	private String				details;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
