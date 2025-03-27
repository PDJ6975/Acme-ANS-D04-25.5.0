<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-moment 
        code="manager.leg.form.label.flightNumber" 
        path="flightNumber"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.duration"
        path="duration"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.legStatus" 
        path="legStatus"
    />
    
    <acme:input-moment 
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
    
    <acme:input-moment 
        code="manager.leg.form.label.draftMode" 
        path="draftMode"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.flight.tag" 
        path="flight.tag"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.aircraft.model" 
        path="aircraft.model"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.departureAirport.name" 
        path="departureAirport.name"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.arrivalAirport.name" 
        path="arrivalAirport.name"
    />
    
    <acme:input-moment 
        code="manager.leg.form.label.flight.airline.name" 
        path="flight.airline.name"
    />
    	
</acme:form>