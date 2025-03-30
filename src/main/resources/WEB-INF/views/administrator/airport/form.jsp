<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.airport.form.label.name" path="name" />
	<acme:input-textbox code="administrator.airport.form.label.iataCode" path="iataCode" />
	<acme:input-select code="administrator.airport.form.label.operationalScope" path="operationalScope" choices="${airportScopes}"/>
	<acme:input-textbox code="administrator.airport.form.label.city" path="city" />
	<acme:input-textbox code="administrator.airport.form.label.country" path="country" />
	<acme:input-textbox code="administrator.airport.form.label.website" path="website" />
	
	<acme:input-textbox code="administrator.airport.form.label.emailAddress" path="emailAddress" />
	<acme:input-textbox code="administrator.airport.form.label.contactPhone" path="contactPhone" />
</acme:form>