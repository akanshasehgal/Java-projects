<c:set var="pageTitle" value="Coupons"/>
<c:set var="adminPage" value="coupons"/>
<%@ include file="/WEB-INF/views/fragments/header.jsp" %>

<div class="row g-4">
    <aside class="col-lg-2"><%@ include file="/WEB-INF/views/fragments/admin-sidebar.jsp" %></aside>
    <section class="col-lg-10">
        <h1 class="h4 fw-bold mb-3">Coupons</h1>
        <div class="row g-4">
            <div class="col-xl-8">
                <div class="bg-white rounded-3 shadow-sm table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light"><tr><th>Code</th><th>Offer</th><th>Expires</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach items="${coupons}" var="cp">
                            <tr>
                                <td><code class="demo"><c:out value="${cp.code}"/></code></td>
                                <td class="small"><c:out value="${cp.description}"/></td>
                                <td class="small ${cp.expired ? 'text-danger' : ''}">
                                    <fmt:formatDate value="${cp.expiryDate}" type="date" dateStyle="medium"/>${cp.expired ? ' (expired)' : ''}</td>
                                <td>${cp.status ? '<span class="badge text-bg-success">Active</span>' : '<span class="badge text-bg-secondary">Disabled</span>'}</td>
                                <td>
                                    <form action="${ctx}/admin/coupons" method="post">
                                        <input type="hidden" name="action" value="toggle">
                                        <input type="hidden" name="id" value="${cp.couponId}">
                                        <input type="hidden" name="active" value="${not cp.status}">
                                        <button class="btn btn-sm btn-outline-secondary" type="submit">${cp.status ? 'Disable' : 'Enable'}</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-xl-4">
                <form action="${ctx}/admin/coupons" method="post" class="bg-white rounded-3 shadow-sm p-3">
                    <h2 class="h6 fw-bold">New coupon</h2>
                    <input type="hidden" name="action" value="add">
                    <input class="form-control mb-2 text-uppercase" name="code" placeholder="Code, e.g. SUMMER15" required pattern="[A-Za-z0-9]{3,20}">
                    <div class="d-flex gap-2 mb-2">
                        <select class="form-select" name="discountType" aria-label="Discount type">
                            <option value="PERCENT">% off</option>
                            <option value="FLAT">Flat &#8377; off</option>
                        </select>
                        <input class="form-control" type="number" step="0.01" min="1" name="discountValue" placeholder="Value" required>
                    </div>
                    <input class="form-control mb-2" type="number" step="0.01" min="0" name="minimumOrder" placeholder="Minimum order (&#8377;)">
                    <input class="form-control mb-2" type="number" step="0.01" min="0" name="maximumDiscount" placeholder="Max discount (&#8377;, 0 = no cap)">
                    <label class="form-label small" for="expiryDate">Expiry date</label>
                    <input class="form-control mb-3" type="date" id="expiryDate" name="expiryDate">
                    <button class="btn btn-brand btn-sm" type="submit">Create coupon</button>
                </form>
            </div>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
