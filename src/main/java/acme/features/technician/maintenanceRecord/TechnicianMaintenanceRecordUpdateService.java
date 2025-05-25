
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
	private TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private SpamDetector							spamDetector;

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
		int maintenanceRecordId = super.getRequest().getData("id", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(maintenanceRecord, "status", "nextInspectionDue", "estimatedCost", "notes");
		maintenanceRecord.setMoment(MomentHelper.getCurrentMoment());
		maintenanceRecord.setTechnician(technician);
		// 1) Lee el id que vino en la petición (que tú mismo metes como hidden en el formulario)
		int id = super.getRequest().getData("id", int.class);

		// 2) Vuelve a cargar el objeto completo de la BD
		MaintenanceRecord original = this.repository.findMaintenanceRecordById(id);

		// 3) No aceptes ningún cambio en el aircraft: copia el que tenía antes
		maintenanceRecord.setAircraft(original.getAircraft());
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		if (!super.getBuffer().getErrors().hasErrors("status"))
			super.state(maintenanceRecord.getStatus() != null, "status", "technician.maintenance-record.form.error.noStatus", maintenanceRecord);

		if (!super.getBuffer().getErrors().hasErrors("nextInspectionDue") && maintenanceRecord.getNextInspectionDue() != null)
			super.state(maintenanceRecord.getNextInspectionDue().compareTo(maintenanceRecord.getMoment()) > 0, "nextInspectionDue", "technician.maintenance-record.form.error.nextInspectionDue", maintenanceRecord);

		if (!super.getBuffer().getErrors().hasErrors("estimatedCost") && maintenanceRecord.getEstimatedCost() != null)
			super.state(maintenanceRecord.getEstimatedCost().getAmount() >= 0.0 && maintenanceRecord.getEstimatedCost().getAmount() <= 1_000_000.0, "estimatedCost", "technician.maintenance-record.form.error.estimatedCost", maintenanceRecord);

		if (!super.getBuffer().getErrors().hasErrors("notes")) {
			boolean isSpamFn = this.spamDetector.isSpam(maintenanceRecord.getNotes());
			super.state(!isSpamFn, "notes", "technician.error.spam");
		}
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);

		super.getResponse().setView("/technician/maintenance-record/list?mine=true");

	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();

		SelectChoices selectStatus = SelectChoices.from(StatusMaintenance.class, maintenanceRecord.getStatus());
		SelectChoices selectAircraft = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		Dataset dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());
		dataset.put("aircraft", selectAircraft.getSelected().getKey());
		dataset.put("aircrafts", selectAircraft);
		dataset.put("status", selectStatus.getSelected().getKey());
		dataset.put("statuses", selectStatus);

		super.getResponse().addData(dataset);
	}
}
