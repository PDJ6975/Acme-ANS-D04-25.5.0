
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceTasks.MaintenanceTask;
import acme.entities.tasks.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("SELECT a FROM Task a WHERE a.id = :id")
	Task findTaskById(int id);

	@Query("SELECT a from MaintenanceRecord a where a.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("SELECT a.task FROM MaintenanceTask a WHERE a.maintenanceRecord.id = :masterId")
	Collection<Task> findTasksByMasterId(@Param("masterId") int masterId);

	@Query("SELECT a.maintenanceRecord FROM MaintenanceTask a WHERE a.id = :taskId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTaskId(int taskId);

	@Query("SELECT a FROM Task a where a.technician.id = :technicianId")
	Collection<Task> findTasksByTechnicianId(int technicianId);

	@Query("SELECT a FROM Task a where a.draftMode = false")
	Collection<Task> findPublishedTasks();

	@Query("SELECT a FROM MaintenanceTask a WHERE a.task.id = :taskId")
	Collection<MaintenanceTask> findAllRelationTask(@Param("taskId") int taskId);

}
