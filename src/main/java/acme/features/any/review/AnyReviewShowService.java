
package acme.features.any.review;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.reviews.Review;

@GuiService
public class AnyReviewShowService extends AbstractGuiService<Any, Review> {

	@Autowired
	protected AnyReviewRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Review review = this.repository.findReviewById(id);

		super.getResponse().setAuthorised(review != null);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Review review = this.repository.findReviewById(id);

		super.getBuffer().addData(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset = super.unbindObject(review, "name", "subject", "moment", "text", "score", "recommended", "userAccount.username");

		super.getResponse().addData(dataset);
	}
}
