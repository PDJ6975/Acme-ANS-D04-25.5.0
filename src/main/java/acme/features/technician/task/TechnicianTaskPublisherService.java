
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TypeTask;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianTaskPublisherService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository	repository;

	// AbstractGuiService interface -------------------------------------------

	@Autowired
	private SpamDetector				spamDetector;


	@Override
	public void authorise() {
		int taskId = super.getRequest().getData("id", int.class);
		Task task = this.repository.findTaskById(taskId);
		Technician technician = task == null ? null : task.getTechnician();

		boolean authorised = task != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(taskId);

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

		if (!super.getBuffer().getErrors().hasErrors("description")) {
			boolean isSpamFn = this.spamDetector.isSpam(task.getDescription());
			super.state(!isSpamFn, "description", "technician.error.spam");
		}
	}

	@Override
	public void perform(final Task task) {
		if (!super.getBuffer().getErrors().hasErrors()) {
			task.setDraftMode(false);
			this.repository.save(task);
			// en lugar de quedarnos en update o en la lista,
			// volvemos al 'show' de esta task, ahora ya publicada:
			super.getResponse().setView(String.format("/technician/task/show?id=%d", task.getId()));
		}
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TypeTask.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getIdentity().getFullName());
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
