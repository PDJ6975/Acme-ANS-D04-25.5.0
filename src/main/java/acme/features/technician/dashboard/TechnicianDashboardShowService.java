
package acme.features.technician.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.StatusMaintenance;
import acme.forms.TechnicianDashboard;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianDashboardShowService extends AbstractGuiService<Technician, TechnicianDashboard> {

	@Autowired
	private TechnicianDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		TechnicianDashboard dashboard = new TechnicianDashboard();

		// 1. Registros de mantenimiento agrupados por estado
		List<Object[]> rawCounts = this.repository.findNumMaintenanceRecordsByStatus(technicianId);
		Map<StatusMaintenance, Integer> counts = new HashMap<>();
		for (Object[] row : rawCounts)
			counts.put((StatusMaintenance) row[0], ((Long) row[1]).intValue());
		dashboard.setNumMaintenanceRecordsByStatus(counts);

		// 2. Registro de mantenimiento con la inspección más próxima
		List<MaintenanceRecord> maintenanceRecords = this.repository.findMaintenanceRecordsWithPendingTasksOrdered(technicianId);
		if (!maintenanceRecords.isEmpty())
			dashboard.setNearestInspectionDue(maintenanceRecords.get(0)); // Puedes extraer la fecha si sólo se desea la fecha
		else
			dashboard.setNearestInspectionDue(null);

		// 3. Top 5 aeronaves con mayor número de tareas
		List<Object[]> topAircrafts = this.repository.findTopFiveAircraftsWithMostTasks(technicianId, PageRequest.of(0, 5));
		List<String> aircraftNames = new java.util.ArrayList<>();
		for (Object[] row : topAircrafts) {
			String aircraftName = row[0] != null ? row[0].toString() : "N/A";
			aircraftNames.add(aircraftName);
		}
		dashboard.setTopFiveAircraftsWhithMostTask(aircraftNames);

		// 4. Estadísticas sobre el coste estimado de registros en el último año.
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		Date lastYear = cal.getTime();

		Double avgCost = this.repository.costAverage(technicianId, lastYear);
		Double minCost = this.repository.costMinimum(technicianId, lastYear);
		Double maxCost = this.repository.costMaximum(technicianId, lastYear);
		Double stddevCost = this.repository.costStandardDeviation(technicianId, lastYear);

		// Si alguno es null, asignamos 0.0.
		avgCost = avgCost != null ? avgCost : 0.0;
		minCost = minCost != null ? minCost : 0.0;
		maxCost = maxCost != null ? maxCost : 0.0;
		stddevCost = stddevCost != null ? stddevCost : 0.0;

		Money avgCostMoney = new Money();
		avgCostMoney.setAmount(avgCost);
		avgCostMoney.setCurrency("EUR");
		dashboard.setAvgEstimatedCost(avgCostMoney);

		Money minCostMoney = new Money();
		minCostMoney.setAmount(minCost);
		minCostMoney.setCurrency("EUR");
		dashboard.setMinEstimatedCost(minCostMoney);

		Money maxCostMoney = new Money();
		maxCostMoney.setAmount(maxCost);
		maxCostMoney.setCurrency("EUR");
		dashboard.setMaxEstimatedCost(maxCostMoney);

		Money stddevCostMoney = new Money();
		stddevCostMoney.setAmount(stddevCost);
		stddevCostMoney.setCurrency("EUR");
		dashboard.setStdDevEstimatedCost(stddevCostMoney);

		// 5. Estadísticas sobre la duración estimada de las tareas.
		Double avgDuration = this.repository.taskDurationAverage(technicianId);
		Double minDuration = this.repository.taskDurationMinimum(technicianId);
		Double maxDuration = this.repository.taskDurationMaximum(technicianId);
		Double stddevDuration = this.repository.taskDurationStandardDeviation(technicianId);

		avgDuration = avgDuration != null ? avgDuration : 0.0;
		minDuration = minDuration != null ? minDuration : 0.0;
		maxDuration = maxDuration != null ? maxDuration : 0.0;
		stddevDuration = stddevDuration != null ? stddevDuration : 0.0;

		dashboard.setAvgEstimatedDuration(avgDuration);
		dashboard.setMinEstimatedDuration(minDuration);
		dashboard.setMaxEstimatedDuration(maxDuration);
		dashboard.setStdDevEstimatedDuration(stddevDuration);

		super.getBuffer().addData(dashboard);
	}

	/**
	 * Desenrolla recursivamente el valor subyacente si está envuelto en arrays.
	 */
	private Object unwrap(Object value) {
		while (value instanceof Object[]) {
			Object[] arr = (Object[]) value;
			if (arr.length == 0)
				return null;
			value = arr[0];
		}
		return value;
	}

	/**
	 * Extrae el valor double del array de resultados en el índice indicado utilizando unwrap.
	 */
	private double extractDouble(final Object[] stats, final int index) {
		if (stats == null || stats.length <= index)
			return 0.0;
		Object value = this.unwrap(stats[index]);
		return value != null && value instanceof Number ? ((Number) value).doubleValue() : 0.0;
	}

	@Override
	public void unbind(final TechnicianDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "numMaintenanceRecordsByStatus", "nearestInspectionDue", "topFiveAircraftsWhithMostTask", "avgEstimatedCost", "minEstimatedCost", "maxEstimatedCost", "stdDevEstimatedCost", "avgEstimatedDuration",
			"minEstimatedDuration", "maxEstimatedDuration", "stdDevEstimatedDuration");
		super.getResponse().addData(dataset);
	}
}
