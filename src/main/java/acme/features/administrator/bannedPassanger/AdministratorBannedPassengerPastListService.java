
package acme.features.administrator.bannedPassanger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.BannedPassenger;

@GuiService
public class AdministratorBannedPassengerPastListService extends AbstractGuiService<Administrator, BannedPassenger> {

	@Autowired
	protected AdministratorBannedPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<BannedPassenger> bp = this.repository.findAllPastBannedPassanger(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(bp);
	}

	@Override
	public void unbind(final BannedPassenger bannedPassenger) {
		String shortenedDescription = bannedPassenger.getReason();
		int maxLength = 100;

		if (shortenedDescription.length() > maxLength)
			shortenedDescription = shortenedDescription.substring(0, maxLength) + "...";
		Dataset dataset = super.unbindObject(bannedPassenger, "fullName", "passportNumber");
		dataset.put("reason", shortenedDescription);
		super.getResponse().addData(dataset);
	}

}
