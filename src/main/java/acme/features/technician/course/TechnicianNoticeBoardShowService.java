
package acme.features.technician.course;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.noticeBoards.NoticeBoard;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianNoticeBoardShowService extends AbstractGuiService<Technician, NoticeBoard> {

	@Autowired
	protected TechnicianNoticeBoardRepository repository;


	@Override
	public void authorise() {
		int noticeBoardId = super.getRequest().getData("id", int.class);
		NoticeBoard nb = this.repository.findOneById(noticeBoardId);
		boolean status = nb != null && super.getRequest().getPrincipal().hasRealmOfType(Technician.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int noticeBoardId = super.getRequest().getData("id", int.class);
		NoticeBoard nb = this.repository.findOneById(noticeBoardId);
		super.getBuffer().addData(nb);
	}

	@Override
	public void unbind(final NoticeBoard nb) {
		// Aquí se extraen todos los campos relevantes que desees mostrar en el detalle del curso.
		Dataset dataset = super.unbindObject(nb, "title", "headline", "url", "isPaid", "price", "currency", "instructorName", "instructorUrl", "instructorImage", "imageUrl", "language", "postedDate");
		super.getResponse().addData(dataset);
	}
}
