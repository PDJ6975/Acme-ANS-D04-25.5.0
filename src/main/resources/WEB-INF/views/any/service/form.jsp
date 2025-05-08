<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-textbox code="any.service.list.completed.label.name" path="name" />
    <acme:input-textbox code="any.service.list.completed.label.link" path="link" />
    <acme:input-double code="any.service.list.completed.label.averageDwellTime" path="averageDwellTime" />
    <acme:input-textbox code="any.service.list.completed.label.promotionCode" path="promotionCode" />
    <acme:input-money code="any.service.list.completed.label.discountMoney" path="discountMoney" />
    <acme:input-textbox code="any.service.list.completed.label.airport.name" path="airport.name" />

</acme:form>