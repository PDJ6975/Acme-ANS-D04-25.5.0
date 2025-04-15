<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<div class="container my-4">

    <!-- Maintenance Records by Status -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="technician.dashboard.title.maintenance-records-by-status"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <thead>
                    <tr>
                        <th><acme:print code="technician.dashboard.label.maintenance-records-by-status"/></th>
                        <th><acme:print code="technician.dashboard.label.count"/></th>
                    </tr>
                </thead>
                <tbody>
                    <jstl:forEach var="entry" items="${numMaintenanceRecordsByStatus}">
                        <tr>
                            <td><jstl:out value="${entry.key}"/></td>
                            <td><jstl:out value="${entry.value}"/></td>
                        </tr>
                    </jstl:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Nearest Inspection Due (Maintenance Record Details) -->
	<div class="card mb-4">
	    <div class="card-header">
	        <h2>
	            <acme:print code="technician.dashboard.title.nearest-inspection-due"/>
	        </h2>
	    </div>
	    <div class="card-body">
	        <jstl:choose>
	            <jstl:when test="${not empty nearestInspectionDue}">
	                <table class="table table-sm table-bordered">
	                    <tr>
	                        <th><acme:print code="technician.dashboard.label.maintenance-records-by-status"/></th>
	                        <td><jstl:out value="${nearestInspectionDue.status}"/></td>
	                    </tr>
	                    <tr>
	                        <th><acme:print code="technician.dashboard.label.moment"/></th>
	                        <td><jstl:out value="${nearestInspectionDue.moment}"/></td>
	                    </tr>
	                    <tr>
	                        <th><acme:print code="technician.dashboard.label.nearest-inspection-due"/></th>
	                        <td><jstl:out value="${nearestInspectionDue.nextInspectionDue}"/></td>
	                    </tr>
	                    <tr>
	                        <th><acme:print code="technician.dashboard.label.estimatedCost"/></th>
	                        <td><jstl:out value="${nearestInspectionDue.estimatedCost}"/></td>
	                    </tr>
	                    <tr>
	                        <th><acme:print code="technician.dashboard.label.aircraft"/></th>
	                        <td><jstl:out value="${nearestInspectionDue.aircraft.model}"/></td>
	                    </tr>
	                    <!-- Puedes agregar otros campos si lo deseas -->
	                </table>
	            </jstl:when>
	            <jstl:otherwise>
	                <p><acme:print code="technician.dashboard.label.no-record"/></p>
	            </jstl:otherwise>
	        </jstl:choose>
	    </div>
	</div>

    <!-- Top 5 Aircrafts with Most Tasks -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="technician.dashboard.title.top-aircrafts"/>
            </h2>
        </div>
        <div class="card-body">
            <ul class="list-group">
                <jstl:forEach var="aircraft" items="${topFiveAircraftsWhithMostTask}">
                    <li class="list-group-item">
                        <jstl:out value="${aircraft}"/>
                    </li>
                </jstl:forEach>
            </ul>
        </div>
    </div>

    <!-- Maintenance Cost Statistics -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="technician.dashboard.title.cost-statistics"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <tr>
                    <th><acme:print code="technician.dashboard.label.avg-cost"/></th>
                    <td><acme:print value="${avgEstimatedCost}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="technician.dashboard.label.min-cost"/></th>
                    <td><acme:print value="${minEstimatedCost}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="technician.dashboard.label.max-cost"/></th>
                    <td><acme:print value="${maxEstimatedCost}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="technician.dashboard.label.stddev-cost"/></th>
                    <td><acme:print value="${stdDevEstimatedCost}"/></td>
                </tr>
            </table>
        </div>
    </div>

    <!-- Task Duration Statistics -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="technician.dashboard.title.task-duration-statistics"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <tr>
                    <th><acme:print code="technician.dashboard.label.avg-duration"/></th>
                    <td><acme:print value="${avgEstimatedDuration}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="technician.dashboard.label.min-duration"/></th>
                    <td><acme:print value="${minEstimatedDuration}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="technician.dashboard.label.max-duration"/></th>
                    <td><acme:print value="${maxEstimatedDuration}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="technician.dashboard.label.stddev-duration"/></th>
                    <td><acme:print value="${stdDevEstimatedDuration}"/></td>
                </tr>
            </table>
        </div>
    </div>

</div>

<acme:return/>
