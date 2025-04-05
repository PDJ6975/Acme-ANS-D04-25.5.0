
package acme.features.authenticated.crewMember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.realms.members.AvailabilityStatus;
import acme.realms.members.FlightCrewMember;

@GuiService
public class AuthenticatedFlightCrewMemberCreateService extends AbstractGuiService<Authenticated, FlightCrewMember> {

	@Autowired
	protected AuthenticatedFlightCrewMemberRepository repository;


	@Override
	public void authorise() {
		boolean status = !super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		UserAccount userAccount = this.repository.findOneUserAccountById(userAccountId);

		FlightCrewMember member = new FlightCrewMember();
		member.setUserAccount(userAccount);
		member.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

		super.getBuffer().addData(member);
	}

	@Override
	public void bind(final FlightCrewMember member) {
		super.bindObject(member, "employeeCode", "phoneNumber", "languageSkills", "salary", "yearsOfExperience", "availabilityStatus");

		int airlineId = super.getRequest().getData("airlineId", int.class);
		Airline airline = this.repository.findAirlineById(airlineId);
		member.setAirline(airline);

	}

	@Override
	public void validate(final FlightCrewMember member) {
		boolean duplicateCode = this.repository.existsByEmployeeCode(member.getEmployeeCode());
		super.state(!duplicateCode, "employeeCode", "authenticated.flight-crew-member.error.duplicate-code");

		super.state(member.getAirline() != null, "airlineId", "authenticated.flight-crew-member.error.null-airline");
	}

	@Override
	public void perform(final FlightCrewMember member) {
		this.repository.save(member);
	}

	@Override
	public void unbind(final FlightCrewMember member) {
		Dataset dataset = super.unbindObject(member, "employeeCode", "phoneNumber", "languageSkills", "salary", "yearsOfExperience", "availabilityStatus");

		Collection<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices choicesAirlines = SelectChoices.from(airlines, "iataCode", member.getAirline());
		SelectChoices choicesStatus = SelectChoices.from(AvailabilityStatus.class, member.getAvailabilityStatus());

		dataset.put("airlines", choicesAirlines);
		dataset.put("airlineId", choicesAirlines.getSelected().getKey());
		dataset.put("availabilityStatuses", choicesStatus);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
