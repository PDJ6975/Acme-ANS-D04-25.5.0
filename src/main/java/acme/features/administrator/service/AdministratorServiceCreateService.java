
package acme.features.administrator.service;

import java.time.ZoneId;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.entities.services.Service;

@GuiService
public class AdministratorServiceCreateService extends AbstractGuiService<Administrator, Service> {

	@Autowired
	protected AdministratorServiceRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Service service = new Service();
		super.getBuffer().addData(service);
	}

	@Override
	public void bind(final Service service) {
		int airportId = super.getRequest().getData("airport", int.class);
		Airport airport = this.repository.findAirportById(airportId);

		super.bindObject(service, "name", "link", "averageDwellTime", "promotionCode", "discountMoney");
		service.setAirport(airport);
	}

	@Override
	public void validate(final Service service) {

		String promotionCode = service.getPromotionCode();
		Money discountMoney = service.getDiscountMoney();

		if (promotionCode != null && !promotionCode.isBlank()) {
			// Validar que el código sea único
			Service existing = this.repository.findServiceByPromotionCode(promotionCode);
			boolean isSame = existing == null || existing.getId() == service.getId();
			super.state(isSame, "promotionCode", "administrator.service.error.duplicate-promotion-code");

			// Validar que haya descuento si hay código
			super.state(discountMoney != null, "discountMoney", "administrator.service.error.discount-required");

			// Obtener el año actual desde MomentHelper
			String expectedYear = String.valueOf(MomentHelper.getCurrentMoment().toInstant().atZone(ZoneId.systemDefault()).getYear()).substring(2);

			// Validar que el código termina con el año actual
			boolean endsWithCorrectYear = promotionCode.matches("^.*-" + expectedYear + "$");
			super.state(endsWithCorrectYear, "promotionCode", "administrator.service.error.year-mismatch");
		}

		// Si hay descuento sin código
		if ((promotionCode == null || promotionCode.isBlank()) && discountMoney != null)
			super.state(false, "promotionCode", "administrator.service.error.promotion-code-required");

		// Un servicio de se debe asociar a un aeropuerto
		super.state(service.getAirport() != null, "airport", "administrator.service.error.airport-required");
	}

	@Override
	public void perform(final Service service) {
		this.repository.save(service);
	}

	@Override
	public void unbind(final Service service) {
		Collection<Airport> airports = this.repository.findAllAirports();
		SelectChoices choices = SelectChoices.from(airports, "iataCode", service.getAirport());

		Dataset dataset = super.unbindObject(service, "name", "link", "averageDwellTime", "promotionCode", "discountMoney");
		dataset.put("airport", choices.getSelected().getKey());
		dataset.put("airports", choices);

		super.getResponse().addData(dataset);
	}
}
