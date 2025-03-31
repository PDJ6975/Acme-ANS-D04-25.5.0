<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="administrator.booking.list.label.locator-code" path="locatorCode" width="15%"/>
    <acme:list-column code="administrator.booking.list.label.purchase-moment" path="purchaseMoment" width="15%"/>
    <acme:list-column code="administrator.booking.list.label.travel-class" path="travelClass" width="15%"/>
    <acme:list-column code="administrator.booking.list.label.price" path="price" width="15%"/>
    <acme:list-column code="administrator.booking.list.label.customer" path="customerFullName" width="40%"/>
</acme:list>