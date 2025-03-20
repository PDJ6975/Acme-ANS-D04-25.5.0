<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <!-- Campos del Leg -->
    <acme:input-textbox 
        code="crewMember.assignment.list.label.flightNumber" 
        path="leg.flightNumber" 
    />

    <acme:input-textbox 
        code="crewMember.assignment.form.label.departureAirport" 
        path="leg.departureAirport.name" 
    />

    <acme:input-textbox 
        code="crewMember.assignment.form.label.arrivalAirport" 
        path="leg.arrivalAirport.name" 
    />

    <acme:input-moment 
        code="crewMember.assignment.form.label.scheduledDeparture" 
        path="leg.scheduledDeparture"
    />
    <acme:input-moment 
        code="crewMember.assignment.form.label.scheduledArrival" 
        path="leg.scheduledArrival"
    />
    
    <!-- Campos del FlightCrewMembers -->
    <acme:input-textbox 
        code="crewMember.assignment.list.label.employeeCode" 
        path="flightCrewMember.employeeCode" 
    />
    
     <acme:input-textbox 
        code="crewMember.assignment.list.label.phoneNumber" 
        path="flightCrewMember.phoneNumber" 
    />

    <!-- Campos del FlightAssignment -->
    <acme:input-select
        code="crewMember.assignment.form.label.crewRole"
        path="crewRole"
        choices="${crewRoles}" 
    />
    
    <acme:input-select
        code="crewMember.assignment.form.label.assignmentStatus"
        path="crewRole"
        choices="${assignmentStatuses}" 
    />
    
    <acme:input-moment
        code="crewMember.assignment.form.label.lastUpdated"
        path="lastUpdated"
    />

    <acme:input-textarea
        code="crewMember.assignment.form.label.comments"
        path="comments"
    />
</acme:form>