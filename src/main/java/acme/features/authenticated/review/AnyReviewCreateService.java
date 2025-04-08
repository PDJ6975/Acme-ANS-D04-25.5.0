
package acme.features.authenticated.review;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.reviews.Review;

@GuiService
public class AnyReviewCreateService extends AbstractGuiService<Authenticated, Review> {

	@Autowired
	protected AnyReviewRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Review review;

		AbstractRealm realm = super.getRequest().getPrincipal().getActiveRealm();

		review = new Review();
		review.setMoment(MomentHelper.getCurrentMoment());
		review.setUserAccount(realm.getUserAccount());
		review.setRecommended(true);

		super.getBuffer().addData(review);
	}

	@Override
	public void bind(final Review review) {
		super.bindObject(review, "name", "subject", "text", "score", "recommended");
	}

	@Override
	public void validate(final Review review) {

		if (!super.getBuffer().getErrors().hasErrors("name"))
			super.state(!review.getName().isEmpty(), "name", "authenticated.review.error.empty-name");

		if (!super.getBuffer().getErrors().hasErrors("subject"))
			super.state(!review.getSubject().isEmpty(), "subject", "authenticated.review.error.empty-subject");

		if (!super.getBuffer().getErrors().hasErrors("text"))
			super.state(!review.getText().isEmpty(), "text", "authenticated.review.error.empty-text");

		if (!super.getBuffer().getErrors().hasErrors("score") && review.getScore() != null)
			super.state(review.getScore() >= 0.0 && review.getScore() <= 10.0, "score", "authenticated.review.error.invalid-score");

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "authenticated.review.error.confirmation-required");
	}

	@Override
	public void perform(final Review review) {
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		review.setMoment(moment);

		this.repository.save(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;
		SelectChoices recommendedOptions;

		recommendedOptions = new SelectChoices();
		recommendedOptions.add("true", "Yes", review.getRecommended());
		recommendedOptions.add("false", "No", !review.getRecommended());

		dataset = super.unbindObject(review, "name", "subject", "text", "score", "recommended");
		dataset.put("confirmation", false);
		dataset.put("recommendedOptions", recommendedOptions);

		super.getResponse().addData(dataset);
	}
}
