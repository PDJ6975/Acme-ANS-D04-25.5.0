<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column 
        code="technician.notice.list.label.title"
        path="title"
    />

    <acme:list-column 
        code="technician.notice.list.label.headline"
        path="headline"
    />

    <acme:list-column 
        code="technician.notice.list.label.url"
        path="url"
    />

    <acme:list-column 
        code="technician.notice.list.label.isPaid"
        path="isPaid"
    />

    <acme:list-column 
        code="technician.notice.list.label.postedDate"
        path="postedDate"
    />

    <acme:list-payload path="payload"/>
</acme:list>
