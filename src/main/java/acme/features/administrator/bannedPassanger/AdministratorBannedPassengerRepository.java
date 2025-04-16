
package acme.features.administrator.bannedPassanger;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passengers.BannedPassenger;

@Repository
public interface AdministratorBannedPassengerRepository extends AbstractRepository {

	@Query("SELECT bp FROM BannedPassenger bp WHERE bp.banDate <= :date AND (bp.liftDate IS NULL OR bp.liftDate > :date)")
	Collection<BannedPassenger> findAllCurrentBannedPassanger(Date date);

	@Query("SELECT bp FROM BannedPassenger bp WHERE bp.id = :id")
	BannedPassenger findBannedPassenger(int id);

}
