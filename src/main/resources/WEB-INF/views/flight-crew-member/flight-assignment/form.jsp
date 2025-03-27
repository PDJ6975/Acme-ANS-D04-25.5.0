<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly = "false">
    <!-- Campos del Leg -->
    <acme:input-textbox 
        code="crewMember.assignment.form.label.flightNumber" 
        path="leg.flightNumber" 
        readonly="true"
    />

    <acme:input-textbox 
        code="crewMember.assignment.form.label.departureAirport" 
        path="leg.departureAirport.name" 
        readonly="true"
    />

    <acme:input-textbox 
        code="crewMember.assignment.form.label.arrivalAirport" 
        path="leg.arrivalAirport.name" 
        readonly="true"
    />

    <acme:input-moment 
        code="crewMember.assignment.form.label.scheduledDeparture" 
        path="leg.scheduledDeparture"
        readonly="true"
    />
    <acme:input-moment 
        code="crewMember.assignment.form.label.scheduledArrival" 
        path="leg.scheduledArrival"
        readonly="true"
    />

    <!-- Campos del FlightAssignment -->
    
    <jstl:choose>
	    <jstl:when test="${_command == 'show'}">
	        <acme:input-textbox 
	            code="crewMember.assignment.form.label.employeeCode" 
	            path="flightCrewMember.employeeCode" 
	            readonly="true"
	        />
	    </jstl:when>
	    <jstl:otherwise>
	        <acme:input-select
	            code="crewMember.assignment.form.label.employeeCode"
	            path="employeeCode"
	            choices="${crewMembers}"
	            readonly="false"
	        />
	    </jstl:otherwise>
	</jstl:choose>
    
    <acme:input-select
        code="crewMember.assignment.form.label.crewRole"
        path="crewRole"
        choices="${crewRoles}" 
    />
    
    <jstl:choose>
    <jstl:when test="${_command == 'create'}">
        <acme:input-select 
            code="crewMember.assignment.form.label.assignmentStatus"
            path="assignmentStatus"
            choices="${assignmentStatuses}"
            readonly="true"
        />
        <acme:input-moment 
            code="crewMember.assignment.form.label.lastUpdated"
            path="lastUpdated"
            readonly="true"
        />
    </jstl:when>
    <jstl:otherwise>
        <acme:input-select 
            code="crewMember.assignment.form.label.assignmentStatus"
            path="assignmentStatus"
            choices="${assignmentStatuses}"
        />
        <acme:input-moment 
            code="crewMember.assignment.form.label.lastUpdated"
            path="lastUpdated"
        />
    </jstl:otherwise>
	</jstl:choose>

    <acme:input-textarea
        code="crewMember.assignment.form.label.comments"
        path="comments"
    />
	 
	<jstl:choose>	
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
			<jstl:if test="${canCreate}">
    			<acme:button code="crewMember.assignment.form.button.create" action="/flight-crew-member/flight-assignment/create?masterId=${masterId}"/>
			</jstl:if>	
			<acme:submit code="employer.job.form.button.update" action="/employer/job/update"/>
			<acme:submit code="employer.job.form.button.delete" action="/employer/job/delete"/>
			<acme:submit code="employer.job.form.button.publish" action="/employer/job/publish"/>
		</jstl:when>	
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="crewMember.assignment.form.submit.create" action="/flight-crew-member/flight-assignment/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>	
    
</acme:form>