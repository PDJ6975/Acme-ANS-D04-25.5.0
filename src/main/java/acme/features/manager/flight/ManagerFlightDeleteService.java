
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.airlines.Airline;
import acme.entities.assignments.FlightAssignment;
import acme.entities.bookings.Booking;
import acme.entities.claims.Claim;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.passengers.Passenger;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.weathers.Weather;
import acme.realms.managers.Manager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	protected ManagerFlightRepository repository;


	@Override
	public void authorise() {

		// OBTENER EL FLIGHT
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findOneById(flightId);

		boolean status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findOneById(flightId);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight);
	}

	@Override
	public void validate(final Flight flight) {

		//OBTENER TODOS LOS OBJETOS RELACIONADOS CON EL FLIGHT
		List<Booking> bookings = this.repository.findBookingsByFlightId(flight.getId());

		List<Passenger> passengers = this.repository.findPassengersByBookings(bookings);

		Weather weather = this.repository.findWeatherByFlightId(flight.getId());

		List<Leg> legs = this.repository.findLegsByFlightId(flight.getId());

		List<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentsByLegs(legs);

		List<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignments(flightAssignments);

		List<Claim> claims = this.repository.findClaimByLegs(legs);

		List<TrackingLog> trackingLogs = this.repository.findTrackingLogsByClaim(claims);

		//COMPROBAR LOS DRAFTMODE DE CADA UNO DE LOS ATRIBUTOS

		boolean flightInDraftMode = flight.isDraftMode();

		boolean allBookingsInDraftMode = bookings.stream().allMatch(Booking::isDraftMode);

		boolean allPassengersInDraftMode = passengers.stream().allMatch(Passenger::isDraftMode);

		boolean allLegsInDraftMode = legs.stream().allMatch(Leg::isDraftMode);

		boolean allFlightAssignmentsInDraftMode = flightAssignments.stream().allMatch(FlightAssignment::isDraftMode);

		boolean allActivityLogsInDraftMode = activityLogs.stream().allMatch(ActivityLog::isDraftMode);

		boolean allClaimsInDraftMode = claims.stream().allMatch(claim -> Boolean.TRUE.equals(claim.getDraftMode()));

		boolean allTrackingLogsInDraftMode = trackingLogs.stream().allMatch(log -> Boolean.TRUE.equals(log.isDraftMode()));

		super.state(flightInDraftMode, "*", "administrator.flight.error.draftMode-false");

		super.state(allBookingsInDraftMode, "*", "administrator.flight.error.bookingDraftMode-false");

		super.state(allPassengersInDraftMode, "*", "administrator.flight.error.passengersDraftMode-false");

		super.state(allLegsInDraftMode, "*", "administrator.flight.error.legDraftMode-false");

		super.state(allFlightAssignmentsInDraftMode, "*", "administrator.flight.error.flightAssignmentDraftMode-false");

		super.state(allActivityLogsInDraftMode, "*", "administrator.flight.error.activityLogDraftMode-false");

		super.state(allClaimsInDraftMode, "*", "administrator.flight.error.claimDraftMode-false");

		super.state(allTrackingLogsInDraftMode, "*", "administrator.flight.error.trackingLogDraftMode-false");
	}

	@Override
	public void perform(final Flight flight) {
		// Únicamente eliminando el Flight, siempre y cuando se cumpla el validate(), se eliminarán todos los hijos directos o indirectos debido a los ondelete=cascade
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers", "draftMode", "airline");
		dataset.put("masterId", flight.getId());

		Collection<Airline> availableAirlines = this.repository.findAllAirlines();
		SelectChoices airlines = SelectChoices.from(availableAirlines, "name", flight.getAirline());
		dataset.put("airlines", airlines);

		super.getResponse().addData(dataset);
	}

}
