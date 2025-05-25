
package acme.features.administrator.recommendation;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import acme.client.components.principals.Administrator;
import acme.client.controllers.GuiController;
import acme.client.helpers.Assert;
import acme.client.helpers.PrincipalHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.airports.Airport;
import acme.entities.recommendations.Recommendation;

@GuiController
public class AdminRecommendationPopulateController {

	private static final String						API_URL	= "https://api.foursquare.com/v3/places/search";

	@Value("${myapp.foursquare.apikey}")
	private String									apiKey;

	@Autowired
	private AdministratorRecommendationRepository	repository;


	@GetMapping("/administrator/system/populate-recommendation")
	public ModelAndView populateRecommendation() {

		Assert.state(PrincipalHelper.get().hasRealmOfType(Administrator.class), "acme.default.error.not-authorised");

		ModelAndView result;

		try {

			int insertedCount = this.doPopulateRecommendations();

			result = new ModelAndView("fragments/welcome");
			result.addObject("_globalSuccessMessage", String.format("Populate done: %d Recommendations inserted", insertedCount));

		} catch (final Throwable oops) {

			result = new ModelAndView("master/panic");
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			result.addObject("_oops", oops);
			result.addObject("_globalErrorMessage", "acme.default.global.message.error");
		}

		return result;
	}

	protected List<Recommendation> computeMockedRecommendations(final Airport airport) {
		List<Recommendation> recommendations = new ArrayList<>();

		if ("Sevilla".equalsIgnoreCase(airport.getCity())) {
			Recommendation r1 = new Recommendation();
			r1.setName("Plaza de España");
			r1.setCategory("Monument");
			r1.setAddress("Av. Isabel la Católica, 41013 Sevilla, Spain");
			r1.setCity("Sevilla");
			r1.setCountry("Spain");
			r1.setLatitude(37.3772);
			r1.setLongitude(-5.9869);
			r1.setExternalLink("https://example.com/plaza-espana");
			r1.setIconUrl("https://example.com/icon1.png");
			r1.setAirport(airport);
			recommendations.add(r1);
		}

		return recommendations;
	}

	protected int doPopulateRecommendations() throws IOException, InterruptedException {
		List<Airport> airports = this.repository.findAllAirports();
		int insertedCount = 0;

		for (Airport airport : airports)

			if (airport.getCity() != null && !airport.getCity().isEmpty() && !airport.getCity().toLowerCase().contains("lorem")) {

				boolean useMock = SpringHelper.isRunningOn("development") || SpringHelper.isRunningOn("tester") || SpringHelper.isRunningOn("tester#replay");

				List<Recommendation> recommendations = useMock ? this.computeMockedRecommendations(airport) : this.fetchRecommendations(airport);

				for (Recommendation recommendation : recommendations)
					if (!this.repository.existsByNameAndAirport(recommendation.getName(), airport.getId())) {
						this.repository.save(recommendation);
						insertedCount++;
					}
			}

		return insertedCount;
	}

	private List<Recommendation> fetchRecommendations(final Airport airport) throws IOException, InterruptedException {
		List<Recommendation> result = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();

		String query = URLEncoder.encode(airport.getCity(), StandardCharsets.UTF_8);
		String limit = "5";

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(AdminRecommendationPopulateController.API_URL + "?query=" + query + "&limit=" + limit)).header("Accept", "application/json").header("Authorization", this.apiKey).GET().build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200)
			result = this.parseApiResponse(response.body(), airport);
		else
			throw new RuntimeException("API responded with status: " + response.statusCode());

		return result;
	}

	private List<Recommendation> parseApiResponse(final String body, final Airport airport) throws IOException {
		List<Recommendation> recommendations = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(body);

		JsonNode results = root.path("results");
		for (JsonNode place : results) {
			Recommendation recommendation = new Recommendation();

			recommendation.setName(place.path("name").asText(""));

			if (place.has("categories") && place.path("categories").isArray() && place.path("categories").size() > 0)
				recommendation.setCategory(place.path("categories").get(0).path("name").asText(""));

			if (place.has("location")) {
				JsonNode location = place.path("location");
				String formattedAddress = location.path("formatted_address").asText("");
				recommendation.setAddress(formattedAddress);
				recommendation.setCity(airport.getCity());
				recommendation.setCountry(airport.getCountry());

				if (location.has("lat") && location.has("lng")) {
					recommendation.setLatitude(location.path("lat").asDouble());
					recommendation.setLongitude(location.path("lng").asDouble());
				}
			}

			String fsqId = place.path("fsq_id").asText("");
			if (!fsqId.isEmpty())
				recommendation.setExternalLink("https://foursquare.com/v/" + fsqId);

			if (place.has("categories") && place.path("categories").isArray() && place.path("categories").size() > 0) {
				JsonNode category = place.path("categories").get(0);
				if (category.has("icon")) {
					JsonNode icon = category.path("icon");
					String prefix = icon.path("prefix").asText("");
					String suffix = icon.path("suffix").asText("");
					recommendation.setIconUrl(prefix + "64" + suffix);
				}
			}

			recommendation.setAirport(airport);

			recommendations.add(recommendation);
		}

		return recommendations;
	}
}
