
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.entities.claims.Claim;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdatedMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				step;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private Indicator			indicator;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				resolution;

	@Mandatory
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@Valid
	@ManyToOne
	private Claim				claim;

	@Mandatory
	@Automapped
	private boolean				draftMode;

}
