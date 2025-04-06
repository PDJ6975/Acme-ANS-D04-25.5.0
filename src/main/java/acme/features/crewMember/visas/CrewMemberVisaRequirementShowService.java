
package acme.features.crewMember.visas;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.visas.VisaRequirement;
import acme.realms.members.FlightCrewMember;

@GuiService
public class CrewMemberVisaRequirementShowService extends AbstractGuiService<FlightCrewMember, VisaRequirement> {

	@Autowired
	protected CrewMemberVisaRequirementRepository repository;


	@Override
	public void authorise() {

		int requirementId = super.getRequest().getData("id", int.class);
		VisaRequirement requirement = this.repository.findOneById(requirementId);

		boolean status = requirement != null && super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		// Se obtiene el id del VisaRequirement
		int requirementId = super.getRequest().getData("id", int.class);

		// Se busca en BD
		VisaRequirement vr = this.repository.findOneById(requirementId);

		// Metemos en el buffer
		super.getBuffer().addData(vr);
	}

	@Override
	public void unbind(final VisaRequirement vr) {
		Dataset dataset = super.unbindObject(vr, "passportCountry", "destinationCountry", "continent", "capital", "currency", "phoneCode", "timezone", "visaType", "stayDuration", "passportValidity", "additionalInfo", "officialLink");
		super.getResponse().addData(dataset);
	}
}
