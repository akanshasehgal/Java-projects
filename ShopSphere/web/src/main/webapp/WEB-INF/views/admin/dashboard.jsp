<c:set var="pageTitle" value="Admin Dashboard"/>
<c:set var="adminPage" value="dashboard"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <h1 class="h4 fw-bold mb-3">Dashboard</h1>

        <div class="row g-3 mb-4">
            <div class="col-6 col-xl-3">
                <div class="card stat-card bg-success"><div class="card-body d-flex justify-content-between align-items-center">
                    <div><div class="small">Total sales</div><div class="value"><ss:price value="${stats.totalSales}"/></div></div>
                    <i class="bi bi-currency-rupee"></i></div></div>
            </div>
            <div class="col-6 col-xl-3">
                <div class="card stat-card bg-primary"><div class="card-body d-flex justify-content-between align-items-center">
                    <div><div class="small">Orders</div><div class="value">${stats.totalOrders}</div>
                        <div class="small">${stats.pendingOrders} to process</div></div>
                    <i class="bi bi-receipt"></i></div></div>
            </div>
            <div class="col-6 col-xl-3">
                <div class="card stat-card" style="background:#7c3aed"><div class="card-body d-flex justify-content-between align-items-center">
                    <div><div class="small">Customers</div><div class="value">${stats.totalCustomers}</div></div>
                    <i class="bi bi-people"></i></div></div>
            </div>
            <div class="col-6 col-xl-3">
                <div class="card stat-card bg-warning text-dark"><div class="card-body d-flex justify-content-between align-items-center">
                    <div><div class="small">Products</div><div class="value">${stats.totalProducts}</div>
                        <div class="small">${stats.lowStockCount} low on stock</div></div>
                    <i class="bi bi-box"></i></div></div>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-xl-7">
                <div class="bg-white rounded-3 shadow-sm p-3 h-100">
                    <div class="d-flex justify-content-between"><h2 class="h6 fw-bold">Recent orders</h2>
                        <a class="small" href="${ctx}/admin/orders">All orders</a></div>
                    <table class="table table-sm table-hover mb-0">
                        <thead><tr><th>#</th><th>Customer</th><th>Date</th><th class="text-end">Amount</th><th>Status</th></tr></thead>
                        <tbody>
                        <c:forEach items="${recentOrders}" var="o">
                            <tr>
                                <td><a href="${ctx}/admin/orders?id=${o.orderId}">#${o.orderId}</a></td>
                                <td><c:out value="${o.customerName}"/></td>
                                <td class="small"><fmt:formatDate value="${o.createdAt}" pattern="dd MMM"/></td>
                                <td class="text-end"><ss:price value="${o.totalAmount}"/></td>
                                <td><t:statusBadge status="${o.orderStatus}"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recentOrders}"><tr><td colspan="5" class="text-muted">No orders yet.</td></tr></c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-xl-5">
                <div class="bg-white rounded-3 shadow-sm p-3 h-100">
                    <h2 class="h6 fw-bold text-danger"><i class="bi bi-exclamation-triangle"></i> Low stock</h2>
                    <c:choose>
                        <c:when test="${empty lowStock}"><p class="small text-muted mb-0">All products are well stocked.</p></c:when>
                        <c:otherwise>
                            <c:forEach items="${lowStock}" var="p">
                                <form action="${ctx}/admin/products" method="post" class="d-flex align-items-center gap-2 border-bottom py-2">
                                    <input type="hidden" name="action" value="stock">
                                    <input type="hidden" name="id" value="${p.productId}">
                                    <input type="hidden" name="returnTo" value="/admin/dashboard">
                                    <span class="small flex-grow-1"><c:out value="${p.name}"/></span>
                                    <span class="badge ${p.stock == 0 ? 'text-bg-danger' : 'text-bg-warning'}">${p.stock}</span>
                                    <input class="form-control form-control-sm" style="width: 70px" type="number" name="stock" min="0" value="${p.stock + 20}">
                                    <button class="btn btn-sm btn-outline-success" type="submit">Restock</button>
                                </form>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
