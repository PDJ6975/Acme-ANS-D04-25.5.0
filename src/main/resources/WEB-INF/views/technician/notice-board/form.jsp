<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox 
        code="technician.notice.form.label.title"
        path="title"
    />

    <acme:input-textbox 
        code="technician.notice.form.label.headline"
        path="headline"
    />

    <acme:input-url 
        code="technician.notice.form.label.url"
        path="url"
    />

    <!-- Para indicar si el curso es de pago -->
    <acme:input-checkbox 
        code="technician.notice.form.label.isPaid"
        path="isPaid"
    />

    <acme:input-money 
        code="technician.notice.form.label.price"
        path="price"
    />

    <acme:input-textbox 
        code="technician.notice.form.label.currency"
        path="currency"
    />

    <acme:input-textbox 
        code="technician.notice.form.label.instructorName"
        path="instructorName"
    />

    <acme:input-url 
        code="technician.notice.form.label.instructorUrl"
        path="instructorUrl"
    />

    <acme:input-url 
        code="technician.notice.form.label.instructorImage"
        path="instructorImage"
    />

    <acme:input-url 
        code="technician.notice.form.label.imageUrl"
        path="imageUrl"
    />

    <acme:input-textbox 
        code="technician.notice.form.label.language"
        path="language"
    />

    <!-- Se muestra la fecha de publicación en modo solo lectura -->
    <acme:input-moment 
        code="technician.notice.form.label.postedDate"
        path="postedDate"
        readonly="true"
    />
</acme:form>
