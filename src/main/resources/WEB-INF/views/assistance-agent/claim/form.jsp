<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment
        code="assistanceAgent.claim.form.label.registrationMoment" 
        path="resgistrationMoment" 
    />
    
    <acme:input-textbox 
        code="assistanceAgent.claim.form.label.passengerEmail" 
        path="passengerEmail" 
    />
    
    <acme:input-textarea
        code="assistanceAgents.claim.list.label.description" 
        path="description" 
    />
    
    <acme:input-textbox 
        code="assistanceAgents.claim.list.label.type" 
        path="type" 
    />
    
    <acme:input-textbox 
        code="assistanceAgent.claim.form.label.state" 
        path="state" 
    />
    
    <acme:input-textbox 
        code="assistanceAgents.claim.form.label.assistanceUsername" 
        path="assistanceAgent.userAccount.username" 
    />

    <acme:input-textbox 
        code="assistanceAgents.claim.list.label.username" 
        path="userAccount.username" 
    />
    
</acme:form>