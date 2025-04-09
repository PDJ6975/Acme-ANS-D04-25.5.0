
package acme.features.administrator.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.services.Service;

@GuiController
public class AdministratorServiceController extends AbstractGuiController<Administrator, Service> {

	@Autowired
	private AdministratorServiceListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
