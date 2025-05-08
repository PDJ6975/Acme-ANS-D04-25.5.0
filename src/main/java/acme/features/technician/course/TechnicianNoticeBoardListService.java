
package acme.features.technician.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.noticeBoards.NoticeBoard;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianNoticeBoardListService extends AbstractGuiService<Technician, NoticeBoard> {

	@Autowired
	protected TechnicianNoticeBoardRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<NoticeBoard> noticeBoards = this.repository.findAllNoticeBoards();
		super.getBuffer().addData(noticeBoards);
	}

	@Override
	public void unbind(final NoticeBoard nb) {
		// Se extraen los campos para el listado; puedes ajustar los nombres de campo según tus necesidades.
		Dataset dataset = super.unbindObject(nb, "title", "headline", "postedDate");
		super.getResponse().addData(dataset);
	}
}
