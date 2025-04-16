
package acme.features.administrator.bannedPassanger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.BannedPassenger;

@GuiService
public class AdministratorBannedPassengerShowService extends AbstractGuiService<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		BannedPassenger bp = this.repository.findBannedPassenger(id);
		super.getBuffer().addData(bp);
	}

	@Override
	public void unbind(final BannedPassenger bp) {
		Dataset dataset;

		dataset = super.unbindObject(bp, "fullName", "passportNumber", "dateOfBirth", "nationality", "reason", "banDate", "liftDate");

		super.getResponse().addData(dataset);
	}

}
