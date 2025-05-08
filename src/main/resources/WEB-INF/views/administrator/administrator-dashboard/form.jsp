<%@ page %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<div class="container my-4">

    <!-- Airports grouped by Operational Scope -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="administrator.dashboard.title.airports-by-scope"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <thead>
                    <tr>
                        <th><acme:print code="administrator.dashboard.label.operational-scope"/></th>
                        <th><acme:print code="administrator.dashboard.label.count"/></th>
                    </tr>
                </thead>
                <tbody>
                    <jstl:forEach var="entry" items="${airportsByOperationalScope}">
                        <tr>
                            <td><jstl:out value="${entry.key}"/></td>
                            <td><jstl:out value="${entry.value}"/></td>
                        </tr>
                    </jstl:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Airlines grouped by Type -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="administrator.dashboard.title.airlines-by-type"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <thead>
                    <tr>
                        <th><acme:print code="administrator.dashboard.label.airline-type"/></th>
                        <th><acme:print code="administrator.dashboard.label.count"/></th>
                    </tr>
                </thead>
                <tbody>
                    <jstl:forEach var="entry" items="${airlinesByType}">
                        <tr>
                            <td><jstl:out value="${entry.key}"/></td>
                            <td><jstl:out value="${entry.value}"/></td>
                        </tr>
                    </jstl:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Ratio of Airlines with both Email and Phone -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="administrator.dashboard.title.ratio-airlines-contact"/>
            </h2>
        </div>
        <div class="card-body">
            <p>
                <acme:print code="administrator.dashboard.label.ratio-airlines-contact"/>:
                <jstl:out value="${ratioAirlinesWithEmailAndPhone}"/>
            </p>
        </div>
    </div>

    <!-- Ratios of Active and Non-Active Aircrafts -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="administrator.dashboard.title.aircraft-status"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <thead>
                    <tr>
                        <th><acme:print code="administrator.dashboard.label.aircraft-status"/></th>
                        <th><acme:print code="administrator.dashboard.label.ratio"/></th>
                    </tr>
                </thead>
                <tbody>
                    <jstl:forEach var="entry" items="${aircraftsActiveRatios}">
                        <tr>
                            <td><jstl:out value="${entry.key}"/></td>
                            <td><jstl:out value="${entry.value}"/></td>
                        </tr>
                    </jstl:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Ratio of Reviews with Score Above 5.00 -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="administrator.dashboard.title.ratio-reviews-above-five"/>
            </h2>
        </div>
        <div class="card-body">
            <p>
                <acme:print code="administrator.dashboard.label.ratio-reviews-above-five"/>:
                <jstl:out value="${ratioReviewsAboveFive}"/>
            </p>
        </div>
    </div>

    <!-- Review Statistics in Last 10 Weeks -->
    <div class="card mb-4">
        <div class="card-header">
            <h2>
                <acme:print code="administrator.dashboard.title.review-statistics"/>
            </h2>
        </div>
        <div class="card-body">
            <table class="table table-sm table-bordered">
                <tr>
                    <th><acme:print code="administrator.dashboard.label.review-count"/></th>
                    <td><jstl:out value="${reviewsCount}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="administrator.dashboard.label.review-average"/></th>
                    <td><jstl:out value="${reviewsAverage}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="administrator.dashboard.label.review-minimum"/></th>
                    <td><jstl:out value="${reviewsMinimum}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="administrator.dashboard.label.review-maximum"/></th>
                    <td><jstl:out value="${reviewsMaximum}"/></td>
                </tr>
                <tr>
                    <th><acme:print code="administrator.dashboard.label.review-stddev"/></th>
                    <td><jstl:out value="${reviewsStandardDeviation}"/></td>
                </tr>
            </table>
        </div>
    </div>

</div>

<acme:return/>
