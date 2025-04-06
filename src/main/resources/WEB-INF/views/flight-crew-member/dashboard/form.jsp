<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
    <acme:print code="flight-crew-member.dashboard.title.assignment-stats"/>
</h2>

<table class="table table-sm">
    <tr>
        <th><acme:print code="flight-crew-member.dashboard.label.average"/></th>
        <td><acme:print value="${average}"/></td>
    </tr>
    <tr>
        <th><acme:print code="flight-crew-member.dashboard.label.minimum"/></th>
        <td><acme:print value="${minimum}"/></td>
    </tr>
    <tr>
        <th><acme:print code="flight-crew-member.dashboard.label.maximum"/></th>
        <td><acme:print value="${maximum}"/></td>
    </tr>
    <tr>
        <th><acme:print code="flight-crew-member.dashboard.label.standard-deviation"/></th>
        <td><acme:print value="${standardDesviation}"/></td>
    </tr>
</table>

<h2>
    <acme:print code="flight-crew-member.dashboard.title.activity-severity"/>
</h2>

<table class="table table-sm">
    <thead>
        <tr>
            <th><acme:print code="flight-crew-member.dashboard.label.severity-range"/></th>
            <th><acme:print code="flight-crew-member.dashboard.label.count"/></th>
        </tr>
    </thead>
    <tbody>
        <jstl:forEach var="entry" items="${activityLogCounts}">
            <tr>
                <td><jstl:out value="${entry.key}"/></td>
                <td><jstl:out value="${entry.value}"/></td>
            </tr>
        </jstl:forEach>
    </tbody>
</table>

<h2>
    <acme:print code="flight-crew-member.dashboard.title.last-destinations"/>
</h2>

<ul>
    <jstl:forEach var="destination" items="${lastFiveDestinations}">
        <li><jstl:out value="${destination}"/></li>
    </jstl:forEach>
</ul>

<h2>
    <acme:print code="flight-crew-member.dashboard.title.colleagues-last-stage"/>
</h2>

<ul>
    <jstl:forEach var="colleague" items="${colleaguesInLastStage}">
        <li><jstl:out value="${colleague.identity.fullName}"/> - <jstl:out value="${colleague.employeeCode}"/></li>
    </jstl:forEach>
</ul>

<h2>
    <acme:print code="flight-crew-member.dashboard.title.assignments-by-status"/>
</h2>

<jstl:forEach var="entry" items="${assignmentsByStatus}">
    <h3><jstl:out value="${entry.key}"/></h3>
    <ul>
        <jstl:forEach var="assignment" items="${entry.value}">
            <li>
                <jstl:out value="${assignment.leg.flightNumber}"/> - 
                <jstl:out value="${assignment.crewRole}"/> - 
                <jstl:out value="${assignment.assignmentStatus}"/>
            </li>
        </jstl:forEach>
    </ul>
</jstl:forEach>

<acme:return/>

