<!-- filepath: c:\Workspace-25\Projects\Acme-ANS-D03-25.3.0\src\main\resources\WEB-INF\views\administrator\maintenanceRecord\form.jsp -->
<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-moment code="administrator.maintenanceRecord.form.label.moment" path="moment"/>
    <acme:input-select code="administrator.maintenanceRecord.form.label.status" path="status" choices="${statuses}"/>
    <acme:input-moment code="administrator.maintenanceRecord.form.label.nextInspectionDue" path="nextInspectionDue"/>
    <acme:input-money code="administrator.maintenanceRecord.form.label.estimatedCost" path="estimatedCost"/>
    <acme:input-textbox code="administrator.maintenanceRecord.form.label.notes" path="notes"/>
    <acme:input-textbox code="administrator.maintenanceRecord.form.label.technician" path="technician"/>
    
    <div class="form-group">
        <hr/>
    </div>
    
    <div class="form-group">
        <h4 class="form-control-label">
            <acme:print code="administrator.maintenanceRecord.form.label.tasks"/>
        </h4>
        
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th><acme:print code="administrator.task.list.label.type"/></th>
                            <th><acme:print code="administrator.task.list.label.priority"/></th>
                            <th><acme:print code="administrator.task.list.label.description"/></th>
                            <th><acme:print code="administrator.task.list.label.estimatedDuration"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <jstl:forEach items="${tasks}" var="task">
                            <tr>
                                <td><acme:print value="${task.type}"/></td>
                                <td><acme:print value="${task.priority}"/></td>
                                <td><acme:print value="${task.description}"/></td>
                                <td><acme:print value="${task.estimatedDuration}"/></td>
                            </tr>
                        </jstl:forEach>
                    </tbody>
                </table>
            </div>
    </div>
</acme:form>