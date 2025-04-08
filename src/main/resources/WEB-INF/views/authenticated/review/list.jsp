<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="any.review.list.label.moment" path="moment" width="20%"/>
    <acme:list-column code="any.review.list.label.name" path="name" width="25%"/>
    <acme:list-column code="any.review.list.label.subject" path="subject" width="35%"/>
    <acme:list-column code="any.review.list.label.score" path="score" width="10%"/>
    <acme:list-column code="any.review.list.label.recommended" path="recommended" width="10%"/>
</acme:list>

    <acme:button code="any.review.list.button.create" action="/authenticated/review/create"/>

<div class="panel panel-default">
    <div class="panel-body">
        <acme:print code="any.review.list.label.explanation"/>
    </div>
</div>