
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceTasks.MaintenanceTask;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("SELECT a FROM MaintenanceRecord a WHERE a.id = :id")
	MaintenanceRecord findMaintenanceRecordById(@Param("id") int masterId);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM MaintenanceRecord a WHERE a.technician.id = :technicianId")
	Collection<MaintenanceRecord> findAllMaintenanceRecordsByTechnicianId(int technicianId);

	@Query("SELECT a FROM MaintenanceRecord a WHERE a.draftMode = false")
	Collection<MaintenanceRecord> findPublishedMaintenanceRecords();

	@Query("SELECT a.task FROM MaintenanceTask a WHERE a.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Task> findTasksByMaintenanceRecordId(int maintenanceRecordId);

	@Query("SELECT a FROM MaintenanceTask a WHERE a.maintenanceRecord.id = :maintenanceRecordId")
	Collection<MaintenanceTask> findAllTaskByMaintenanceRecordId(int maintenanceRecordId);

}
