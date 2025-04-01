
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.StatusMaintenance;
import acme.entities.maintenanceTasks.MaintenanceTask;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		Technician technician;
		MaintenanceRecord maintenanceRecord;
		boolean status;
		int maintenanceRecordId;
		maintenanceRecordId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int maintenanceRecordId;
		maintenanceRecordId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		super.bindObject(maintenanceRecord, "moment", "status", "nextInspectionDue", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(super.getRequest().getData("aircraft", Aircraft.class));
		maintenanceRecord.setTechnician(technician);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		Collection<MaintenanceTask> assignment;
		assignment = this.repository.findAllTaskByMaintenanceRecordId(maintenanceRecord.getId());
		if (!assignment.isEmpty())
			this.repository.deleteAll(assignment);
		this.repository.delete(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices selectAircrafts;
		SelectChoices selectStatus;
		Collection<Aircraft> aircrafts;
		Dataset dataset;

		aircrafts = this.repository.findAllAircrafts();

		selectStatus = SelectChoices.from(StatusMaintenance.class, maintenanceRecord.getStatus());
		selectAircrafts = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());
		dataset.put("aircraft", selectAircrafts.getSelected().getKey());
		dataset.put("aircrafts", selectAircrafts);
		dataset.put("status", selectStatus.getSelected().getKey());
		dataset.put("statuses", selectStatus);

		super.getResponse().addData(dataset);
	}

}
