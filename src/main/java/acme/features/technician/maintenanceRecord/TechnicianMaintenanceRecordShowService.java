
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
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		status = maintenanceRecord != null && (super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()) || !maintenanceRecord.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Collection<Aircraft> aircrafts;
		Dataset dataset;
		SelectChoices selectAircrafts;
		SelectChoices selectStatus;
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
		if (maintenanceRecord.isDraftMode())
			super.getResponse().addGlobal("warning", "Este registro aún no ha sido publicado. Solo tú puedes verlo.");
	}

}
