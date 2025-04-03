
package acme.features.assistantAgent.trackingLog;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.Indicator;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.agents.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	protected AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean auth;
		int id;
		TrackingLog log;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLogById(id);

		auth = log.isDraftMode() && log.getClaim().getAssistanceAgent().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(auth);
		;
	}

	@Override
	public void load() {
		TrackingLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		;
	}

	@Override
	public void validate(final TrackingLog log) {
		Double resolutionCreate;
		Collection<TrackingLog> logMaxResolution;

		resolutionCreate = log.getResolutionPercentage();

		logMaxResolution = this.repository.findHighestResolutionLogsByClaimId(log.getClaim().getId());

		boolean isFirst = logMaxResolution.isEmpty();

		Optional<TrackingLog> maxLog = logMaxResolution.stream().max(Comparator.comparing(TrackingLog::getResolutionPercentage));

		if (resolutionCreate != null && !StringHelper.isBlank(log.getIndicator().toString())) {
			boolean isResolutionFull = resolutionCreate == 100.0;
			// 1. No se permite un porcentaje menor o igual al ya existente, excepto si es el primero o es 100%
			if (!isFirst && !isResolutionFull && maxLog.isPresent() && resolutionCreate <= maxLog.get().getResolutionPercentage())
				super.state(false, "*", "assistant-agent.create.resolution-lower-than-previous");

			// 2. Solo se permite un segundo log con resolución al 100%
			if (!isFirst && isResolutionFull && logMaxResolution.size() > 1)
				super.state(false, "*", "assistant-agent.create.only-one-resolution-100");

			// 3. Si ya hay un log con 100%, el segundo no puede cambiar el indicador
			if (isResolutionFull && logMaxResolution.size() == 1) {
				TrackingLog firstFullLog = logMaxResolution.iterator().next();

				boolean indicatorChanged = !log.getIndicator().equals(firstFullLog.getIndicator());
				if (indicatorChanged)
					super.state(false, "*", "assistant-agent.create.cannot-change-indicator");
			}

			if (resolutionCreate == 100.00 && (log.getIndicator() == Indicator.ONGOING || StringHelper.isBlank(log.getResolution())))
				super.state(false, "*", "assistant-agent.create.indicator-resolution-problem");

			if (resolutionCreate != 100.00 && (!StringHelper.isBlank(log.getResolution()) || log.getIndicator() != Indicator.ONGOING))
				super.state(false, "*", "assistant-agent.create.resolution-problem");
		}
	}

	@Override
	public void perform(final TrackingLog log) {
		log.setDraftMode(false);
		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		SelectChoices logIndicator = SelectChoices.from(Indicator.class, log.getIndicator());
		dataset.put("logIndicator", logIndicator);
		super.getResponse().addData(dataset);
	}

}
