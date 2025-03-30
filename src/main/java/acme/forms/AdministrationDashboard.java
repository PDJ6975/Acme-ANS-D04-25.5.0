
package acme.forms;

import java.util.Map;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministrationDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	private Map<String, Long>	airportsByOperationalScope;

	@Mandatory
	@Valid
	private Map<String, Long>	airlinesByType;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				ratioAirlinesWithEmailAndPhone;

	@Mandatory
	@Valid
	private Map<String, Double>	aircraftsActiveRatios;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				ratioReviewsAboveFive;

	@Mandatory
	@ValidNumber(min = 0)
	private Long				reviewsCount;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				reviewsAverage;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				reviewsMinimum;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				reviewsMaximum;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				reviewsStandardDeviation;
}
