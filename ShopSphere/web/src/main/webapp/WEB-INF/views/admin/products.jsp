<c:set var="pageTitle" value="Manage Products"/>
<c:set var="adminPage" value="products"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
            <h1 class="h4 fw-bold mb-0">Products <span class="text-muted fs-6">(${fn:length(products)})</span></h1>
            <form class="d-flex gap-2" action="${ctx}/admin/products" method="get">
                <input class="form-control form-control-sm" name="q" placeholder="Search products" value="${fn:escapeXml(param.q)}">
                <button class="btn btn-sm btn-outline-secondary" type="submit"><i class="bi bi-search"></i></button>
            </form>
            <a class="btn btn-brand btn-sm" href="${ctx}/admin/products?action=new"><i class="bi bi-plus-lg"></i> Add product</a>
        </div>

        <div class="bg-white rounded-3 shadow-sm table-responsive">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                <tr><th>ID</th><th>Product</th><th>Category</th><th class="text-end">Price</th><th class="text-center">Off</th>
                    <th class="text-center">Stock</th><th>Status</th><th class="text-end">Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="p">
                    <tr class="${p.status ? '' : 'table-secondary'}">
                        <td>${p.productId}</td>
                        <td><c:out value="${p.name}"/><br><small class="text-muted"><c:out value="${p.brand}"/></small></td>
                        <td class="small"><c:out value="${empty p.categoryName ? '-' : p.categoryName}"/></td>
                        <td class="text-end"><ss:price value="${p.finalPrice}"/></td>
                        <td class="text-center"><fmt:formatNumber value="${p.discount}" maxFractionDigits="0"/>%</td>
                        <td class="text-center">
                            <span class="badge ${p.stock == 0 ? 'text-bg-danger' : (p.lowStock ? 'text-bg-warning' : 'text-bg-light border')}">${p.stock}</span>
                        </td>
                        <td>${p.status ? '<span class="badge text-bg-success">Active</span>' : '<span class="badge text-bg-secondary">Hidden</span>'}</td>
                        <td class="text-end text-nowrap">
                            <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/products?action=edit&amp;id=${p.productId}" title="Edit"><i class="bi bi-pencil"></i></a>
                            <form action="${ctx}/admin/products" method="post" class="d-inline">
                                <input type="hidden" name="action" value="toggle">
                                <input type="hidden" name="id" value="${p.productId}">
                                <input type="hidden" name="active" value="${not p.status}">
                                <button class="btn btn-sm ${p.status ? 'btn-outline-danger' : 'btn-outline-success'}" type="submit"
                                        title="${p.status ? 'Hide from store' : 'Show in store'}"><i class="bi ${p.status ? 'bi-eye-slash' : 'bi-eye'}"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
