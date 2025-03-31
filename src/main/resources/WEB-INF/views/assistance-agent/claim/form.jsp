<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment
        code="assistanceAgent.claim.form.label.registrationMoment" 
        path="resgistrationMoment" 
        readonly="true"
    />
    
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
    
    <acme:input-textbox 
        code="assistanceAgent.claim.form.label.state" 
        path="state" 
        readonly="true"
    />
    
    <acme:input-textbox 
        code="assistanceAgents.claim.form.label.assistanceUsername" 
        path="assistanceAgent.userAccount.username" 
        readonly="true"
    />

    <acme:input-textbox 
        code="assistanceAgents.claim.list.label.username" 
        path="userAccount.username" 
        readonly="false"
    />
    <acme:input-integer 
        code="assistanceAgents.claim.list.label.legId" 
        path="leg.id" 
        readonly="false"
    />
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
            <acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${masterId}"/>
            <jstl:if test="${draftMode == true}">
                <acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
                <acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>
                <jstl:if test="${!empty creditCardNibble}">
                    <acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
                </jstl:if>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.claim.form.submit.create" action="/assistance-agent/claim/create" />
        </jstl:when>
    </jstl:choose>
    
</acme:form>