
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.assignments.FlightAssignment;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.entities.trackingLogs.TrackingLog;
import acme.features.manager.flight.ManagerFlightRepository;
import acme.realms.managers.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	protected ManagerLegRepository		repository;

	@Autowired
	protected ManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {

		// OBTENER EL LEG
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);

		//OBTENER TODOS LOS OBJETOS RELACIONADOS CON EL LEG
		List<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentsByLegId(legId);

		List<ActivityLog> activityLogs = this.flightRepository.findActivityLogsByFlightAssignments(flightAssignments);

		List<Claim> claims = this.repository.findClaimsByLegId(legId);

		List<TrackingLog> trackingLogs = this.flightRepository.findTrackingLogsByClaim(claims);

		//COMPROBAR LOS DRAFTMODE DE CADA UNO DE LOS ATRIBUTOS
		boolean legInDraftMode = leg.isDraftMode();

		boolean allFlightAssignmentsInDraftMode = flightAssignments.stream().allMatch(FlightAssignment::isDraftMode);

		boolean allActivityLogsInDraftMode = activityLogs.stream().allMatch(ActivityLog::isDraftMode);

		//boolean allClaimsInDraftMode = claims.stream().allMatch(Claim::isDraftMode); COMENTADO POR QUE EL CLAIM TODAVÍA NO TIENE ATRIBUTO DRAFTMODE

		//boolean allTrackingLogsInDraftMode = trackingLogs.stream().allMatch(TrackingLog::isDraftMode); COMENTADO POR QUE EL CLAIM TODAVÍA NO TIENE ATRIBUTO DRAFTMODE

		boolean status = leg != null && legInDraftMode && allFlightAssignmentsInDraftMode && allActivityLogsInDraftMode && super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg);
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		// Únicamente eliminando el Leg, siempre y cuando se cumpla el authorise(), se eliminarán todos los hijos directos o indirectos debido a los ondelete=cascade
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "duration", "legStatus", "description", "scheduledDeparture", "scheduledArrival", "draftMode", "departureAirport", "arrivalAirport", "aircraft");

		// Añadir masterId para coherencia con create/update
		dataset.put("masterId", leg.getFlight().getId());

		// Choices necesarios para que los <acme:input-select> funcionen en modo show
		SelectChoices legStatuses = SelectChoices.from(LegStatus.class, leg.getLegStatus());
		dataset.put("legStatuses", legStatuses);

		Collection<Airport> availableAirports = this.repository.findAirports();
		SelectChoices departureAirports = SelectChoices.from(availableAirports, "name", leg.getDepartureAirport());
		dataset.put("departureAirports", departureAirports);

		SelectChoices arrivalAirports = SelectChoices.from(availableAirports, "name", leg.getArrivalAirport());
		dataset.put("arrivalAirports", arrivalAirports);

		Collection<Aircraft> availableAircrafts = this.repository.findAircrafts();
		SelectChoices aircrafts = SelectChoices.from(availableAircrafts, "registrationNumber", leg.getAircraft());
		dataset.put("aircrafts", aircrafts);

		super.getResponse().addData(dataset);
	}

}
