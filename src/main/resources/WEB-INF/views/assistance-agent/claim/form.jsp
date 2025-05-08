<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show')}">
			<acme:input-moment
        	code="assistanceAgent.claim.form.label.registrationMoment" 
       		path="resgistrationMoment" 
        	readonly="true"
    		/>
	    </jstl:when>
	</jstl:choose>
    
    <acme:input-textbox 
        code="assistanceAgent.claim.form.label.passengerEmail" 
        path="passengerEmail" 
        readonly="false"
    />
    
    <acme:input-textarea
        code="assistanceAgents.claim.list.label.description" 
        path="description" 
        readonly="false"
    />
    
    <acme:input-select
        code="assistanceAgents.claim.list.label.type" 
        path="type" 
        choices="${claimType}"
        readonly="false"
    />
    <jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-select
        		code="assistanceAgent.claim.form.label.state" 
        		path="state" 
        		choices="${claimState}"
        		readonly="true"
    		/>
	    </jstl:when>
	</jstl:choose>

    <acme:input-textbox 
        code="assistanceAgents.claim.list.label.username" 
        path="userAccount.username" 
        readonly="false"
    />
    <acme:input-textbox
        code="assistanceAgents.claim.list.label.legFlightNumber" 
        path="leg.flightNumber" 
        readonly="false"
    />
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|delete|update')}">
 			<acme:button code="master.menu.assistanceAgent.list-tracking-log" action="/assistance-agent/tracking-log/list?masterId=${masterId}" />				
            <jstl:if test="${draftMode == true}">
                <acme:submit code="assistance-agent.claim.form.submit.update" action="/assistance-agent/claim/update"/>
                <acme:submit code="assistance-agent.claim.form.submit.delete" action="/assistance-agent/claim/delete"/>
                <acme:submit code="assistance-agent.claim.form.submit.publish" action="/assistance-agent/claim/publish"/>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.claim.form.submit.create" action="/assistance-agent/claim/create" />
        </jstl:when>
    </jstl:choose>
    
</acme:form>