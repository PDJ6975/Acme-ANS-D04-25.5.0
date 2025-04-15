<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="administrator.maintenanceRecord.list.label.moment" path="moment" width="15%"/>
    <acme:list-column code="administrator.maintenanceRecord.list.label.status" path="status" width="15%"/>
    <acme:list-column code="administrator.maintenanceRecord.list.label.nextInspectionDue" path="nextInspectionDue" width="15%"/>
    <acme:list-column code="administrator.maintenanceRecord.list.label.estimatedCost" path="estimatedCost" width="15%"/>
    <acme:list-column code="administrator.maintenanceRecord.list.label.technician" path="technician" width="40%"/>
</acme:list>