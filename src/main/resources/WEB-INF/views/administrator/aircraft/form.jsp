<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.aircraft.form.label.registrationNumber" path="registrationNumber" />
	<acme:input-textbox code="administrator.aircraft.form.label.model" path="model" />
	<acme:input-integer code="administrator.aircraft.form.label.capacity" path="capacity" />
	<acme:input-double code="administrator.aircraft.form.label.cargoWeight" path="cargoWeight" />
	<acme:input-select code="administrator.aircraft.form.label.aircraftStatus" path="aircraftStatus" choices="${aircraftStatuses}" />
	<acme:input-textarea code="administrator.aircraft.form.label.details" path="details" />
	
	<acme:input-textbox code="administrator.aircraft.form.label.airlineName" path="airline.name" />
	<acme:input-textbox code="administrator.aircraft.form.label.airlineIataCode" path="airline.iataCode" />
</acme:form>
