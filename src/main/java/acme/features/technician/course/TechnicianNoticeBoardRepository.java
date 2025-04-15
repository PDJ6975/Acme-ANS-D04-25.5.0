
package acme.features.technician.course;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.noticeBoards.NoticeBoard;

@Repository
public interface TechnicianNoticeBoardRepository extends AbstractRepository {

	// Obtiene todos los NoticeBoard (publicidades de cursos) ordenados por fecha de publicación (descendente)
	@Query("SELECT n FROM NoticeBoard n ORDER BY n.postedDate DESC")
	List<NoticeBoard> findAllNoticeBoards();

	// Obtiene un NoticeBoard concreto dado su id
	@Query("SELECT n FROM NoticeBoard n WHERE n.id = :id")
	NoticeBoard findOneById(int id);
}
