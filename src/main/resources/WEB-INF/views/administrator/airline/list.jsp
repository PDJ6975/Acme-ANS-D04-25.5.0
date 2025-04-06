<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airline.list.label.name" path="name" width="20%"/>
	<acme:list-column code="administrator.airline.list.label.type" path="type" width="10%"/>
	<acme:list-column code="administrator.airline.list.label.iataCode" path="iataCode" width="20%"/>
	<acme:list-column code="administrator.airline.list.label.emailAddress" path="emailAddress" width="30%"/>
	<acme:list-column code="administrator.airline.list.label.phoneNumber" path="phoneNumber" width="20%"/>
</acme:list>

<acme:button code="administrator.airline.list.button.create" action="/administrator/airline/create"/>