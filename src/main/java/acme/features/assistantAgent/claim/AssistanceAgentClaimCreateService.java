
package acme.features.assistantAgent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import com.acme.spam.detection.SpamDetector;

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
	private AssistanceAgentClaimRepository	repository;

	@Autowired
	private SpamDetector					spamDetector;


	@Override
	public void authorise() {
		boolean auth = true;
		super.getResponse().setAuthorised(auth);
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
		String legflightNumber;
		Leg leg;
		String username;
		UserAccount user;

		username = super.getRequest().getData("userAccount.username", String.class);
		user = this.repository.findUserAccountByUsername(username);
		legflightNumber = super.getRequest().getData("leg.flightNumber", String.class);
		leg = this.repository.findLegByFlightNumber(legflightNumber);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setLeg(leg);
		claim.setUserAccount(user);

	}

	@Override
	public void validate(final Claim claim) {
		String legFlightNumber;
		String username;
		UserAccount user;
		Type t;

		legFlightNumber = super.getRequest().getData("leg.flightNumber", String.class);
		Leg leg = this.repository.findLegByFlightNumber(legFlightNumber);

		username = super.getRequest().getData("userAccount.username", String.class);
		user = this.repository.findUserAccountByUsername(username);

		if (!(claim.getType() == null))
			t = super.getRequest().getData("type", Type.class);

		if (username == null || username.trim().isEmpty())
			super.state(false, "*", "assistant-agent.create.user-cant-be-null");

		if (!(username == null || username.trim().isEmpty()))
			if (user != null)
				super.state(user.hasRealmOfType(Customer.class), "*", "assistant-agent.create.user-not-passenger-or-customer");
			else
				super.state(false, "*", "assistant-agent.create.user-not-exist");

		if (legFlightNumber == null || legFlightNumber.trim().isEmpty())
			super.state(false, "*", "assistant-agent.create.leg-cant-be-null");

		if (!(legFlightNumber == null || legFlightNumber.trim().isEmpty()))
			if (leg != null)
				super.state(!leg.isDraftMode(), "*", "assistant-agent.create.leg-is-not-published");
			else
				super.state(false, "*", "assistant-agent.create.leg-not-exist");

		if (!super.getBuffer().getErrors().hasErrors("description")) {
			boolean isSpamFn = this.spamDetector.isSpam(claim.getDescription());
			super.state(!isSpamFn, "description", "customer.passenger.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("passengerEmail")) {
			boolean isSpamSn = this.spamDetector.isSpam(claim.getPassengerEmail());
			super.state(!isSpamSn, "passengerEmail", "customer.passenger.error.spam");
		}

		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		dataset = super.unbindObject(claim, "passengerEmail", "description", "type", "userAccount.username", "leg.flightNumber");
		SelectChoices claimType = SelectChoices.from(Type.class, claim.getType());
		SelectChoices claimState = SelectChoices.from(State.class, claim.getState());
		dataset.put("claimType", claimType);
		dataset.put("claimState", claimState);
		super.getResponse().addData(dataset);
	}

}
