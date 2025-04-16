
package acme.features.technician.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	// 1. Número de registros de mantenimiento agrupados por su estado para el técnico.
	@Query("SELECT m.status, COUNT(m) " + "FROM MaintenanceRecord m " + "WHERE m.technician.id = :technicianId " + "GROUP BY m.status")
	List<Object[]> findNumMaintenanceRecordsByStatus(int technicianId);

	// 2. Registro de mantenimiento con la inspección más próxima (con tareas asignadas).
	@Query("SELECT m " + "FROM MaintenanceRecord m " + "JOIN MaintenanceTask mt ON mt.maintenanceRecord.id = m.id " + "JOIN Task t ON t.id = mt.task.id " + "WHERE t.technician.id = :technicianId " + "AND m.nextInspectionDue > CURRENT_TIMESTAMP "
		+ "ORDER BY m.nextInspectionDue ASC")
	List<MaintenanceRecord> findMaintenanceRecordsWithPendingTasksOrdered(int technicianId);

	// 3. Top 5 aeronaves con mayor número de tareas asociadas a sus registros de mantenimiento.
	@Query("SELECT m.aircraft.model, COUNT(t) " + "FROM MaintenanceRecord m " + "JOIN MaintenanceTask mt ON mt.maintenanceRecord.id = m.id " + "JOIN Task t ON t.id = mt.task.id " + "WHERE m.technician.id = :technicianId " + "GROUP BY m.aircraft.model "
		+ "ORDER BY COUNT(t) DESC")
	List<Object[]> findTopFiveAircraftsWithMostTasks(int technicianId, Pageable pageable);

	// 4. Estadísticas sobre el coste estimado de registros en el último año.
	@Query("select avg(m.estimatedCost.amount) " + "from MaintenanceRecord m " + "where m.technician.id = :technicianId and m.moment >= :lastYear")
	Double costAverage(int technicianId, Date lastYear);

	@Query("select min(m.estimatedCost.amount) " + "from MaintenanceRecord m " + "where m.technician.id = :technicianId and m.moment >= :lastYear")
	Double costMinimum(int technicianId, Date lastYear);

	@Query("select max(m.estimatedCost.amount) " + "from MaintenanceRecord m " + "where m.technician.id = :technicianId and m.moment >= :lastYear")
	Double costMaximum(int technicianId, Date lastYear);

	@Query("select stddev(m.estimatedCost.amount) " + "from MaintenanceRecord m " + "where m.technician.id = :technicianId and m.moment >= :lastYear")
	Double costStandardDeviation(int technicianId, Date lastYear);

	// 5. Estadísticas sobre la duración estimada de las tareas en las que participa el técnico.
	@Query("select avg(t.estimatedDuration) " + "from Task t " + "where t.technician.id = :technicianId")
	Double taskDurationAverage(int technicianId);

	@Query("select min(t.estimatedDuration) " + "from Task t " + "where t.technician.id = :technicianId")
	Double taskDurationMinimum(int technicianId);

	@Query("select max(t.estimatedDuration) " + "from Task t " + "where t.technician.id = :technicianId")
	Double taskDurationMaximum(int technicianId);

	@Query("select stddev(t.estimatedDuration) " + "from Task t " + "where t.technician.id = :technicianId")
	Double taskDurationStandardDeviation(int technicianId);
}
