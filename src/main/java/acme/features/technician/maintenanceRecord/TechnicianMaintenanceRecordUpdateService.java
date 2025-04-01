
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
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Technician technician;
		boolean status;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

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

		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setAircraft(super.getRequest().getData("aircraft", Aircraft.class));
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		if (!super.getBuffer().getErrors().hasErrors("status"))
			super.state(maintenanceRecord.getStatus() != null, "status", "technician.maintenance-record.form.error.noStatus", maintenanceRecord);

		if (!super.getBuffer().getErrors().hasErrors("nextInspectionDue") && maintenanceRecord.getNextInspectionDue() != null)
			super.state(maintenanceRecord.getNextInspectionDue().compareTo(maintenanceRecord.getMoment()) > 0, "nextInspectionDue", "technician.maintenance-record.form.error.nextInspectionDue", maintenanceRecord);

		if (!super.getBuffer().getErrors().hasErrors("estimatedCost") && maintenanceRecord.getEstimatedCost() != null)
			super.state(maintenanceRecord.getEstimatedCost().getAmount() >= 0.0 && maintenanceRecord.getEstimatedCost().getAmount() <= 1_000_000.0, "estimatedCost", "technician.maintenance-record.form.error.estimatedCost", maintenanceRecord);
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);

		super.getResponse().setView("/technician/maintenance-record/list?mine=true");

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
