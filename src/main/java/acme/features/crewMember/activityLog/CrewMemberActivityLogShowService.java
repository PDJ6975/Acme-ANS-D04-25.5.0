
package acme.features.crewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.assignments.AssignmentStatus;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private CrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = false;
		int logId;
		ActivityLog log;

		if (super.getRequest().hasData("id", int.class)) {
			logId = super.getRequest().getData("id", int.class);
			log = this.repository.findOneById(logId);

			if (log != null) {

				// La etapa debe haber finalizado
				boolean legFinished = !log.getFlightAssignment().getLeg().getScheduledArrival().after(MomentHelper.getCurrentMoment());

				// Entendemos que una asignación solo puede tener logs si: ella y la etapa son públicas y si la asignación está confirmada (para evitar incongruencias)

				authorised = super.getRequest().getPrincipal().hasRealm(log.getFlightAssignment().getFlightCrewMember()) && log.getFlightAssignment().getAssignmentStatus() == AssignmentStatus.CONFIRMED && !log.getFlightAssignment().isDraftMode()
					&& !log.getFlightAssignment().getLeg().isDraftMode() && legFinished;
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {

		ActivityLog log;
		int logId;

		logId = super.getRequest().getData("id", int.class);
		log = this.repository.findOneById(logId);

		super.getBuffer().addData(log);

	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		dataset.put("validDraft", log.isDraftMode() && !log.getFlightAssignment().isDraftMode() && !log.getFlightAssignment().getLeg().isDraftMode());
		dataset.put("masterId", log.getFlightAssignment().getId());
		super.getResponse().addData(dataset);
	}
}
