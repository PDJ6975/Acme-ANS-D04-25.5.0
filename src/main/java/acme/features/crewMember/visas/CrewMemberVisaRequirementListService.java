
package acme.features.crewMember.visas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.visas.VisaRequirement;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberVisaRequirementListService extends AbstractGuiService<FlightCrewMember, VisaRequirement> {

	@Autowired
	protected CrewMemberVisaRequirementRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		// Obtenemos el ID del FlightCrewMember
		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Sacamos los países de destino de sus FlightAssignments
		List<String> countries = this.repository.findDestinationCountriesByCrewMemberId(crewMemberId);

		// Obtenemos los VisaRequirements de dichos países
		List<VisaRequirement> requirements = countries.isEmpty() ? List.of() : this.repository.findVisaRequirementsByCountries(countries);

		// Añadimos al buffer
		super.getBuffer().addData(requirements);
	}

	@Override
	public void unbind(final VisaRequirement vr) {
		// Extraemos los campos
		Dataset dataset = super.unbindObject(vr, "passportCountry", "destinationCountry", "visaType", "stayDuration", "passportValidity");

		// Añadimos al response
		super.getResponse().addData(dataset);
	}
}
