<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<h2>
    <acme:print code="customer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.last-five-destinations"/>
        </th>
        <td>
            <acme:print value="${lastFiveDestinations}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.money-spent-last-year"/>
        </th>
        <td>
            <acme:print value="${moneySpentLastYear}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.bookings-by-travel-class"/>
        </th>
        <td>
            <acme:print value="${bookingsByTravelClass}"/>
        </td>
    </tr>
</table>

<h2>
    <acme:print code="customer.dashboard.form.title.booking-costs"/>
</h2>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.booking-cost-count"/>
        </th>
        <td>
            <acme:print value="${bookingCostCount}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.booking-cost-average"/>
        </th>
        <td>
            <acme:print value="${bookingCostAverage}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.booking-cost-minimum"/>
        </th>
        <td>
            <acme:print value="${bookingCostMinimum}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.booking-cost-maximum"/>
        </th>
        <td>
            <acme:print value="${bookingCostMaximum}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.booking-cost-standard-deviation"/>
        </th>
        <td>
            <acme:print value="${bookingCostStandardDeviation}"/>
        </td>
    </tr>
</table>

<h2>
    <acme:print code="customer.dashboard.form.title.passenger-statistics"/>
</h2>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.passenger-count"/>
        </th>
        <td>
            <acme:print value="${passengerCount}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.passenger-average"/>
        </th>
        <td>
            <acme:print value="${passengerAverage}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.passenger-minimum"/>
        </th>
        <td>
            <acme:print value="${passengerMinimum}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.passenger-maximum"/>
        </th>
        <td>
            <acme:print value="${passengerMaximum}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="customer.dashboard.form.label.passenger-standard-deviation"/>
        </th>
        <td>
            <acme:print value="${passengerStandardDeviation}"/>
        </td>
    </tr>
</table>

<acme:return/>
