
package acme.features.assistantAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.State;
import acme.entities.claims.Type;
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
		dataset = super.unbindObject(claim, "resgistrationMoment", "passengerEmail", "description", "type", "state", "assistanceAgent.userAccount.username", "userAccount.username", "leg.flightNumber", "draftMode");
		SelectChoices claimType = SelectChoices.from(Type.class, claim.getType());
		SelectChoices claimState = SelectChoices.from(State.class, claim.getState());
		dataset.put("claimType", claimType);
		dataset.put("claimState", claimState);
		super.getResponse().addData(dataset);
	}

}
