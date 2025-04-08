<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-textbox code="any.review.form.label.name" path="name"/>
    <acme:input-textbox code="any.review.form.label.subject" path="subject"/>
    <acme:input-moment code="any.review.form.label.moment" path="moment"/>
    <acme:input-textarea code="any.review.form.label.text" path="text"/>
    <acme:input-double code="any.review.form.label.score" path="score"/>
    <acme:input-checkbox code="any.review.form.label.recommended" path="recommended"/>
    <acme:input-textbox code="any.review.form.label.username" path="userAccount.username"/>
    
    <acme:button code="any.review.form.button.return" action="/any/review/list"/>
</acme:form>