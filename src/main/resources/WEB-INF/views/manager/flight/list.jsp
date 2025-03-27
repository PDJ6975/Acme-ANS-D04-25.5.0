<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.flight.list.label.tag" path="tag"/>
	<acme:list-column code="manager.flight.list.label.cost" path="cost"/>
	<acme:list-column code="manager.flight.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column code="manager.flight.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column code="manager.flight.list.label.originCity" path="originCity"/>
	<acme:list-column code="manager.flight.list.label.destinationCity" path="destinationCity"/>
	<acme:list-payload path="payload"/>
</acme:list>