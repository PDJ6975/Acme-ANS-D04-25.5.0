
package acme.entities.assignments;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.legs.Leg;
import acme.realms.members.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"flight_crew_member_id", "leg_id"
	})
})
public class FlightAssignment extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private CrewRole			crewRole;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdated;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private AssignmentStatus	assignmentStatus;

	@Optional
	@ValidString(min = 1)
	@Automapped
	private String				comments;

	// Evitamos cadenas vacías no nulas, pues optional no las convierte a null


	public void setComments(String c) {
		// Si c está vacío o son solo espacios, lo pongo a null
		if (c != null && c.trim().isEmpty())
			c = null;
		this.comments = c;
	}

	// Atributo necesario para saber cuando una asignación está o no publicada


	@Mandatory
	@Automapped
	private boolean draftMode;

}
