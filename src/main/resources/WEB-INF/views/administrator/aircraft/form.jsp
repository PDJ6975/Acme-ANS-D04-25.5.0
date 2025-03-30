<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.aircraft.form.label.registrationNumber" path="registrationNumber" />
	<acme:input-textbox code="administrator.aircraft.form.label.model" path="model" />
	<acme:input-integer code="administrator.aircraft.form.label.capacity" path="capacity" />
	<acme:input-double code="administrator.aircraft.form.label.cargoWeight" path="cargoWeight" />
	<jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show|update|disable')}">
	        <acme:input-textbox code="administrator.aircraft.form.label.aircraftStatus" path="aircraftStatus" readonly="true"/>
	    </jstl:when>
	    <jstl:otherwise>
	        <acme:input-select code="administrator.aircraft.form.label.aircraftStatus" path="aircraftStatus" choices="${aircraftStatuses}" readonly="false"/>
	    </jstl:otherwise>
	</jstl:choose>
	<acme:input-textarea code="administrator.aircraft.form.label.details" path="details" />
	
	 <jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show|update|disable')}">
	        <acme:input-textbox code="administrator.aircraft.form.label.airlineIataCode" path="airline.iataCode" readonly="true" />
	    </jstl:when>
	    <jstl:otherwise>
	        <acme:input-select
	            code="administrator.aircraft.form.label.airlineIataCode"
	            path="iataCode"
	            choices="${airlines}"
	            readonly="false"
	        />
	    </jstl:otherwise>
	</jstl:choose>
	
	<acme:input-checkbox code="administrator.aircraft.form.label.confirmation" path="confirmation"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|disable')}">
	    	<acme:submit code="crewMember.log.list.button.update" action="/administrator/aircraft/update"/>
	    	<jstl:if test="${disable == false}">
    			<acme:submit code="administrator.aircraft.form.button.disable" action="/administrator/aircraft/disable"/>
			</jstl:if>
	    </jstl:when>
	    <jstl:when test="${_command == 'create'}">
	        <acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create" />
	    </jstl:when>
	</jstl:choose>
	
</acme:form>
