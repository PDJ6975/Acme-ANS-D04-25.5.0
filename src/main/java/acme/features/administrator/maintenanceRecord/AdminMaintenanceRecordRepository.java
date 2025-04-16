
package acme.features.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;

@Repository
public interface AdminMaintenanceRecordRepository extends AbstractRepository {

	@Query("SELECT b FROM MaintenanceRecord b WHERE b.draftMode = false")
	Collection<MaintenanceRecord> findPublishedMaintenanceRecords();

	@Query("SELECT a.task FROM MaintenanceTask a WHERE a.maintenanceRecord.id = :masterId")
	Collection<Task> findTasksByMaintenanceRecordId(int masterId);

	@Query("SELECT b FROM MaintenanceRecord b WHERE b.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);

}
