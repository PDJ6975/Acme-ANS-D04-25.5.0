
package acme.forms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.entities.maintenanceRecords.StatusMaintenance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	private static final long				serialVersionUID	= 1L;

	@Mandatory
	@Valid
	private Map<StatusMaintenance, Integer>	numMaintenanceRecordsByStatus;

	@Mandatory
	@ValidMoment
	private Date							nearestInspectionDue;

	@Mandatory
	@Valid
	private List<String>					topFiveAircraftsWhithMostTask;

	@Mandatory
	@ValidMoney
	private Money							avgEstimatedCost;

	// Asignaciones de vuelo agrupadas por estado
	@Mandatory
	@ValidMoney
	private Money							minEstimatedCost;

	@Mandatory
	@ValidMoney
	private Money							maxEstimatedCost;

	@Mandatory
	@ValidMoney
	private Money							stdDevEstimatedCost;

	@Mandatory
	@ValidNumber(min = 0)
	private Double							avgEstimatedDuration;

	@Mandatory
	@ValidNumber(min = 0)
	private Double							minEstimatedDuration;

	@Mandatory
	@ValidNumber(min = 0)
	private Double							maxEstimatedDuration;

	@Mandatory
	@ValidNumber(min = 0)
	private Double							stdDevEstimatedDuration;

}
