<c:set var="pageTitle" value="Manage Orders"/>
<c:set var="adminPage" value="orders"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
            <h1 class="h4 fw-bold mb-0">Orders</h1>
            <div class="btn-group btn-group-sm flex-wrap">
                <a class="btn ${empty param.status ? 'btn-dark' : 'btn-outline-dark'}" href="${ctx}/admin/orders">All</a>
                <c:forEach items="${statuses}" var="s">
                    <a class="btn ${param.status == s ? 'btn-dark' : 'btn-outline-dark'}" href="${ctx}/admin/orders?status=${s}">${fn:toLowerCase(s)}</a>
                </c:forEach>
            </div>
        </div>

        <div class="bg-white rounded-3 shadow-sm table-responsive">
            <table class="table table-hover mb-0">
                <thead class="table-light"><tr><th>Order</th><th>Customer</th><th>Date</th><th class="text-center">Items</th>
                    <th class="text-end">Amount</th><th>Payment</th><th>Status</th><th></th></tr></thead>
                <tbody>
                <c:forEach items="${orders}" var="o">
                    <tr>
                        <td>#${o.orderId}</td>
                        <td><c:out value="${o.customerName}"/></td>
                        <td class="small"><fmt:formatDate value="${o.createdAt}" type="both" dateStyle="medium" timeStyle="short"/></td>
                        <td class="text-center">${o.itemCount}</td>
                        <td class="text-end"><ss:price value="${o.totalAmount}"/></td>
                        <td class="small">${o.paymentMethod} <t:statusBadge status="${o.paymentStatus}"/></td>
                        <td><t:statusBadge status="${o.orderStatus}"/></td>
                        <td><a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/orders?id=${o.orderId}">Manage</a></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty orders}"><tr><td colspan="8" class="text-muted text-center py-4">No orders found.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
