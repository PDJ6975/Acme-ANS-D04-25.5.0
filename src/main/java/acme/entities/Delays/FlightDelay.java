
package acme.entities.Delays;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class FlightDelay extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				flightMoment;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$")
	@Automapped
	private String				depatureAirportCode;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$")
	@Automapped
	private String				arrivalAirportCode;

	@Mandatory
	@ValidNumber(min = 0, max = 1440)
	@Automapped
	private Integer				depatureDelayMin;

	@Mandatory
	@ValidNumber(min = 0, max = 1440)
	@Automapped
	private Integer				arrivalDelayMin;

	@Optional
	@Automapped
	private Status				status;

}
