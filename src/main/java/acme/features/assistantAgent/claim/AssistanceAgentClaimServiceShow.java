
package acme.features.assistantAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimServiceShow extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int claimId;
		Claim claim;
		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		auth = claim.getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
	}

	@Override
	public void load() {
		Claim claim;
		int id;
		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);
		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		dataset = super.unbindObject(claim, "resgistrationMoment", "passengerEmail", "description", "type", "state", "assistanceAgent.userAccount.username", "userAccount.username", "leg.flightNumber");
		super.getResponse().addData(dataset);
	}

}
