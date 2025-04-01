
package acme.features.assistantAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListOngoingService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> Claims;
		int id;
		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		Claims = this.repository.findOngoingClaims(id);
		super.getBuffer().addData(Claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		String shortenedDescription = claim.getDescription();
		int maxLength = 100;

		if (shortenedDescription.length() > maxLength)
			shortenedDescription = shortenedDescription.substring(0, maxLength) + "...";

		dataset = super.unbindObject(claim, "type", "userAccount.username", "leg.flightNumber");

		dataset.put("description", shortenedDescription);

		super.getResponse().addData(dataset);
	}
}
