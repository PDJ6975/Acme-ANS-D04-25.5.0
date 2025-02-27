
package acme.entities.assignments;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.members.FlightCrewMember;
import acme.entities.stages.FlightStage;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	@Automapped
	private FlightCrewMember	crewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	@Automapped
	private FlightStage			flightStage;

	@Mandatory
	@Valid
	@Automapped
	private CrewRole			crewRole;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdated;

	@Mandatory
	@Valid
	@Automapped
	private AssignmentStatus	assignmentStatus;

	@Optional
	@ValidString
	@Automapped
	private String				comments;

}
