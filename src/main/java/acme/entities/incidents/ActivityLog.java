
package acme.entities.incidents;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.stages.FlightStage;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private FlightStage			flightStage;

}
