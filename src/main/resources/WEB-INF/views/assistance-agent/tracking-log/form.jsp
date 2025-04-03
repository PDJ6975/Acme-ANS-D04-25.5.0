<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show')}">
			<acme:input-moment
       		code="assistanceAgent.tracking-log.form.label.lastUpdatedMoment" 
        	path="lastUpdatedMoment" 
        	readonly="true"
   		 	/>
	    </jstl:when>
	</jstl:choose>
    
    <acme:input-textbox 
        code="assistanceAgents.tracking-log.list.step" 
        path="step" 
        readonly="false"
    />
    
    <acme:input-double
        code="assistanceAgents.tracking-log.list.resolutionPercentage" 
        path="resolutionPercentage" 
		readonly="false"
    />
    
    <acme:input-select 
        code="assistanceAgent.tracking-log.form.label.indicator" 
        choices = "${logIndicator}"
        path="indicator"
    />
    
    <acme:input-textbox 
        code="assistanceAgent.tracking-log.form.label.resolution" 
        path="resolution" 
		readonly="false"
        
    />
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|delete|update')}">
            <jstl:if test="${draftMode == true}">
                <acme:submit code="assistance-agent.tracking-log.form.submit.update" action="/assistance-agent/tracking-log/update"/>
                <acme:submit code="assistance-agent.tracking-log.form.submit.delete" action="/assistance-agent/tracking-log/delete"/>
                <acme:submit code="assistance-agent.tracking-log.form.submit.publish" action="/assistance-agent/tracking-log/publish"/>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.tracking-log.form.submit.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}" />
        </jstl:when>
    </jstl:choose>
    
</acme:form>