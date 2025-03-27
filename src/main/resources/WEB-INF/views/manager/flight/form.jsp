<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-moment 
        code="manager.flight.form.label.tag" 
        path="tag"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.selfTransfer" 
        path="selfTransfer"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.cost" 
        path="cost"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.description" 
        path="description"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.scheduledDeparture" 
        path="scheduledDeparture"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.scheduledArrival" 
        path="scheduledArrival"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.originCity" 
        path="originCity"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.destinationCity" 
        path="destinationCity"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.layovers" 
        path="layovers"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.name" 
        path="airline.name"
    />
    
    <acme:input-moment 
        code="manager.flight.form.label.managerId" 
        path="manager.managerId"
    />
    	
</acme:form>