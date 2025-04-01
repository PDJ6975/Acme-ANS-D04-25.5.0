
package acme.features.assistantAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.State;
import acme.entities.claims.Type;
import acme.entities.legs.Leg;
import acme.realms.Customer;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	protected AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int claimId;
		Claim claim;
		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		auth = claim.getDraftMode() && claim.getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
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
	public void bind(final Claim claim) {
		String legflightNumber;
		Leg leg;
		String username;
		UserAccount user;

		username = super.getRequest().getData("userAccount.username", String.class);
		user = this.repository.findUserAccountByUsername(username);
		legflightNumber = super.getRequest().getData("leg.flightNumber", String.class);
		leg = this.repository.findLegByFlightNumber(legflightNumber);

		super.bindObject(claim, "passengerEmail", "description", "type", "state");
		claim.setLeg(leg);
		claim.setUserAccount(user);

	}

	@Override
	public void validate(final Claim claim) {
		boolean status;
		String legFlightNumber;
		String username;
		UserAccount user;

		legFlightNumber = super.getRequest().getData("leg.flightNumber", String.class);
		Leg leg = this.repository.findLegByFlightNumber(legFlightNumber);

		username = super.getRequest().getData("userAccount.username", String.class);
		user = this.repository.findUserAccountByUsername(username);

		if (user != null)
			status = leg != null && user.hasRealmOfType(Customer.class);
		else
			status = leg != null;

		if (user != null && !user.hasRealmOfType(Customer.class))
			super.state(status, "*", "assistant-agent.create.user-not-passenger-or-customer");
		else if (leg == null)
			super.state(status, "*", "assistant-agent.create.leg-not-exist");
		else
			super.state(status, "*", "assistant-agent.create.user-cant-be-null");
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		dataset = super.unbindObject(claim, "passengerEmail", "description", "type", "state", "userAccount.username", "leg.flightNumber", "draftMode");
		SelectChoices claimType = SelectChoices.from(Type.class, claim.getType());
		SelectChoices claimState = SelectChoices.from(State.class, claim.getState());
		dataset.put("claimType", claimType);
		dataset.put("claimState", claimState);
		super.getResponse().addData(dataset);
	}

}
