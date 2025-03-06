
package acme.forms;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.entities.airports.Airport;
import acme.entities.legs.LegStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidNumber(min = 1, message = "El ranking debe ser al menos 1")
	private Integer					ranking;

	@Mandatory
	@ValidNumber(min = 0, message = "Los años hasta la jubilación no pueden ser negativos")
	private Integer					yearsToRetire;

	@Mandatory
	@ValidNumber(min = 0, message = "El ratio de vuelos a tiempo y retrasados debe ser positivo")
	private Double					onTimeDelayedRatio;

	@Mandatory
	@Valid
	private List<Airport>			mostPopularAirports;

	@Mandatory
	@Valid
	private List<Airport>			leastPopularAirports;

	@Mandatory
	@Valid
	private Map<LegStatus, Long>	legsByStatus;

	@Mandatory
	@ValidNumber(min = 0, message = "El coste promedio debe ser positivo")
	private Double					averageCost;

	@Mandatory
	@ValidNumber(min = 0, message = "El coste mínimo debe ser positivo")
	private Double					minimumCost;

	@Mandatory
	@ValidNumber(min = 0, message = "El coste máximo debe ser positivo")
	private Double					maximumCost;

	@Mandatory
	@ValidNumber(min = 0, message = "La desviación estándar del coste debe ser positiva")
	private Double					standardDeviationCost;

}
