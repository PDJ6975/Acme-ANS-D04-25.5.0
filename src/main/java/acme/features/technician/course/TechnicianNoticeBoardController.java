
package acme.features.technician.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.noticeBoards.NoticeBoard;
import acme.realms.technicians.Technician;

@GuiController
public class TechnicianNoticeBoardController extends AbstractGuiController<Technician, NoticeBoard> {

	@Autowired
	private TechnicianNoticeBoardShowService	showService;

	@Autowired
	private TechnicianNoticeBoardListService	listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
