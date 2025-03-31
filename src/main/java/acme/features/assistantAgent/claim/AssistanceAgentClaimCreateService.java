
package acme.features.assistantAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.State;
import acme.entities.claims.Type;
import acme.entities.legs.Leg;
import acme.realms.Customer;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Claim claim;
		String agentName;
		AssistanceAgent agent;

		agentName = super.getRequest().getPrincipal().getUsername();
		agent = this.repository.findAgentByUsername(agentName);

		claim = new Claim();
		claim.setResgistrationMoment(MomentHelper.getCurrentMoment());
		claim.setPassengerEmail("");
		claim.setDescription("");
		claim.setType(Type.OTHER_ISSUES);
		claim.setState(State.ONGOING);
		claim.setUserAccount(null);
		claim.setLeg(null);
		claim.setDraftMode(true);
		claim.setAssistanceAgent(agent);
		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;
		String username;
		UserAccount user;

		username = super.getRequest().getData("userAccount.username", String.class);
		user = this.repository.findUserAccountByUsername(username);
		legId = super.getRequest().getData("leg.id", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "resgistrationMoment", "passengerEmail", "description", "type", "state", "assistanceAgent.userAccount.username");
		claim.setLeg(leg);
		claim.setUserAccount(user);

	}

	@Override
	public void validate(final Claim claim) {
		boolean status;
		int legId;
		String username;
		UserAccount user;

		legId = super.getRequest().getData("leg.id", int.class);
		Leg leg = this.repository.findLegById(legId);

		username = super.getRequest().getData("userAccount.username", String.class);
		user = this.repository.findUserAccountByUsername(username);

		status = leg != null && user.hasRealmOfType(Customer.class);

		if (!user.hasRealmOfType(Customer.class))
			super.state(status, "*", "assistant-agent.create.user-not-passenger-or-customer");
		else
			super.state(status, "*", "assistant-agent.create.leg-not-exist");

		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		dataset = super.unbindObject(claim, "resgistrationMoment", "passengerEmail", "description", "type", "state", "assistanceAgent.userAccount.username", "userAccount.username", "leg.id");
		SelectChoices claimType = SelectChoices.from(Type.class, claim.getType());
		dataset.put("claimType", claimType);
		super.getResponse().addData(dataset);
	}

}
