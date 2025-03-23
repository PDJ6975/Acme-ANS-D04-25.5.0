<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-moment 
        code="crewMember.log.form.label.registrationMoment" 
        path="registrationMoment"
    />
    
    <acme:input-textbox 
        code="crewMember.log.form.label.typeOfIncident" 
        path="typeOfIncident" 
    />
    
    <acme:input-textarea
        code="crewMember.log.form.label.description"
        path="description"
    />
    
    <acme:input-integer code="crewMember.log.form.label.severityLevel" path="severityLevel" placeholder="crewMember.log.form.placeholder.severityLevel"/>
    	
</acme:form>