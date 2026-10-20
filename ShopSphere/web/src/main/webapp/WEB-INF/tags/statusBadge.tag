<%@ tag description="Coloured badge for order, payment and account statuses" pageEncoding="UTF-8" %>
<%@ attribute name="status" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${status == 'DELIVERED' || status == 'SUCCESS' || status == 'ACTIVE'}"><c:set var="color" value="success"/></c:when>
    <c:when test="${status == 'SHIPPED'}"><c:set var="color" value="primary"/></c:when>
    <c:when test="${status == 'CONFIRMED'}"><c:set var="color" value="info"/></c:when>
    <c:when test="${status == 'PENDING'}"><c:set var="color" value="warning"/></c:when>
    <c:when test="${status == 'CANCELLED' || status == 'FAILED' || status == 'BLOCKED'}"><c:set var="color" value="danger"/></c:when>
    <c:when test="${status == 'REFUNDED'}"><c:set var="color" value="dark"/></c:when>
    <c:otherwise><c:set var="color" value="secondary"/></c:otherwise>
</c:choose>
<span class="badge text-bg-${color}">${status}</span>
