<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.service.list.completed.label.name" path="name"/>
	<acme:list-column code="any.service.list.completed.label.link" path="link"/>
	<acme:list-column code="any.service.list.completed.label.averageDwellTime" path="averageDwellTime"/>
	<acme:list-column code="any.service.list.completed.label.airport.name" path="airport.name"/>
	<acme:list-payload path="payload"/>
</acme:list>