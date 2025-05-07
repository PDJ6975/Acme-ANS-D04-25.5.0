<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.banned.passenger.list.label.fullName" path="fullName" />
	<acme:list-column code="administrator.banned.passenger.list.label.passportNumber" path="passportNumber" />
	<acme:list-column code="administrator.banned.passenger.list.label.reason" path="reason" />
</acme:list>

<acme:button code="administrator.banned.passenger.form.submit.create" action="/administrator/banned-passenger/create" />