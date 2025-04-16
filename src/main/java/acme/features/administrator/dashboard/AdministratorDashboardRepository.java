
package acme.features.administrator.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface AdministratorDashboardRepository extends AbstractRepository {

	// 1. Aeropuertos por alcance operativo: ahora retorna una lista de Object[]
	@Query("SELECT a.operationalScope, COUNT(a) FROM Airport a GROUP BY a.operationalScope")
	List<Object[]> findAirportsByOperationalScope();

	// 2. Aerolíneas por tipo: ahora retorna una lista de Object[]
	@Query("SELECT a.type, COUNT(a) FROM Airline a GROUP BY a.type")
	List<Object[]> findAirlinesByType();

	// 3. Ratio de aerolíneas que tienen tanto email como teléfono
	@Query("SELECT (SUM(CASE WHEN (a.emailAddress IS NOT NULL AND a.phoneNumber IS NOT NULL) THEN 1 ELSE 0 END) * 1.0) / COUNT(a) FROM Airline a")
	Double findRatioAirlinesWithEmailAndPhone();

	// 4a. Ratio de aeronaves activas (suponiendo que 'ACTIVE' es el estado activo)
	@Query("SELECT (COUNT(a) * 1.0 / (SELECT COUNT(a1) FROM Aircraft a1)) FROM Aircraft a WHERE a.aircraftStatus = 'ACTIVE'")
	Double findAircraftActiveRatio();

	// 4b. Ratio de aeronaves no activas
	@Query("SELECT (COUNT(a) * 1.0 / (SELECT COUNT(a1) FROM Aircraft a1)) FROM Aircraft a WHERE a.aircraftStatus <> 'ACTIVE'")
	Double findAircraftNonActiveRatio();

	// 5. Ratio de reseñas con score mayor a 5.00 (considerando solo cuando score no es nulo)
	@Query("SELECT (SUM(CASE WHEN r.score > 5 THEN 1 ELSE 0 END) * 1.0) / COUNT(r) FROM Review r WHERE r.score IS NOT NULL")
	Double findRatioReviewsAboveFive();

	// 6a. Cantidad de reseñas en las últimas 10 semanas
	@Query("SELECT COUNT(r) FROM Review r WHERE r.moment >= :since")
	Long findReviewsCountLast10Weeks(@Param("since") Date since);

	// 6b. Promedio de score de las reseñas de las últimas 10 semanas
	@Query("SELECT AVG(r.score) FROM Review r WHERE r.moment >= :since AND r.score IS NOT NULL")
	Double findReviewsAverageLast10Weeks(@Param("since") Date since);

	// 6c. Mínimo score de las reseñas de las últimas 10 semanas
	@Query("SELECT MIN(r.score) FROM Review r WHERE r.moment >= :since AND r.score IS NOT NULL")
	Double findReviewsMinimumLast10Weeks(@Param("since") Date since);

	// 6d. Máximo score de las reseñas de las últimas 10 semanas
	@Query("SELECT MAX(r.score) FROM Review r WHERE r.moment >= :since AND r.score IS NOT NULL")
	Double findReviewsMaximumLast10Weeks(@Param("since") Date since);

	// 6e. Desviación estándar del score de las reseñas de las últimas 10 semanas
	// (Algunas bases de datos soportan STDDEV en JPQL; si no, se podría usar un query nativo).
	@Query("SELECT STDDEV(r.score) FROM Review r WHERE r.moment >= :since AND r.score IS NOT NULL")
	Double findReviewsStdDevLast10Weeks(@Param("since") Date since);
}
