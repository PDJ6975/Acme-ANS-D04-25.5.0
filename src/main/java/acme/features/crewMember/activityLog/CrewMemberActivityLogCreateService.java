
package acme.features.crewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.FlightAssignment;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected CrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {

		int masterId;
		FlightAssignment assignment;
		FlightCrewMember crewMember;
		boolean status;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findAssignmentById(masterId);
		crewMember = assignment.getFlightCrewMember();

		status = super.getRequest().getPrincipal().hasRealm(crewMember);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int masterId;
		FlightAssignment assignment;
		ActivityLog log;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findAssignmentById(masterId);

		log = new ActivityLog();
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setTypeOfIncident("New Incident");
		log.setDescription("Turbulence");
		log.setSeverityLevel(0);
		log.setDraftMode(true);
		log.setFlightAssignment(assignment);

		super.getBuffer().addData(log);

	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		dataset.put("draftMode", log.getFlightAssignment().isDraftMode());
		dataset.put("draftLeg", true);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
