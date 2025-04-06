/*
 * FlightCrewMemberDashboardShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.crewMember.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignments.AssignmentStatus;
import acme.entities.assignments.FlightAssignment;
import acme.forms.FlightCrewMemberDashboard;
import acme.realms.members.FlightCrewMember;

@GuiService
public class FlightCrewMemberDashboardShowService extends AbstractGuiService<FlightCrewMember, FlightCrewMemberDashboard> {

	@Autowired
	protected FlightCrewMemberDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMemberDashboard dashboard = new FlightCrewMemberDashboard();
		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Últimos 5 destinos
		PageRequest p = PageRequest.of(0, 5);
		Page<String> page = this.repository.findDestinations(crewMemberId, p);
		List<String> lastFive = page.getContent();
		dashboard.setLastFiveDestinations(lastFive);

		// Logs por rango
		List<Object[]> rawRanges = this.repository.countLogsBySeverityRanges(crewMemberId);
		Map<String, Long> activityLogCounts = new HashMap<>();
		for (Object[] row : rawRanges) {
			String range = (String) row[0];
			Long count = (Long) row[1];
			activityLogCounts.put(range, count);
		}
		dashboard.setActivityLogCounts(activityLogCounts);

		// Última leg
		List<Integer> lastLegs = this.repository.findLastLegIdByCrewMember(crewMemberId);
		Integer lastLegId = lastLegs.isEmpty() ? null : lastLegs.get(0);
		// Compañeros de etapa
		List<FlightCrewMember> colleagues = lastLegId != null ? this.repository.findColleaguesByLegId(lastLegId, crewMemberId) : List.of();
		dashboard.setColleaguesInLastStage(colleagues);

		// Asignaciones agrupadas por status
		List<Object[]> rawAssignments = this.repository.findAssignmentsByStatus(crewMemberId);
		Map<AssignmentStatus, List<FlightAssignment>> grouped = new HashMap<>();
		for (Object[] row : rawAssignments) {
			AssignmentStatus st = (AssignmentStatus) row[0];
			FlightAssignment fa = (FlightAssignment) row[1];
			grouped.computeIfAbsent(st, x -> new ArrayList<>()).add(fa);
		}
		dashboard.setAssignmentsByStatus(grouped);

		// Estadísticas

		List<Object[]> rawData = this.repository.findAssignmentsCountPerDayInLastMonth(crewMemberId);

		// Extraigo los conteos
		List<Long> counts = new ArrayList<>();
		for (Object[] row : rawData) {
			// row[0] es la fecha, row[1] es la cantidad
			Long count = (Long) row[1];
			counts.add(count);
		}

		// Calculo average, min, max, std dev de "counts"
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		long sum = 0;
		for (Long c : counts) {
			if (c < min)
				min = c;
			if (c > max)
				max = c;
			sum += c;
		}
		double average = counts.isEmpty() ? 0 : (double) sum / counts.size();

		// std dev
		double variance = 0;
		for (Long c : counts) {
			double diff = c - average;
			variance += diff * diff;
		}
		double std = counts.size() > 1 ? Math.sqrt(variance / (counts.size() - 1)) : 0.0;

		dashboard.setAverage(average);
		dashboard.setMinimum((double) min);
		dashboard.setMaximum((double) max);
		dashboard.setStandardDesviation(std);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final FlightCrewMemberDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "activityLogCounts", "colleaguesInLastStage", "member", "assignmentsByStatus", "average", "minimum", "maximum", "standardDesviation");

		super.getResponse().addData(dataset);
	}
}
