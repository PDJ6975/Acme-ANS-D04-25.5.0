<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.leg.list.label.flightNumber" path="flightNumber"/>
	<acme:list-column code="manager.leg.list.label.duration" path="duration"/>
	<acme:list-column code="manager.leg.list.label.legStatus" path="legStatus"/>
	<acme:list-column code="manager.leg.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column code="manager.leg.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column code="manager.leg.list.label.flight.tag" path="flight.tag"/>
	<acme:list-payload path="payload"/>
</acme:list>