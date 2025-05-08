
package acme.features.administrator.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@GuiController
public class AdminMaintenanceRecordController extends AbstractGuiController<Administrator, MaintenanceRecord> {

	@Autowired
	private AdminMaintenanceRecordListService	listService;

	@Autowired
	private AdminMaintenanceRecordShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
