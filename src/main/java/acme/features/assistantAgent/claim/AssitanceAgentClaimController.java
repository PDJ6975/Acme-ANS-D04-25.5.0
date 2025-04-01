
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
	private AssistanceAgentClaimListService			listService;

	@Autowired
	private AssistanceAgentClaimListOngoingService	listOngoingService;

	@Autowired
	private AssistanceAgentClaimServiceShow			showService;

	@Autowired
	private AssistanceAgentClaimCreateService		createService;

	@Autowired
	private AssistanceAgentClaimUpdateService		updateService;

	@Autowired
	private AssistanceAgentClaimDeleteService		deleteService;

	@Autowired
	private AssistanceAgentClaimPublishService		publishService;


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("completed-list", "list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("ongoing-list", "list", this.listOngoingService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
