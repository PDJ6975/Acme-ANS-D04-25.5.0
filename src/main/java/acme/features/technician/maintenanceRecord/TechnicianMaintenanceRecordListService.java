
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecords;
		int technicianId;
		boolean mine;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		mine = super.getRequest().hasData("mine", boolean.class);

		if (mine)
			maintenanceRecords = this.repository.findAllMaintenanceRecordsByTechnicianId(technicianId);
		else
			maintenanceRecords = this.repository.findPublishedMaintenanceRecords();

		super.getBuffer().addData(maintenanceRecords);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		boolean mine = super.getRequest().hasData("mine", boolean.class) && super.getRequest().getData("mine", boolean.class);
		boolean showCreate = mine;
		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDue");
		super.addPayload(dataset, maintenanceRecord, //
			"estimatedCost", "notes", "draftMode", "aircraft.model", //
			"aircraft.registrationNumber", "technician.identity.fullName",//
			"technician.licenseNumber", "technician.phoneNumber");

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("mine", mine);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

	@Override
	public void unbind(final Collection<MaintenanceRecord> maintenanceRecords) {
		boolean mine = super.getRequest().hasData("mine", boolean.class) && super.getRequest().getData("mine", boolean.class);
		boolean showCreate = mine;

		super.getResponse().addGlobal("mine", mine);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
