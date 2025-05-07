
package acme.features.administrator.bannedPassanger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.BannedPassenger;

@GuiService
public class AdministratorBannedPassengerCreateService extends AbstractGuiService<Administrator, BannedPassenger> {

	@Autowired
	private AdministratorBannedPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		BannedPassenger bp;

		bp = new BannedPassenger();
		bp.setFullName(null);
		bp.setPassportNumber(null);
		bp.setDateOfBirth(null);
		bp.setNationality(null);
		bp.setReason(null);
		bp.setBanDate(null);
		bp.setLiftDate(null);
		super.getBuffer().addData(bp);

	}

	@Override
	public void bind(final BannedPassenger bp) {
		super.bindObject(bp, "fullName", "passportNumber", "dateOfBirth", "nationality", "reason", "banDate");

	}

	@Override
	public void validate(final BannedPassenger bp) {

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
