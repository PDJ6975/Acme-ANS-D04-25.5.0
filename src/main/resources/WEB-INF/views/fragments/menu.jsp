<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Antonio" action="https://es.aliexpress.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-PabloC" action="https://www.amazon.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Adrian" action="https://www.youtube.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-Jianwu" action="https://pomodoro-tracker.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-pabolimor" action="https://eloquentclicks.com/"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.list-assignments" action="/any/flight-assignment/list" />
			<acme:menu-suboption code="master.menu.authenticated.list-service" action="/any/service/list" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.authenticated.list-airports" action="/any/airport/list" />
		    <acme:menu-suboption code="master.menu.authenticated.list-assignments" action="/any/flight-assignment/list" />
		    <acme:menu-suboption code="master.menu.authenticated.list-reviews" action="/authenticated/review/list" />
		    <acme:menu-suboption code="master.menu.authenticated.list-service" action="/any/service/list" />
		    <acme:menu-separator />
			<acme:menu-suboption code="master.menu.authenticated.money-exchage" action="/authenticated/exchange-money/perform" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="administrator.menu.populate-visa" action="/administrator/system/populate-visa"/>
			<acme:menu-suboption code="master.menu.administrator.populate-recommendation" action="/administrator/system/populate-recommendation"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-bookings" action="/administrator/booking/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-services" action="/administrator/service/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-maintenanceRecord" action="/administrator/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.administrator.show-dashboard" action="/administrator/administrator-dashboard/show" />
			<acme:menu-suboption code="master.menu.administrator.show-systemConfiguration" action="/administrator/system-currency/show" />

			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.administrator.banned-passengers" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-banned-passanger" action="/administrator/banned-passenger/current-list"/>
						<acme:menu-suboption code="master.menu.administrator.list-past-banned-passanger" action="/administrator/banned-passenger/past-list"/>
			<acme:menu-suboption code="master.menu.administrator.list-last-month-banned-passanger" action="/administrator/banned-passenger/last-month-list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.crewMember" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.crewMember.list-my-completed-assignments" action="/flight-crew-member/flight-assignment/list-completed" />			
			<acme:menu-suboption code="master.menu.crewMember.list-my-planned-assignments" action="/flight-crew-member/flight-assignment/list-planned" />
			<acme:menu-separator />
			<acme:menu-suboption code="master.menu.crewMember.show-dashboard" action="/flight-crew-member/flight-crew-member-dashboard/show" />
			<acme:menu-separator />
			<acme:menu-suboption code="master.menu.crewMember.list-visa" action="/flight-crew-member/visa-requirement/list" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list-my-flights" action="/manager/flight/list" />			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.list-my-maintenance-records" action="/technician/maintenance-record/list?mine=true" />			
			<acme:menu-suboption code="master.menu.technician.list-my-tasks" action="/technician/task/list?mine=true" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.technician.show-dashboard" action="/technician/technician-dashboard/show" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.technician.list-maintenance-record-catalogue" action="/technician/maintenance-record/list" />
			<acme:menu-suboption code="master.menu.technician.list-task-catalogue" action="/technician/task/list" />
			<acme:menu-separator />
			<acme:menu-suboption code="master.menu.technician.list-notice-board" action="/technician/notice-board/list" />
		</acme:menu-option>

		
		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
 			<acme:menu-suboption code="master.menu.assistanceAgent.list-completed" action="/assistance-agent/claim/completed-list" />
 			<acme:menu-suboption code="master.menu.assistanceAgent.list-ongoing" action="/assistance-agent/claim/ongoing-list" />
 		</acme:menu-option>
 		
 		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
		    <acme:menu-suboption code="master.menu.customer.dashboard" action="/customer/customer-dashboard/show"/>
		    <acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
		    <acme:menu-suboption code="master.menu.customer.recommendations" action="/customer/recommendation/list"/>
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
		    <acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
		    <acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
		    <acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
		    <acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
		    <acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
		    <acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
		    <acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
		    <acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
		    <acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>
		    <acme:menu-suboption code="master.menu.user-account.become-crewMember" action="/authenticated/flight-crew-member/create" access="!hasRealm('FlightCrewMember')"/>
		    <acme:menu-suboption code="master.menu.user-account.crewMember-profile" action="/authenticated/flight-crew-member/update" access="hasRealm('FlightCrewMember')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

