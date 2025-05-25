
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
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	protected TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private SpamDetector							spamDetector;


	@Override
	public void authorise() {
		boolean isAuthorised = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		Integer aircraftId = super.getRequest().getData("aircraft", int.class, null);

		if (aircraftId != null && aircraftId != 0) {
			Collection<Aircraft> available = this.repository.findAllAircrafts();
			boolean aircraftIsAvailable = available.stream().anyMatch(l -> l.getId() == aircraftId);
			isAuthorised = isAuthorised && aircraftIsAvailable;
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setStatus(StatusMaintenance.PENDING);
		// Set default moment to current
		maintenanceRecord.setMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		// Set moment automatically
		maintenanceRecord.setMoment(MomentHelper.getCurrentMoment());
		// Bind other fields
		super.bindObject(maintenanceRecord, "status", "nextInspectionDue", "estimatedCost", "notes");
		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setAircraft(super.getRequest().getData("aircraft", Aircraft.class));
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		if (!this.getBuffer().getErrors().hasErrors("status"))
			super.state(maintenanceRecord.getStatus() != null, "status", "technician.maintenance-record.form.error.noStatus", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("nextInspectionDue") && maintenanceRecord.getNextInspectionDue() != null)
			super.state(maintenanceRecord.getNextInspectionDue().compareTo(maintenanceRecord.getMoment()) > 0, "nextInspectionDue", "technician.maintenance-record.form.error.nextInspectionDue", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("estimatedCost") && maintenanceRecord.getEstimatedCost() != null)
			super.state(0.00 <= maintenanceRecord.getEstimatedCost().getAmount() && maintenanceRecord.getEstimatedCost().getAmount() <= 1000000.00, "estimatedCost", "technician.maintenance-record.form.error.estimatedCost", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("notes") && maintenanceRecord.getNotes() != null)
			super.state(maintenanceRecord.getNotes().length() <= 255, "notes", "technician.maintenance-record.form.error.notes", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("aircraft") && maintenanceRecord.getAircraft() != null)
			super.state(this.repository.findAllAircrafts().contains(maintenanceRecord.getAircraft()), "aircraft", "technician.maintenance-record.form.error.aircraft", maintenanceRecord);

		if (!super.getBuffer().getErrors().hasErrors("notes")) {
			boolean isSpamFn = this.spamDetector.isSpam(maintenanceRecord.getNotes());
			super.state(!isSpamFn, "notes", "technician.error.spam");
		}

		// Validate moment not in past
		long actual = MomentHelper.getCurrentMoment().getTime() / 60000;
		long momento = maintenanceRecord.getMoment().getTime() / 60000;
		super.state(momento >= actual, "moment", "technician.maintenance-record.form.error.date");

		// Validate nextInspectionDue not in past
		if (maintenanceRecord.getNextInspectionDue() != null) {
			long nextMin = maintenanceRecord.getNextInspectionDue().getTime() / 60000;
			super.state(nextMin >= actual, "nextInspectionDue", "technician.maintenance-record.form.error.date");
		}
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		assert maintenanceRecord != null;
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();
		SelectChoices selectAircraft = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());
		SelectChoices selectStatus = SelectChoices.from(StatusMaintenance.class, maintenanceRecord.getStatus());

		Dataset dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		// Override moment in view
		dataset.put("moment", MomentHelper.getCurrentMoment());

		dataset.put("aircraft", selectAircraft.getSelected().getKey());
		dataset.put("aircrafts", selectAircraft);
		dataset.put("status", selectStatus.getSelected().getKey());
		dataset.put("statuses", selectStatus);
		dataset.put("technician", maintenanceRecord.getTechnician().getIdentity().getFullName());

		super.getResponse().addData(dataset);
	}
}
