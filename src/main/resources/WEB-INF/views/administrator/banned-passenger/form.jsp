<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

    <acme:input-textbox code="administrator.banned.passenger.list.label.fullName" path="fullName" />
    <acme:input-textbox code="administrator.banned.passenger.list.label.passportNumber" path="passportNumber" />
    <acme:input-moment code="administrator.banned.passenger.show.label.dateOfBirth" path="dateOfBirth" />
    <acme:input-textbox code="administrator.banned.passenger.show.label.nationality" path="nationality" />
    <acme:input-textarea code="administrator.banned.passenger.list.label.reason" path="reason" />
    <acme:input-moment code="administrator.banned.passenger.show.label.banDate" path="banDate" />
    <acme:input-moment code="administrator.banned.passenger.show.label.liftDate" path="liftDate" />
    
</acme:form>
