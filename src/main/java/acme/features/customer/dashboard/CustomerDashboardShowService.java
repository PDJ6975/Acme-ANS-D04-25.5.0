
package acme.features.customer.dashboard;

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
		if (lastFive.size() > 5)
			lastFive = lastFive.subList(0, 5);
		dashboard.setLastFiveDestinations(lastFive);

		dashboard.setMoneySpentLastYear(this.repository.totalMoneySpentLastYear(customerId));

		List<Object[]> bookingsResult = this.repository.bookingsByTravelClass(customerId);
		Map<String, Long> bookingsByTravelClass = new HashMap<>();
		for (Object[] row : bookingsResult)
			bookingsByTravelClass.put(row[0].toString(), (Long) row[1]);
		dashboard.setBookingsByTravelClass(bookingsByTravelClass);

		dashboard.setBookingCostCount(this.repository.bookingCostCount(customerId));
		dashboard.setBookingCostAverage(this.repository.bookingCostAverage(customerId));
		dashboard.setBookingCostMinimum(this.repository.bookingCostMinimum(customerId));
		dashboard.setBookingCostMaximum(this.repository.bookingCostMaximum(customerId));
		dashboard.setBookingCostStandardDeviation(this.repository.bookingCostStandardDeviation(customerId));

		dashboard.setPassengerCount(this.repository.passengerCount(customerId));
		dashboard.setPassengerAverage(this.repository.passengerAverage(customerId));

		Long minPassengers = this.repository.passengerMinimum(customerId);
		dashboard.setPassengerMinimum(minPassengers != null ? minPassengers.doubleValue() : null);

		Long maxPassengers = this.repository.passengerMaximum(customerId);
		dashboard.setPassengerMaximum(maxPassengers != null ? maxPassengers.doubleValue() : null);
		dashboard.setPassengerStandardDeviation(this.repository.passengerStandardDeviation(customerId));

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "moneySpentLastYear", "bookingsByTravelClass", "bookingCostCount", "bookingCostAverage", "bookingCostMinimum", "bookingCostMaximum", "bookingCostStandardDeviation",
			"passengerCount", "passengerAverage", "passengerMinimum", "passengerMaximum", "passengerStandardDeviation");

		super.getResponse().addData(dataset);
	}
}
