
package acme.features.authenticated.review;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.reviews.Review;

@Repository
public interface AnyReviewRepository extends AbstractRepository {

	@Query("SELECT r FROM Review r WHERE r.moment >= :oneYearAgo ORDER BY r.moment DESC")
	Collection<Review> findReviewsFromLastYear(Date oneYearAgo);

	@Query("SELECT r FROM Review r WHERE r.id = :id")
	Review findReviewById(int id);
}
