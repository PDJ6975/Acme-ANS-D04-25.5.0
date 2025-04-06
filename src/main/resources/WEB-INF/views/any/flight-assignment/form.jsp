<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-textbox code="any.assignment.form.label.flightNumber" path="leg.flightNumber" />
    <acme:input-textbox code="any.assignment.form.label.departureAirport" path="leg.departureAirport.name" />
    <acme:input-textbox code="any.assignment.form.label.arrivalAirport" path="leg.arrivalAirport.name" />
    <acme:input-moment code="any.assignment.form.label.scheduledDeparture" path="leg.scheduledDeparture" />
    <acme:input-moment code="any.assignment.form.label.scheduledArrival" path="leg.scheduledArrival" />

    <acme:input-textbox code="any.assignment.form.label.employeeCode" path="flightCrewMember.employeeCode" />
    <acme:input-select code="any.assignment.form.label.assignmentStatus" path="assignmentStatus" choices="${assignmentStatuses}" />
    <acme:input-select code="any.assignment.form.label.crewRole" path="crewRole" choices="${crewRoles}" />
    <acme:input-moment code="any.assignment.form.label.lastUpdated" path="lastUpdated" />
    <acme:input-textarea code="any.assignment.form.label.comments" path="comments" />

</acme:form>