<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.service.list.label.name" path="name"/>
	<acme:list-column code="administrator.service.list.label.averageDwellTime" path="averageDwellTime"/>
	<acme:list-column code="administrator.service.list.label.airport" path="airport.iataCode"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="administrator.service.list.button.create" action="/administrator/service/create"/>