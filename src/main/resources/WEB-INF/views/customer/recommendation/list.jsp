<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="customer.recommendation.list.label.name" path="name" width="30%"/>
    <acme:list-column code="customer.recommendation.list.label.category" path="category" width="20%"/>
    <acme:list-column code="customer.recommendation.list.label.city" path="city" width="20%"/>
    <acme:list-column code="customer.recommendation.list.label.externalLink" path="externalLink" width="30%"/>
</acme:list>

<div class="panel panel-default">
    <div class="panel-body">
        <acme:print code="customer.recommendation.list.explanation"/>
    </div>
</div>