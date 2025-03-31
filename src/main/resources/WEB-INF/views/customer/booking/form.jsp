<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox 
        code="customer.booking.form.label.locatorCode" 
        path="locatorCode" 
        readonly="false"/>
    
    <acme:input-moment 
        code="customer.booking.form.label.purchaseMoment" 
        path="purchaseMoment" 
        readonly="true"/>
    
    <acme:input-select 
        code="customer.booking.form.label.travelClass" 
        path="travelClass" 
        choices="${travelClasses}"/>
    
    <acme:input-money 
        code="customer.booking.form.label.price" 
        path="price"/>
    
    <acme:input-textbox 
        code="customer.booking.form.label.creditCardNibble" 
        path="creditCardNibble"/>
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
            <acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${masterId}"/>
            <jstl:if test="${draftMode == true}">
                <acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
                <acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>
                <jstl:if test="${!empty creditCardNibble}">
                    <acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
                </jstl:if>
            </jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="customer.booking.form.button.create" action="/customer/booking/create?masterId=${masterId}"/>
        </jstl:when>
    </jstl:choose>
</acme:form>