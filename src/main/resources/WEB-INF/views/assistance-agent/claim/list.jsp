<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgents.claim.list.label.username" path="userAccount.username"/>
	<acme:list-column code="assistanceAgents.claim.list.label.description" path="description"/>
	<acme:list-column code="assistanceAgents.claim.list.label.type" path="type"/>
	<acme:list-payload path="payload"/>
</acme:list>