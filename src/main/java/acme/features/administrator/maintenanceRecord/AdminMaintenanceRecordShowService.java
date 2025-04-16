
package acme.features.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.StatusMaintenance;
import acme.entities.tasks.Task;

@GuiService
public class AdminMaintenanceRecordShowService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdminMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);
		status = maintenanceRecord != null && !maintenanceRecord.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		assert maintenanceRecord != null;

		Dataset dataset;
		Collection<Task> tasks;
		SelectChoices statuses;

		statuses = SelectChoices.from(StatusMaintenance.class, maintenanceRecord.getStatus());
		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecord.getId());

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("technician", maintenanceRecord.getTechnician().getUserAccount().getIdentity().getFullName());
		dataset.put("tasks", tasks);
		dataset.put("masterId", maintenanceRecord.getId());

		super.getResponse().addData(dataset);
	}
}
