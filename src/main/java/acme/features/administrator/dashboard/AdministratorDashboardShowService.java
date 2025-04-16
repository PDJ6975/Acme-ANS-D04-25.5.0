
package acme.features.administrator.dashboard;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.AdministratorDashboard;

@GuiService
public class AdministratorDashboardShowService extends AbstractGuiService<Administrator, AdministratorDashboard> {

	@Autowired
	AdministratorDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AdministratorDashboard dashboard = new AdministratorDashboard();

		// 1. Transformación de lista de aeropuertos en un Map
		List<Object[]> airportsData = this.repository.findAirportsByOperationalScope();
		Map<String, Long> airportsByOperationalScope = new HashMap<>();
		for (Object[] row : airportsData)
			// row[0]: operational scope, row[1]: count
			airportsByOperationalScope.put(row[0].toString(), (Long) row[1]);
		dashboard.setAirportsByOperationalScope(airportsByOperationalScope);

		// 2. Transformación de lista de aerolíneas en un Map
		List<Object[]> airlinesData = this.repository.findAirlinesByType();
		Map<String, Long> airlinesByType = new HashMap<>();
		for (Object[] row : airlinesData)
			airlinesByType.put(row[0].toString(), (Long) row[1]);
		dashboard.setAirlinesByType(airlinesByType);

		// 3. Ratio de aerolíneas con email y teléfono
		dashboard.setRatioAirlinesWithEmailAndPhone(this.repository.findRatioAirlinesWithEmailAndPhone());

		// 4. Ratios de aeronaves activas y no activas
		Double activeRatio = this.repository.findAircraftActiveRatio();
		Double nonActiveRatio = this.repository.findAircraftNonActiveRatio();
		Map<String, Double> aircraftsActiveRatios = new HashMap<>();
		aircraftsActiveRatios.put("ACTIVE", activeRatio);
		aircraftsActiveRatios.put("NON_ACTIVE", nonActiveRatio);
		dashboard.setAircraftsActiveRatios(aircraftsActiveRatios);

		// 5. Ratio de reseñas con score mayor a 5.00
		dashboard.setRatioReviewsAboveFive(this.repository.findRatioReviewsAboveFive());

		// 6. Estadísticas de reseñas en las últimas 10 semanas
		// Se calcula la fecha de hace 10 semanas.
		Date tenWeeksAgo = new Date(System.currentTimeMillis() - 70L * 24L * 60L * 60L * 1000L);
		System.out.println(tenWeeksAgo);
		System.out.println(System.currentTimeMillis());
		dashboard.setReviewsCount(this.repository.findReviewsCountLast10Weeks(tenWeeksAgo));
		dashboard.setReviewsAverage(this.repository.findReviewsAverageLast10Weeks(tenWeeksAgo));
		dashboard.setReviewsMinimum(this.repository.findReviewsMinimumLast10Weeks(tenWeeksAgo));
		dashboard.setReviewsMaximum(this.repository.findReviewsMaximumLast10Weeks(tenWeeksAgo));
		dashboard.setReviewsStandardDeviation(this.repository.findReviewsStdDevLast10Weeks(tenWeeksAgo));

		// Se añade el objeto dashboard al response.
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "airportsByOperationalScope", "airlinesByType", "ratioAirlinesWithEmailAndPhone", "aircraftsActiveRatios", "ratioReviewsAboveFive", "reviewsCount", "reviewsAverage", "reviewsMinimum",
			"reviewsMaximum", "reviewsStandardDeviation");

		super.getResponse().addData(dataset);
	}
}
