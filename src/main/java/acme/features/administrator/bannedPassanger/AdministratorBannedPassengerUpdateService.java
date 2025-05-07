
package acme.features.administrator.bannedPassanger;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.BannedPassenger;

@GuiService
public class AdministratorBannedPassengerUpdateService extends AbstractGuiService<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		BannedPassenger bp;
		int id = super.getRequest().getData("id", int.class);
		bp = this.repository.findBannedPassenger(id);
		super.getBuffer().addData(bp);

	}

	@Override
	public void bind(final BannedPassenger bp) {
		super.bindObject(bp, "fullName", "passportNumber", "dateOfBirth", "nationality", "reason", "banDate", "liftDate");

	}

	@Override
	public void validate(final BannedPassenger bp) {
		Date banDate;
		Date liftDate;
		banDate = super.getRequest().getData("banDate", Date.class);
		liftDate = super.getRequest().getData("liftDate", Date.class);
		if (liftDate != null && liftDate.before(banDate))
			super.state(false, "*", "administrator.update.lift-date-cant-be-before");
		;
	}

	@Override
	public void perform(final BannedPassenger bp) {
		this.repository.save(bp);
	}

	@Override
	public void unbind(final BannedPassenger bp) {
		Dataset dataset;
		dataset = super.unbindObject(bp, "fullName", "passportNumber", "dateOfBirth", "nationality", "reason", "banDate", "liftDate");
		super.getResponse().addData(dataset);
	}
}
