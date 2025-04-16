
package acme.features.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@GuiService
public class AdminMaintenanceRecordListService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdminMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecords;

		maintenanceRecords = this.repository.findPublishedMaintenanceRecords();

		super.getBuffer().addData(maintenanceRecords);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {

		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "nextInspectionDue", "estimatedCost", "draftMode");
		dataset.put("technician", maintenanceRecord.getTechnician().getUserAccount().getIdentity().getFullName());
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("masterId", maintenanceRecord.getId());
	}
}
