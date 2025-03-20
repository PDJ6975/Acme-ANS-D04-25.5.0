<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="crewMember.assignment.list.completed.label.flightNumber" path="leg.flightNumber" width="10%"/>
	<acme:list-column code="crewMember.assignment.list.completed.label.crewRole" path="crewRole" width="10%"/>
	<acme:list-column code="crewMember.assignment.list.completed.label.departureAirport" path="leg.departureAirport" width="10%"/>
	<acme:list-column code="crewMember.assignment.list.completed.label.arrivalAirport" path="leg.arrivalAirport" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>