
package acme.features.customer.recommendation;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendations.Recommendation;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationShowService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	protected CustomerRecommendationRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recommendationId;
		Recommendation recommendation;

		recommendationId = super.getRequest().getData("id", int.class);
		recommendation = this.repository.findRecommendationById(recommendationId);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = recommendation != null && this.repository.isDestinationAirportForCustomer(recommendation.getAirport().getId(), customerId);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Recommendation recommendation;
		int id;

		id = super.getRequest().getData("id", int.class);
		recommendation = this.repository.findRecommendationById(id);

		super.getBuffer().addData(recommendation);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset;

		dataset = super.unbindObject(recommendation, "name", "category", "address", "city", "country", "externalLink", "iconUrl", "latitude", "longitude", "airport.name");

		if (recommendation.getLatitude() != null && recommendation.getLongitude() != null)
			dataset.put("hasLocation", true);
		else
			dataset.put("hasLocation", false);

		super.getResponse().addData(dataset);
	}
}
