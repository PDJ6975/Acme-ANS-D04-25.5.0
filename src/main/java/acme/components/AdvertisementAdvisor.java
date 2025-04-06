
package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.advertisements.Advertisement;

@ControllerAdvice
public class AdvertisementAdvisor {

	@Autowired
	private AdvertisementRepository repository;


	@ModelAttribute("advertisement")
	public Advertisement getAdvertisement() {
		Advertisement result;

		try {
			result = this.repository.findRandomAdvertisement();
		} catch (final Throwable oops) {
			result = null;
		}

		return result;
	}

}
