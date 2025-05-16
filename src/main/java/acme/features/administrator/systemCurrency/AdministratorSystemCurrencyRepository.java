
package acme.features.administrator.systemCurrency;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfigurations.SystemCurrency;

@Repository
public interface AdministratorSystemCurrencyRepository extends AbstractRepository {

	@Query("SELECT sc FROM SystemCurrency sc")
	SystemCurrency findSystemConfiguration();
}
