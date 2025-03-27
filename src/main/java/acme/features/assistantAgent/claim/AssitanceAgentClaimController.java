
package acme.features.assistantAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.agents.AssistanceAgent;

@GuiController
public class AssitanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimListService	listService;

	@Autowired
	private AssistanceAgentClaimServiceShow	showService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("completed-list", "list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
