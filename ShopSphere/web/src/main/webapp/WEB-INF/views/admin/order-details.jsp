<c:set var="pageTitle" value="Order #${order.orderId}"/>
<c:set var="adminPage" value="orders"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <a href="${ctx}/admin/orders" class="small text-decoration-none"><i class="bi bi-arrow-left"></i> Orders</a>
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
            <div>
                <h1 class="h4 fw-bold mb-0">Order #${order.orderId} <t:statusBadge status="${order.orderStatus}"/></h1>
                <span class="small text-muted">Customer: <c:out value="${order.customerName}"/> &middot;
                    <fmt:formatDate value="${order.createdAt}" type="both" dateStyle="long" timeStyle="short"/></span>
            </div>
            <c:if test="${order.orderStatus != 'CANCELLED'}">
                <form action="${ctx}/admin/orders" method="post" class="d-flex gap-2">
                    <input type="hidden" name="id" value="${order.orderId}">
                    <select class="form-select form-select-sm" name="status" aria-label="New status">
                        <c:forEach items="${statuses}" var="s">
                            <option value="${s}" ${s == order.orderStatus ? 'selected' : ''}>${s}</option>
                        </c:forEach>
                    </select>
                    <button class="btn btn-sm btn-brand text-nowrap" type="submit">Update status</button>
                </form>
            </c:if>
        </div>
        <p class="small text-muted mt-1">Cancelling an order returns its items to stock and refunds online payments.
            Marking a COD order as delivered marks it as paid.</p>

        <%@ include file="/WEB-INF/views/fragments/order-body.jsp" %>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
