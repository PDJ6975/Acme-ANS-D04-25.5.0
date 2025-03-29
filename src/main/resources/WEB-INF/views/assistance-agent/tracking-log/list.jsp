<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgents.tracking-log.list.id" path="id"/>
	<acme:list-column code="assistanceAgents.tracking-log.list.step" path="step"/>
	<acme:list-column code="assistanceAgents.tracking-log.list.resolutionPercentage" path="resolutionPercentage"/>
	<acme:list-payload path="payload"/>
</acme:list>