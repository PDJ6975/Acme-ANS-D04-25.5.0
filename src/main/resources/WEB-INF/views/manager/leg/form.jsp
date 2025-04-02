<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-textbox
        code="manager.leg.form.label.flightNumber" 
        path="flightNumber"
    />
    
    <acme:input-double
        code="manager.leg.form.label.duration"
        path="duration"
    />
    
    <acme:input-select 
        code="manager.leg.form.label.legStatus" 
        path="legStatus"
        choices="${legStatuses}"
    />
    
    <acme:input-textarea 
        code="manager.leg.form.label.description" 
        path="description"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.scheduledDeparture" 
        path="scheduledDeparture"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.scheduledArrival" 
        path="scheduledArrival"
    />
    
    <acme:input-checkbox 
        code="manager.leg.form.label.draftMode" 
        path="draftMode"
        readonly="true"
    />
    
    <acme:input-select 
        code="manager.leg.form.label.aircraft" 
        path="aircraft"
        choices="${aircrafts}"
    />
    
    <acme:input-select 
        code="manager.leg.form.label.departureAirport" 
        path="departureAirport"
        choices="${departureAirports}"
    />
    
    <acme:input-select 
        code="manager.leg.form.label.arrivalAirport" 
        path="arrivalAirport"
        choices="${arrivalAirports}"
    />
    
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
            <jstl:if test="${draftMode == true}">
                <acme:submit code="manager.leg.form.button.update" action="/manager/leg/update"/>
                <acme:submit code="manager.leg.form.button.delete" action="/manager/leg/delete"/>
                <acme:submit code="manager.leg.form.button.publish" action="/manager/leg/publish"/>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="manager.leg.form.button.create" action="/manager/leg/create?masterId=${masterId}"/>
        </jstl:when>
    </jstl:choose>	
</acme:form>