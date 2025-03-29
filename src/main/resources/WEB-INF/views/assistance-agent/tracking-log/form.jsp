<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment
        code="assistanceAgent.tracking-log.form.label.lastUpdatedMoment" 
        path="lastUpdatedMoment" 
    />
    
    <acme:input-textbox 
        code="assistanceAgents.tracking-log.list.step" 
        path="step" 
    />
    
    <acme:input-double
        code="assistanceAgents.tracking-log.list.resolutionPercentage" 
        path="resolutionPercentage" 
    />
    
    <acme:input-textbox 
        code="assistanceAgent.tracking-log.form.label.indicator" 
        path="indicator"
    />
    
    <acme:input-textbox 
        code="assistanceAgent.tracking-log.form.label.resolution" 
        path="resolution" 
    />
    
    <acme:input-textbox
        code="assistanceAgent.tracking-log.form.label.claim-id" 
        path="claim.id" 
    />
    
</acme:form>