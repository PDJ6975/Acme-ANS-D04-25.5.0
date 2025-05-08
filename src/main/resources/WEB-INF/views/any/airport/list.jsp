<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.airport.list.label.name" path="name" />
	<acme:list-column code="any.airport.list.label.iataCode" path="iataCode" />
	<acme:list-column code="any.airport.list.label.city" path="city" />
</acme:list>
