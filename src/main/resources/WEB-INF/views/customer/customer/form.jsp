<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.customer.form.label.identifier" path="identifier"/>
    <acme:input-textbox code="customer.customer.form.label.phoneNumber" path="phoneNumber"/>
    <acme:input-textbox code="customer.customer.form.label.address" path="address"/>
    <acme:input-textbox code="customer.customer.form.label.city" path="city"/>
    <acme:input-textbox code="customer.customer.form.label.country" path="country"/>
    
    <acme:submit code="customer.customer.form.button.update" action="/customer/customer/update"/>
</acme:form>