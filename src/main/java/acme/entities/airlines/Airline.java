
package acme.entities.airlines;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{3}$")
	@Column(unique = true)
	private String				iataCode;

	@Mandatory
	@ValidUrl(remote = false)
	@Automapped
	private String				website;

	@Mandatory
	@Valid
	@Automapped
	private Type				type;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				foundationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				emailAddress;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

}
