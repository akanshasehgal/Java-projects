<c:set var="pageTitle" value="Order #${order.orderId}"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="d-flex flex-wrap justify-content-between align-items-center mb-2">
    <div>
        <a href="${ctx}/orders" class="small text-decoration-none"><i class="bi bi-arrow-left"></i> <fmt:message key="nav.orders"/></a>
        <h1 class="h4 fw-bold mb-0">Order #${order.orderId} <t:statusBadge status="${order.orderStatus}"/></h1>
        <span class="text-muted small">Placed on <fmt:formatDate value="${order.createdAt}" type="both" dateStyle="long" timeStyle="short"/></span>
    </div>
    <c:if test="${order.cancellable}">
        <form action="${ctx}/order" method="post" data-confirm="Cancel this order?">
            <input type="hidden" name="action" value="cancel">
            <input type="hidden" name="id" value="${order.orderId}">
            <button class="btn btn-outline-danger btn-sm" type="submit"><i class="bi bi-x-circle"></i> Cancel order</button>
        </form>
    </c:if>
</div>

<%@ include file="/WEB-INF/views/fragments/order-body.jsp" %>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
