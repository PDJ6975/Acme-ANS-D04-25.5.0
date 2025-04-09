<!-- filepath: c:\Workspace-25\Projects\Acme-ANS-D03-25.3.0\src\main\resources\WEB-INF\views\administrator\booking\form.jsp -->
<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-textbox code="administrator.booking.form.label.locator-code" path="locatorCode"/>
    <acme:input-moment code="administrator.booking.form.label.purchase-moment" path="purchaseMoment"/>
    <acme:input-select code="administrator.booking.form.label.travel-class" path="travelClass" choices="${travelClasses}"/>
    <acme:input-money code="administrator.booking.form.label.price" path="price"/>
    <acme:input-textbox code="administrator.booking.form.label.credit-card-nibble" path="creditCardNibble"/>
    <acme:input-textbox code="administrator.booking.form.label.customer" path="customerFullName"/>
    
    <div class="form-group">
        <hr/>
    </div>
    
    <div class="form-group">
        <h4 class="form-control-label">
            <acme:print code="administrator.booking.form.label.passengers"/>
        </h4>
        
        <jstl:if test="${passengerCount > 0}">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th><acme:print code="administrator.passenger.list.label.fullName"/></th>
                            <th><acme:print code="administrator.passenger.list.label.passport"/></th>
                            <th><acme:print code="administrator.passenger.list.label.email"/></th>
                            <th><acme:print code="administrator.passenger.list.label.dateOfBirth"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <jstl:forEach items="${passengers}" var="passenger">
                            <tr>
                                <td><acme:print value="${passenger.fullName}"/></td>
                                <td><acme:print value="${passenger.passportNumber}"/></td>
                                <td><acme:print value="${passenger.email}"/></td>
                                <td><acme:print value="${passenger.dateOfBirth}"/></td>
                            </tr>
                        </jstl:forEach>
                    </tbody>
                </table>
            </div>
        </jstl:if>
        <jstl:if test="${passengerCount == 0}">
            <div class="alert alert-info">
                <acme:print code="administrator.booking.form.no-passengers"/>
            </div>
        </jstl:if>
    </div>
</acme:form>