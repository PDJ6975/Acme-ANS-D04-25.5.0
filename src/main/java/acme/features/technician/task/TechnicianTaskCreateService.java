
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceTasks.MaintenanceTask;
import acme.entities.tasks.Task;
import acme.entities.tasks.TypeTask;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task task;
		Technician technician;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		task = new Task();
		task.setDraftMode(true);
		task.setTechnician(technician);
		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
		task.setTechnician(technician);
	}

	@Override
	public void validate(final Task task) {
		if (task.getPriority() == null)
			super.state(false, "priority", "typeMismatch.priority", task);

		if (!this.getBuffer().getErrors().hasErrors("type"))
			super.state(task.getType() != null, "type", "technician.task.form.error.noType", task);

		if (!this.getBuffer().getErrors().hasErrors("description") && task.getDescription() != null)
			super.state(task.getDescription().length() <= 255, "description", "technician.task.form.error.description", task);

		if (!this.getBuffer().getErrors().hasErrors("priority") && task.getPriority() != null)
			super.state(0 <= task.getPriority() && task.getPriority() <= 10, "priority", "technician.task.form.error.priority", task);

		if (!this.getBuffer().getErrors().hasErrors("estimatedDuration") && task.getEstimatedDuration() != null)
			super.state(0 <= task.getEstimatedDuration() && task.getEstimatedDuration() <= 1000, "estimatedDuration", "technician.task.form.error.estimatedDuration", task);
	}

	@Override
	public void perform(final Task task) {
		MaintenanceRecord maintenanceRecord;
		Integer maintenanceRecordId;
		MaintenanceTask assignment = new MaintenanceTask();
		maintenanceRecordId = super.getRequest().hasData("maintenanceRecordId") ? super.getRequest().getData("maintenanceRecordId", Integer.class) : null;
		this.repository.save(task);
		if (maintenanceRecordId != null) {
			maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
			super.state(maintenanceRecord != null && maintenanceRecord.getTechnician().equals(task.getTechnician()), "*", "technician.task.form.error.invalidMaintenanceRecord", task);
			assignment.setTask(task);
			assignment.setMaintenanceRecord(maintenanceRecord);
			this.repository.save(assignment);
		}

	}

	@Override
	public void unbind(final Task task) {
		SelectChoices selection;
		Dataset dataset;
		selection = SelectChoices.from(TypeTask.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getIdentity().getFullName());
		dataset.put("type", selection.getSelected().getKey());
		dataset.put("types", selection);
		dataset.put("maintenanceRecordId", super.getRequest().getData("maintenanceRecordId", Integer.class));
		System.out.println(dataset);
		super.getResponse().addData(dataset);
	}

}
