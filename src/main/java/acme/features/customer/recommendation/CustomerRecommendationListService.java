
package acme.features.customer.recommendation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.recommendations.Recommendation;
import acme.realms.Customer;

@Service
public class CustomerRecommendationListService extends AbstractGuiService<Customer, Recommendation> {

	@Autowired
	protected CustomerRecommendationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Recommendation> recommendations = this.repository.findByCustomerId(customerId);
		super.getBuffer().addData(recommendations);
	}

	@Override
	public void unbind(final Recommendation recommendation) {
		Dataset dataset = super.unbindObject(recommendation, "name", "category", "city", "externalLink");
		super.getResponse().addData(dataset);
	}
}
