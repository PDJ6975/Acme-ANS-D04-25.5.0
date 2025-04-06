
package acme.features.customer.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		CustomerDashboard dashboard = new CustomerDashboard();

		List<String> lastFive = this.repository.findLastFiveDestinations(customerId);
		if (lastFive != null) {
			if (lastFive.size() > 5)
				lastFive = lastFive.subList(0, 5);
		} else {
			lastFive = new ArrayList<>();
		}
		dashboard.setLastFiveDestinations(lastFive);

		Double moneySpent = this.repository.totalMoneySpentLastYear(customerId);
		dashboard.setMoneySpentLastYear(moneySpent != null ? moneySpent : 0.0);

		Map<String, Long> bookingsByTravelClass = new HashMap<>();
		List<Object[]> results = this.repository.bookingsByTravelClass(customerId);
		if (results != null) {
			for (Object[] row : results) {
				bookingsByTravelClass.put(row[0] != null ? row[0].toString() : "N/A", 
									row[1] != null ? (Long)row[1] : 0L);
			}
		}
		dashboard.setBookingsByTravelClass(bookingsByTravelClass);

		Long bookingCostCount = this.repository.bookingCostCount(customerId);
		dashboard.setBookingCostCount(bookingCostCount != null ? bookingCostCount : 0L);
		
		Double bookingCostAverage = this.repository.bookingCostAverage(customerId);
		dashboard.setBookingCostAverage(bookingCostAverage != null ? bookingCostAverage : 0.0);
		
		Double bookingCostMinimum = this.repository.bookingCostMinimum(customerId);
		dashboard.setBookingCostMinimum(bookingCostMinimum != null ? bookingCostMinimum : 0.0);
		
		Double bookingCostMaximum = this.repository.bookingCostMaximum(customerId);
		dashboard.setBookingCostMaximum(bookingCostMaximum != null ? bookingCostMaximum : 0.0);
		
		Double bookingCostStdDev = this.repository.bookingCostStandardDeviation(customerId);
		dashboard.setBookingCostStandardDeviation(bookingCostStdDev != null ? bookingCostStdDev : 0.0);

		Long passengerCount = this.repository.passengerCount(customerId);
		dashboard.setPassengerCount(passengerCount != null ? passengerCount : 0L);

		Double passengerAverage = this.repository.passengerAverage(customerId);
		dashboard.setPassengerAverage(passengerAverage != null ? passengerAverage : 0.0);

		Long minPassengers = this.repository.passengerMinimum(customerId);
		dashboard.setPassengerMinimum(minPassengers != null ? minPassengers.doubleValue() : 0.0);

		Long maxPassengers = this.repository.passengerMaximum(customerId);
		dashboard.setPassengerMaximum(maxPassengers != null ? maxPassengers.doubleValue() : 0.0);

		Double passengerStdDev = this.repository.passengerStandardDeviation(customerId);
		dashboard.setPassengerStandardDeviation(passengerStdDev != null ? passengerStdDev : 0.0);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentLastYear", "bookingsByTravelClass", "bookingCostCount", "bookingCostAverage", "bookingCostMinimum", "bookingCostMaximum", "bookingCostStandardDeviation",
			"passengerCount", "passengerAverage", "passengerMinimum", "passengerMaximum", "passengerStandardDeviation");

		super.getResponse().addData(dataset);
	}
}
