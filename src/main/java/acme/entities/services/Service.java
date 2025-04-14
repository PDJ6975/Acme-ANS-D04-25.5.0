
package acme.entities.services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				link;

	@Mandatory
	@ValidNumber(min = 0, max = 8760)
	@Automapped
	private Double				averageDwellTime;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$", message = "El código de promoción debe seguir el patrón ^[A-Z]{4}-[0-9]{2}$")
	@Column(unique = true)
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				discountMoney;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;

	// Evitamos cadenas vacías no nulas, pues optional no las convierte a null


	public void setPromotionCode(String p) {
		// Si c está vacío o son solo espacios, lo pongo a null
		if (p != null && p.trim().isEmpty())
			p = null;
		this.promotionCode = p;
	}

}
