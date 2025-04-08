
package acme.features.authenticated.review;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.reviews.Review;

@GuiService
public class AnyReviewListService extends AbstractGuiService<Authenticated, Review> {

	@Autowired
	protected AnyReviewRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Review> reviews;

		Date oneYearAgo = MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS);

		reviews = this.repository.findReviewsFromLastYear(oneYearAgo);

		super.getBuffer().addData(reviews);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "subject", "moment", "score", "recommended");
		super.getResponse().addData(dataset);
	}
}
