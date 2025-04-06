<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistanceAgents.tracking-log.list.step" path="step"/>
	<acme:list-column code="assistanceAgents.tracking-log.list.resolutionPercentage" path="resolutionPercentage"/>
	<acme:list-column code="assistanceAgents.tracking-log.list.indicator" path="indicator"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="assistanceAgents.tracking-log.list.button" action="/assistance-agent/tracking-log/create?masterId=${masterId}" />