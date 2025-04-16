<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.airport.form.label.name" path="name" />
	<acme:input-textbox code="any.airport.form.label.iataCode" path="iataCode" />
	<acme:input-select code="any.airport.form.label.operationalScope" path="operationalScope" choices="${operationalScopes}"/>
	<acme:input-textbox code="any.airport.form.label.city" path="city" />
	<acme:input-textbox code="any.airport.form.label.country" path="country" />
	<acme:input-textbox code="any.airport.form.label.website" path="website" />
	<acme:input-textbox code="any.airport.form.label.emailAddress" path="emailAddress" />
	<acme:input-textbox code="any.airport.form.label.contactPhone" path="contactPhone" />
</acme:form>