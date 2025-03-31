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
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-bookings" action="/administrator/booking/list"/>
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
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list-my-flights" action="/manager/flight/list" />	
			<acme:menu-suboption code="master.menu.manager.list-my-legs" action="/manager/leg/list" />		
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
 			<acme:menu-suboption code="master.menu.assistanceAgent.list-completed" action="/assistance-agent/claim/completed-list" />
 			<acme:menu-suboption code="master.menu.assistanceAgent.list-ongoing" action="/assistance-agent/claim/ongoing-list" />
 			<acme:menu-suboption code="master.menu.assistanceAgent.list-tracking-log" action="/assistance-agent/tracking-log/list" />				
 		</acme:menu-option>
 		
 		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
		    <acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
		    <acme:menu-separator/>
		    <acme:menu-suboption code="master.menu.customer.user-account.update" action="/customer/customer/update"/>
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
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

