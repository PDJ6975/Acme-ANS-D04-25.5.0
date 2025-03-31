<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-textbox 
        code="manager.flight.form.label.tag" 
        path="tag"
    />
    
    <acme:input-checkbox 
        code="manager.flight.form.label.selfTransfer" 
        path="selfTransfer"
    />
    
    <acme:input-money
        code="manager.flight.form.label.cost" 
        path="cost"
    />
    
    <acme:input-textarea 
        code="manager.flight.form.label.description" 
        path="description"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.scheduledDeparture" 
        path="scheduledDeparture"
        readonly="true"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.scheduledArrival" 
        path="scheduledArrival"
        readonly="true"
    />
    
    <acme:input-textbox
        code="manager.flight.form.label.originCity" 
        path="originCity"
        readonly="true"
    />
    
    <acme:input-textbox
        code="manager.flight.form.label.destinationCity" 
        path="destinationCity"
        readonly="true"
    />
    
    <acme:input-textbox 
        code="manager.flight.form.label.layovers" 
        path="layovers"
        readonly="true"
    />
    
    <acme:input-textbox
        code="manager.flight.form.label.airline" 
        path="airline.name"   
    />
    
    <acme:input-checkbox
    	code="manager.flight.form.label.draftMode"
    	path="draftMode"
    	readonly="true"
    />
    
    <acme:input-integer
    	code="manager.flight.form.label.manager.id"
    	path="manager.id"
    	readonly="true"
    />
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
            <acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${masterId}"/>
            <jstl:if test="${draftMode == true}">
                <acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>
                <acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
        </jstl:when>
    </jstl:choose>
</acme:form>