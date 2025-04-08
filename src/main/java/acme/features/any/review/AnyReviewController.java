
package acme.features.any.review;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.client.controllers.RestController;
import acme.entities.reviews.Review;

@GuiController
@RestController
public class AnyReviewController extends AbstractGuiController<Any, Review> {

	@Autowired
	private AnyReviewShowService	showService;

	@Autowired
	private AnyReviewListService	listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
