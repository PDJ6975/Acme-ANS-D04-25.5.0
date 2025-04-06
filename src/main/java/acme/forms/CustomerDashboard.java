
package acme.forms;

import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	private List<String>		lastFiveDestinations;

	@Mandatory
	@ValidNumber(min = 0)
	private Double				moneySpentLastYear;

	@Mandatory
	@Valid
	private Map<String, Long>	bookingsByTravelClass;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Long				bookingCostCount;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				bookingCostAverage;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				bookingCostMinimum;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				bookingCostMaximum;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				bookingCostStandardDeviation;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Long				passengerCount;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				passengerAverage;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				passengerMinimum;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				passengerMaximum;

	@Mandatory
	@ValidNumber(min = 0)
	@Transient
	private Double				passengerStandardDeviation;
}
