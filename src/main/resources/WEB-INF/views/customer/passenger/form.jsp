<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox 
        code="customer.passenger.form.label.fullName" 
        path="fullName"/>
    
    <acme:input-textbox 
        code="customer.passenger.form.label.email" 
        path="email"/>
    
    <acme:input-textbox 
        code="customer.passenger.form.label.passportNumber" 
        path="passportNumber"/>
    
    <acme:input-moment 
        code="customer.passenger.form.label.dateOfBirth" 
        path="dateOfBirth"/>
    
    <acme:input-textbox 
        code="customer.passenger.form.label.specialNeeds" 
        path="specialNeeds"/>
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
            <jstl:if test="${draftMode == true}">
                <acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
                <acme:submit code="customer.passenger.form.button.delete" action="/customer/passenger/delete"/>
                <acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish"/>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create?masterId=${masterId}"/>
        </jstl:when>
    </jstl:choose>
</acme:form>