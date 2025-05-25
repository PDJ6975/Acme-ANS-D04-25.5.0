<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-money code="authenticated.exchange-money.form.label.source" path="source"/>
	<acme:input-textbox code="authenticated.exchange-money.form.label.target-currency" path="targetCurrency" placeholder="acme.default.placeholder.currency"/>
	
	<acme:input-moment code="authenticated.exchange-money.form.label.moment" path="moment" readonly="true"/>
	<acme:input-money code="authenticated.exchange-money.form.label.target" path="target" readonly="true"/>
		
	<acme:submit code="authenticated.exchange-money.form.button.perform" action="/authenticated/exchange-money/perform"/>
</acme:form>
