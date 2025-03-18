
package acme.entities.passengers;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

public class BannedPassenger extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				fullName;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Automapped
	private String				passportNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				nationality;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				reason;

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				banDate;

	@Optional
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				liftDate;

}
