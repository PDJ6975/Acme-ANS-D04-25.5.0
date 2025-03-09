
package acme.forms;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidScore
	private Double				ratioSuccessfulClaims;

	@Mandatory
	@ValidScore
	private Double				ratioRejectedClaims;

	@Mandatory
	@ValidString
	private String				highestClaimsMonths;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				aveLogs;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				minLogs;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				maxLogs;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				standardDesviationLogs;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				aveClaims;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				minClaims;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				maxClaims;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				standardDesviationClaims;

}
