<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:choose>
    <jstl:when test="${acme:anyOf(_command, 'show')}">
        <acme:form readonly="true">
            <acme:input-textbox code="any.review.form.label.name" path="name"/>
            <acme:input-textbox code="any.review.form.label.subject" path="subject"/>
            <acme:input-moment code="any.review.form.label.moment" path="moment"/>
            <acme:input-textarea code="any.review.form.label.text" path="text"/>
            <acme:input-double code="any.review.form.label.score" path="score"/>
            <acme:input-checkbox code="any.review.form.label.recommended" path="recommended"/>
            <acme:input-textbox code="any.review.form.label.username" path="userAccount.username"/>
            
        </acme:form>
    </jstl:when>
    
    <jstl:when test="${_command == 'create'}">
        <acme:form>
            <acme:input-textbox code="any.review.form.label.name" path="name"/>
            <acme:input-textbox code="any.review.form.label.subject" path="subject"/>
            <acme:input-textarea code="any.review.form.label.text" path="text"/>
            <acme:input-double code="any.review.form.label.score" path="score"/>
            <acme:input-select code="any.review.form.label.recommended" path="recommended" choices="${recommendedOptions}"/>
            
            <div class="alert alert-warning">
                <acme:print code="any.review.form.label.warning"/>
            </div>
            
            <acme:input-checkbox code="any.review.form.label.confirmation" path="confirmation"/>
            
            <acme:submit code="any.review.form.button.create" action="/authenticated/review/create"/>
        </acme:form>
    </jstl:when>
</jstl:choose>